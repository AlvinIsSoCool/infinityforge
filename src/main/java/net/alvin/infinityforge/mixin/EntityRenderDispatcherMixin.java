package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.util.accessor.PlayerEffectsAccess;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @Inject(
            method = "renderShadow(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/entity/Entity;FFLnet/minecraft/world/WorldView;F)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void infinityforge$onRenderShadow(MatrixStack matrices, VertexConsumerProvider vertexConsumers,
                                       Entity entity, float opacity, float tickDelta,
                                       WorldView world, float radius, CallbackInfo ci) {
        if (entity instanceof PlayerEffectsAccess access
                && access.infinityforge$isInvisible()) ci.cancel(); // TODO: Flagged for consideration for phasing.
    }
}
