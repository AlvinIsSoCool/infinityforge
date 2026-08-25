package net.alvin.infinityforge.client.render.be;

import net.alvin.infinityforge.block.entity.BlueprintTableBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class BlueprintTableBERenderer implements BlockEntityRenderer<BlueprintTableBlockEntity> {
    @Override
    public void render(BlueprintTableBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        ItemStack output = entity.getStack(2);
        if (output.isEmpty()) return;

        BlockState state = entity.getCachedState();
        Direction facing = state.get(Properties.HORIZONTAL_FACING);

        matrices.push();
            matrices.translate(0.5, 1.3, 0.5);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-facing.asRotation()));

            MinecraftClient.getInstance().getItemRenderer().renderItem(
                    output, ModelTransformationMode.GROUND, light, overlay,
                    matrices, vertexConsumers, entity.getWorld(), 0
            );
        matrices.pop();
    }
}
