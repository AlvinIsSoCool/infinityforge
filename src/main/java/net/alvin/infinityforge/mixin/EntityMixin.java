package net.alvin.infinityforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.alvin.infinityforge.util.accessor.PlayerEffectsAccess;
import net.alvin.infinityforge.client.state.PlayerScaleAnimationState;
import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.registry.ModTags;
import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(
            method = "setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void infinityforge$powerStoneCancelVelocity(Vec3d velocity, CallbackInfo ci) {
        if ((Object) this instanceof LivingEntity entity) {
            if (entity.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.POWER_STONE)
                    || entity.getStackInHand(Hand.OFF_HAND).isOf(ModItems.POWER_STONE))
                ci.cancel();
        }
    }

    @ModifyVariable(
            method = "setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private Vec3d infinityforge$phasingVelocity(Vec3d velocity) {
        if ((Object) this instanceof PlayerEffectsAccess access) {
            if (access.infinityforge$isPhasing()) {
                return new Vec3d(velocity.x, 0.0, velocity.z);
            }
        }
        return velocity;
    }

    @SuppressWarnings("ConstantValue")
    @ModifyReturnValue(
            method = "getStandingEyeHeight()F",
            at = @At("RETURN")
    )
    private float infinityforge$applyEyeScaleAnimation(float original) {
        if ((Object) this instanceof PlayerEntity player && player.getWorld().isClient()) {
            if (player.getPose() == EntityPose.STANDING) {
                float animatedScale = PlayerScaleAnimationState.getAnimatedScale(player);
                return original * animatedScale;
            }
            return original;
        }
        return original;
    }

    @SuppressWarnings("ConstantConditions")
    @ModifyReturnValue(
            method = "isPushable()Z",
            at = @At("RETURN")
    )
    private boolean infinityforge$makeCollidableIE(boolean original) {
        if (original) return true;
        if (!((Object) this instanceof ItemEntity itemEntity)) return false;
        return itemEntity.getStack().isIn(ModTags.Items.INFINITY_ITEMS);
    }

    @SuppressWarnings("ConstantConditions")
    @ModifyReturnValue(
            method = "canHit()Z",
            at = @At("RETURN")
    )
    private boolean infinityforge$makeHittableIE(boolean original) {
        if (original) return true;
        return ((Object) this instanceof ItemEntity itemEntity)
                && itemEntity.getStack().isIn(ModTags.Items.INFINITY_ITEMS);
    }

    @ModifyReturnValue(
            method = "canBeHitByProjectile()Z",
            at = @At("RETURN")
    )
    private boolean infinityforge$preventProjectileHit(boolean original) {
        if ((Object) this instanceof PlayerEntity player)
            if (((PlayerEffectsAccess) player).infinityforge$isPhasing()) return false;
        return original;
    }
}
