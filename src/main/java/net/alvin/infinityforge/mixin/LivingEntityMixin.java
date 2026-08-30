package net.alvin.infinityforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.alvin.infinityforge.config.server.InfinityForgeServerConfig;
import net.alvin.infinityforge.entity.effect.HarmfulEffectsBypass;
import net.alvin.infinityforge.util.accessor.PlayerEffectsAccess;
import net.alvin.infinityforge.client.state.PlayerScaleAnimationState;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.entity.effect.ModStatusEffects;
import net.alvin.infinityforge.infinity.ModStones;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @ModifyReturnValue(
            method = "getEyeHeight(Lnet/minecraft/entity/EntityPose;Lnet/minecraft/entity/EntityDimensions;)F",
            at = @At("RETURN")
    )
    private float infinityforge$applyEyeHeight(float original, EntityPose pose) {
        if ((Object) this instanceof PlayerEntity player) {
            if (player.getWorld().isClient()) {
                if (pose == EntityPose.STANDING) return original;
                float animatedScale = PlayerScaleAnimationState.getAnimatedScale(player);
                return original * animatedScale;
            } else {
                float scale = ((PlayerEffectsAccess) player).infinityforge$getScale();
                if (scale != 1.0f) return original * scale;
            }
        }
        return original;
    }

    @ModifyReturnValue(
            method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z",
            at = @At("RETURN")
    )
    private boolean infinityforge$canTargetPlayer(boolean original, LivingEntity target) {
        if (target instanceof ServerPlayerEntity player) {
            boolean isHoldingMindStoneMainHand = player.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.MIND_STONE);
            boolean isHoldingMindStoneOffHand = player.getStackInHand(Hand.OFF_HAND).isOf(ModItems.MIND_STONE);
            boolean isInvisible = ((PlayerEffectsAccess) player).infinityforge$isInvisible();

            if (isHoldingMindStoneMainHand || isHoldingMindStoneOffHand || isInvisible) {
                return false;
            }
        }
        return original;
    }

    @Inject(
            method = "travel(Lnet/minecraft/util/math/Vec3d;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void infinityforge$applyMovementLock(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object) this;
        if (entity.hasStatusEffect(ModStatusEffects.MOVEMENT_LOCKED_EFFECT))
            ci.cancel();
    }

    @ModifyReturnValue(
            method = "getJumpBoostVelocityModifier()F",
            at = @At("RETURN")
    )
    private float infinityforge$getJumpBoostVelocityModifier(float original) {
        if ((Object) this instanceof PlayerEntity player) {
            ItemStack gauntletStack = InfinityGauntletItem.findGauntlet(player);
            if (gauntletStack == null) return original;
            List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(gauntletStack);
            if (activeStones.contains(ModStones.POWER)) return 0.25f;
        }
        return original;
    }

    @ModifyReturnValue(
            method = "canHaveStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z",
            at = @At("RETURN")
    )
    private boolean infinityforge$canHaveStatusEffect(boolean original, StatusEffectInstance effect) {
        LivingEntity entity = (LivingEntity)(Object) this;

        if (entity.getWorld().isClient()) return original;
        if (entity instanceof PlayerEntity player) {
            ItemStack gauntletStack = InfinityGauntletItem.findGauntlet(player);
            if (gauntletStack == null) return original;
            List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(gauntletStack);

            boolean isHarmful = effect.getEffectType().getCategory().equals(StatusEffectCategory.HARMFUL);
            boolean allStonesEquipped = activeStones.size() == ModStones.ALL_STONES.size();
            if (allStonesEquipped && isHarmful) return HarmfulEffectsBypass.isExempt(effect) && original;
        }
        return original;
    }

    @ModifyReturnValue(
            method = "modifyAppliedDamage(Lnet/minecraft/entity/damage/DamageSource;F)F",
            at = @At("RETURN")
    )
    private float infinityforge$modifyAppliedDamage(float original, DamageSource source, float amount) {
        if ((Object) this instanceof PlayerEntity player) {
            ItemStack gauntletStack = InfinityGauntletItem.findGauntlet(player);
            if (gauntletStack == null) return original;
            List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(gauntletStack);
            return infinityforge$getModifiedDamage(original, activeStones);
        }
        return original;
    }

    @Unique
    private static float infinityforge$getModifiedDamage(float original, List<InfinityStoneType> activeStones) {
        boolean allStonesEquipped = activeStones.size() == ModStones.ALL_STONES.size();
        if (allStonesEquipped) {
            return InfinityForgeServerConfig.INSTANCE.godMode
                    ? 0f
                    : original * InfinityForgeServerConfig.INSTANCE.allStonesDamageResistance;
        }
        if (activeStones.contains(ModStones.POWER))
            return original * InfinityForgeServerConfig.INSTANCE.powerStoneDamageResistance;
        return original;
    }
}