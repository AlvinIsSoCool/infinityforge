package net.alvin.infinityforge.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.alvin.infinityforge.accessor.PlayerEffectsAccess;
import net.alvin.infinityforge.client.render.PlayerGlintFeatureRenderer;
import net.alvin.infinityforge.client.state.PlayerScaleAnimationState;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {
    @Inject(
            method = "<init>(Lnet/minecraft/client/render/entity/EntityRendererFactory$Context;Z)V",
            at = @At("TAIL")
    )
    private void onInit(EntityRendererFactory.Context ctx, boolean slim, CallbackInfo ci) {
        PlayerEntityRenderer renderer = (PlayerEntityRenderer)(Object) this;
        renderer.addFeature(new PlayerGlintFeatureRenderer(renderer));
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

        if (access.isCustomPhasing())
            RenderSystem.setShaderColor(1f, 1f, 1f, 0.25f);

        float animatedScale = PlayerScaleAnimationState.getAnimatedScale(player);
        if (animatedScale != 1.0f) {
            matrices.scale(animatedScale, animatedScale, animatedScale);
        }
    }

    @Inject(
            method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("RETURN")
    )
    private void renderAtReturn(AbstractClientPlayerEntity player, float yaw, float tickDelta,
                          MatrixStack matrices, VertexConsumerProvider provider,
                          int light, CallbackInfo ci) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
    }
}
