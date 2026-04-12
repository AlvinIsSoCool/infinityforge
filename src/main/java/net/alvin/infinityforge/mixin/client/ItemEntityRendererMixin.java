package net.alvin.infinityforge.mixin.client;

import net.alvin.infinityforge.infinity.InfinityStoneItem;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin {
    @Redirect(
            method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V",
                    ordinal = 0
            )
    )
    private void noBob(MatrixStack matrices, float x, float y, float z, ItemEntity entity, float f, float g) {
        if (entity.getStack().getItem() instanceof InfinityStoneItem) {
            matrices.translate(x, 0.1F, z);
            return;
        }
        matrices.translate(x, y, z);
    }

    @Redirect(
            method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V",
                    ordinal = 0
            )
    )
    private void noRot(MatrixStack matrices, Quaternionf q, ItemEntity entity, float f, float g) {
        if (entity.getStack().getItem() instanceof InfinityStoneItem) return;
        matrices.multiply(q);
    }
}