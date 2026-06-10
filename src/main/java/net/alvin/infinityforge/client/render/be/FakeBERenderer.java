package net.alvin.infinityforge.client.render.be;

import net.alvin.infinityforge.block.entity.FakeBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.world.World;

public class FakeBERenderer implements BlockEntityRenderer<FakeBlockEntity> {
    @Override
    public void render(FakeBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        BlockState disguiseState = entity.getDisguiseState();
        World world = entity.getWorld();
        if (disguiseState == null || world == null) return;

        MinecraftClient client = MinecraftClient.getInstance();
        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayers.getBlockLayer(disguiseState));

        client.getBlockRenderManager().renderBlock(
                disguiseState,
                entity.getPos(),
                world,
                matrices,
                consumer,
                false,
                world.random
        );
    }
}
