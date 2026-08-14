package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.client.state.GauntletClientState;
import net.alvin.infinityforge.config.server.InfinityForgeServerConfig;
import net.alvin.infinityforge.util.accessor.PlayerEffectsAccess;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.abilities.impl.ModGauntletAbilities;
import net.alvin.infinityforge.infinity.ModStones;
import net.alvin.infinityforge.registry.ModDamageSources;
import net.alvin.infinityforge.server.event.InfinityStoneEventHandler;
import net.alvin.infinityforge.server.state.GauntletToggleState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin implements PlayerEffectsAccess {
    @Unique
    private static final TrackedData<Float> CUSTOM_SCALE =
            DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.FLOAT);
    @Unique
    private static final TrackedData<Boolean> CUSTOM_INVISIBLE =
            DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> CUSTOM_PHASING =
            DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> CUSTOM_FORCEFIELD_ACTIVE =
            DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Unique
    private static final TrackedData<Boolean> CUSTOM_FORCEFIELD_HIT =
            DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    @Inject(
            method = "initDataTracker()V",
            at = @At("TAIL")
    )
    private void addTracker(CallbackInfo ci) {
        DataTracker tracker = ((Entity)(Object) this).getDataTracker();
        tracker.startTracking(CUSTOM_SCALE, 1.0f);
        tracker.startTracking(CUSTOM_INVISIBLE, false);
        tracker.startTracking(CUSTOM_PHASING, false);
        tracker.startTracking(CUSTOM_FORCEFIELD_ACTIVE, false);
        tracker.startTracking(CUSTOM_FORCEFIELD_HIT, false);
    }

    @Override
    public float infinityforge$getScale() {
        return ((Entity)(Object) this).getDataTracker().get(CUSTOM_SCALE);
    }

    @Override
    public void infinityforge$setScale(float scale) {
        ((Entity)(Object) this).getDataTracker().set(CUSTOM_SCALE, scale);
    }

    @Override
    public boolean infinityforge$isInvisible() {
        return ((Entity)(Object) this).getDataTracker().get(CUSTOM_INVISIBLE);
    }

    @Override
    public void infinityforge$setInvisible(boolean invisible) {
        ((Entity)(Object) this).getDataTracker().set(CUSTOM_INVISIBLE, invisible);
    }

    @Override
    public boolean infinityforge$isPhasing() {
        return ((Entity)(Object) this).getDataTracker().get(CUSTOM_PHASING);
    }

    @Override
    public void infinityforge$setPhasing(boolean phasing) {
        ((Entity)(Object) this).getDataTracker().set(CUSTOM_PHASING, phasing);
    }

    @Override
    public boolean infinityforge$isForcefieldActive() {
        return ((Entity)(Object) this).getDataTracker().get(CUSTOM_FORCEFIELD_ACTIVE);
    }

    @Override
    public void infinityforge$setForcefieldActive(boolean forcefieldActive) {
        ((Entity)(Object) this).getDataTracker().set(CUSTOM_FORCEFIELD_ACTIVE, forcefieldActive);
    }

    @Override
    public boolean infinityforge$isForcefieldHit() {
        return ((Entity)(Object) this).getDataTracker().get(CUSTOM_FORCEFIELD_HIT);
    }

    @Override
    public void infinityforge$setForcefieldHit(boolean forcefieldHit) {
        ((Entity)(Object) this).getDataTracker().set(CUSTOM_FORCEFIELD_HIT, forcefieldHit);
    }

    @Inject(
            method = "getDimensions(Lnet/minecraft/entity/EntityPose;)Lnet/minecraft/entity/EntityDimensions;",
            at = @At("RETURN"),
            cancellable = true
    )
    private void onGetDimensions(EntityPose pose, CallbackInfoReturnable<EntityDimensions> cir) {
        if (infinityforge$getScale() != 1.0f) cir.setReturnValue(cir.getReturnValue().scaled(infinityforge$getScale()));
    }

    @Redirect(
            method = "tick()V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;isSpectator()Z",
                    ordinal = 0
            )
    )
    private boolean redirectNoClip(PlayerEntity self) {
        Identifier phasingId = ModGauntletAbilities.PHASING.getId();
        if (self.getWorld().isClient()) {
            return GauntletClientState.ACTIVE_TOGGLES.contains(phasingId)
                    || self.isSpectator();
        }
        return GauntletToggleState.isActive(self, phasingId)
                || self.isSpectator();
    }

    @Inject(
            method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void godModeInvulnerablilityBypass(DamageSource source,
                                               float amount, CallbackInfoReturnable<Boolean> cir) {
        PlayerEntity self = (PlayerEntity)(Object) this;
        if (self.getWorld().isClient()) return;
        if (!self.getAbilities().invulnerable) return;
        if (!InfinityForgeServerConfig.INSTANCE.godMode) return;

        Entity attacker = source.getAttacker();
        if (!(attacker instanceof ServerPlayerEntity attackerPlayer)) return;

        ItemStack stack = InfinityGauntletItem.findGauntlet(attackerPlayer);
        if (stack == null) return;
        if (!new HashSet<>(InfinityGauntletItem.getAddedStones(stack))
                .containsAll(ModStones.ALL_STONES)) return;

        InfinityStoneEventHandler.applyDamageInfinity(self,
                ModDamageSources.powerStone(self.getWorld()), true);
        cir.setReturnValue(true);
    }
}
