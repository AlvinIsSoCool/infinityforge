package net.alvin.infinityforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.alvin.infinityforge.accessor.PlayerEffectsAccess;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(Entity.class)
public class EntityMixin {
    @Inject(
            method = "setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void setVelocity(Vec3d velocity, CallbackInfo ci) {
        if ((Object) this instanceof LivingEntity entity) {
            if (entity.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.POWER_STONE)
                    || entity.getStackInHand(Hand.OFF_HAND).isOf(ModItems.POWER_STONE))
                ci.cancel();
        }
    }

    @ModifyReturnValue(
            method = "getStandingEyeHeight()F",
            at = @At("RETURN")
    )
    private float applyEyeScaleAnimation(float original) {
        if ((Object) this instanceof PlayerEntity player && player.getWorld().isClient()) {
            if (player.getPose() == EntityPose.STANDING) {
                float animatedScale = PlayerScaleAnimationState.getAnimatedScale(player);
                return original * animatedScale;
            }
            return original;
        }
        return original;
    }

    @ModifyReturnValue(
            method = "isPushable()Z",
            at = @At("RETURN")
    )
    private boolean makeCollidable(boolean original) {
        if (original) return true;
        if (!((Object) this instanceof ItemEntity itemEntity)) return false;
        return itemEntity.getStack().isIn(ModTags.Items.INFINITY_ITEMS);
    }

    @ModifyReturnValue(
            method = "canHit()Z",
            at = @At("RETURN")
    )
    private boolean makeHittable(boolean original) {
        if (original) return true;
        return ((Object)this instanceof ItemEntity itemEntity)
                && itemEntity.getStack().isIn(ModTags.Items.INFINITY_TESSERACTS);
    }

    @ModifyReturnValue(
            method = "canBeHitByProjectile()Z",
            at = @At("RETURN")
    )
    private boolean preventProjectileHit(boolean original) {
        if ((Object) this instanceof PlayerEntity player)
            if (((PlayerEffectsAccess) player).isCustomPhasing())
                return false;
        return original;
    }
}
