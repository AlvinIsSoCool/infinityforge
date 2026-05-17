package net.alvin.infinityforge.client.render;

import net.alvin.infinityforge.item.FakeItem;
import net.alvin.infinityforge.registry.ModItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class FakeItemRenderer {
    public void render(ItemStack stack, ModelTransformationMode mode,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, int overlay) {
        ItemStack disguiseStack = new ItemStack(FakeItem.getDisguise(stack));
        if (disguiseStack.getItem() == ModItems.FAKE_ITEM) return;
        matrices.translate(0.5, 0.5, 0.5);
        MinecraftClient.getInstance().getItemRenderer().renderItem(
                disguiseStack, mode,
                light, overlay,
                matrices, vertexConsumers,
                null, 0
        );
    }
}
