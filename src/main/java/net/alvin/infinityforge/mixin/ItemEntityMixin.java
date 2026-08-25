package net.alvin.infinityforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.alvin.infinityforge.registry.ModTags;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Shadow
    private int itemAge;

    @Inject(
            method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void infinityforge$noAutoPickupItem(PlayerEntity player, CallbackInfo ci) {
        ItemEntity self = (ItemEntity)(Object)this;
        if (self.getStack().isIn(ModTags.Items.INFINITY_ITEMS)) ci.cancel();
    }

    @Inject(
            method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void infinityforge$cancelDamageIE(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ItemEntity self = (ItemEntity)(Object)this;
        if (self.getStack().isIn(ModTags.Items.INFINITY_ITEMS)) cir.setReturnValue(false);
    }

    @ModifyReturnValue(
            method = "isAttackable()Z",
            at = @At("RETURN")
    )
    private boolean infinityforge$makeAttackableIE(boolean original) {
        if (original) return true;
        return ((ItemEntity)(Object)this).getStack().isIn(ModTags.Items.INFINITY_TESSERACTS);
    }

    @Inject(
            method = "tick()V",
            at = @At("TAIL")
    )
    private void infinityforge$onTickIE(CallbackInfo ci) {
        ItemEntity self = (ItemEntity)(Object) this;
        if (self.getWorld().isClient) return;
        if (!self.getStack().isIn(ModTags.Items.INFINITY_ITEMS)) return;

        this.itemAge = 0;
        Box searchBox = self.getBoundingBox().expand(0.25);
        List<ItemEntity> nearbyItems = self.getWorld().getEntitiesByClass(
                ItemEntity.class,
                searchBox,
                e -> e != self && self.getStack().isIn(ModTags.Items.INFINITY_ITEMS)
        );

        for (ItemEntity other : nearbyItems) {
            self.pushAwayFrom(other);
        }
    }

    @Inject(
            method = "applyWaterBuoyancy()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void infinityforge$onWaterBuoyancy(CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        if (self.getStack().isIn(ModTags.Items.INFINITY_ITEMS)) {
            Vec3d vec3d = self.getVelocity();
            self.setVelocity(vec3d.x * 0.96F, -0.04, vec3d.z * 0.96F);
            ci.cancel();
        }
    }

    @Inject(
            method = "applyLavaBuoyancy()V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void infinityforge$onLavaBuoyancy(CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        if (self.getStack().isIn(ModTags.Items.INFINITY_ITEMS)) {
            Vec3d vec3d = self.getVelocity();
            self.setVelocity(vec3d.x * 0.93F, -0.035, vec3d.z * 0.93F);
            ci.cancel();
        }
    }
}
