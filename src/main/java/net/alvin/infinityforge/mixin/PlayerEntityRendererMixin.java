package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.accessor.PlayerEffectsAccess;
import net.alvin.infinityforge.client.render.player.PlayerForcefieldFeatureRenderer;
import net.alvin.infinityforge.client.render.player.AlphaMultiplyingVertexConsumer;
import net.alvin.infinityforge.client.state.PlayerScaleAnimationState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @Unique
    private boolean isCustomPhasing = false;

    @Inject(
            method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;Z)V",
            at = @At("TAIL")
    )
    private void onInit(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        PlayerEntityRenderer renderer = (PlayerEntityRenderer)(Object) this;
        renderer.addFeature(new PlayerForcefieldFeatureRenderer(renderer));
    }

    @Inject(
            method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onRender(AbstractClientPlayerEntity player, float yaw, float tickDelta,
                          MatrixStack matrices, VertexConsumerProvider provider,
                          int light, CallbackInfo ci) {
        PlayerEffectsAccess access = (PlayerEffectsAccess) player;
        if (access.isCustomInvisible()) {
            ci.cancel();
            return;
        }
        isCustomPhasing = access.isCustomPhasing();
    }

    @Inject(
            method = "scale(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;F)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void applyCustomScale(AbstractClientPlayerEntity player, MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        float animatedScale = PlayerScaleAnimationState.getAnimatedScale(player);
        if (animatedScale != 1.0f) {
            matrices.scale(animatedScale, animatedScale, animatedScale);
            ci.cancel();
        }
    }

    @ModifyArg(
            method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
            ),
            index = 4
    )
    private VertexConsumerProvider applyPhasingAlpha(VertexConsumerProvider original) {
        if (isCustomPhasing) return layer
                -> new AlphaMultiplyingVertexConsumer(original.getBuffer(layer), 0.25f);
        return original;
    }
}
