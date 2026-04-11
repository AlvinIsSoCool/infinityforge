package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.InfinityForgeClient;
import net.alvin.infinityforge.infinity.InfinityStoneItem;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public class ItemEntityRendererMixin {
    /*@Inject(
            method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void render(ItemEntity entity,
                       float yaw, float tickDelta,
                       MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                       int light, CallbackInfo ci) {
        if (!(entity.getStack().getItem() instanceof InfinityStoneItem stoneItem)) return;
        matrices.push();
        matrices.translate(0.0f, -0.5f, 0.0f);
        InfinityForgeClient.STONE_RENDERER.render(
                entity.getStack(), ModelTransformationMode.GROUND,
                matrices, vertexConsumers,
                light, OverlayTexture.DEFAULT_UV,
                stoneItem.getStoneType()
        );
        matrices.pop();

        ci.cancel();
    } */

    @ModifyVariable(
            method = "render(Lnet/minecraft/entity/ItemEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("STORE"),
            ordinal = 0
    )
    private float noBob(float bob, ItemEntity entity) {
        if (entity.getStack().getItem() instanceof InfinityStoneItem) return 0f;
        return bob;
    }
}