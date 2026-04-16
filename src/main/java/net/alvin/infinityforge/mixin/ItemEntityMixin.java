package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.infinity.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(
            method = "onPlayerCollision(Lnet/minecraft/entity/player/PlayerEntity;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void noAutoPickup(PlayerEntity player, CallbackInfo ci) {
        ItemEntity self = (ItemEntity)(Object)this;
        if (self.getStack().getItem() instanceof InfinityStoneItem) {
            ci.cancel();
        }
    }

    @Inject(
            method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cancelDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        ItemEntity self = (ItemEntity)(Object)this;
        if (self.getStack().getItem() instanceof InfinityStoneItem
                || self.getStack().getItem() instanceof InfinityGauntletItem) {
            cir.setReturnValue(false);
        }
    }

    /*@Inject(
            method = "tick()V",
            at = @At("TAIL")
    )
    private void onTick(CallbackInfo ci) {
        ItemEntity self = (ItemEntity)(Object) this;
        if (self.getWorld().isClient ||
                !(self.getStack().getItem() instanceof InfinityStoneItem)) return;

        Box searchBox = self.getBoundingBox().expand(0.5);
        List<ItemEntity> nearbyItems = self.getWorld().getEntitiesByClass(
                ItemEntity.class,
                searchBox,
                e -> e != self && e.getStack().getItem() instanceof InfinityStoneItem
        );

        for (ItemEntity other : nearbyItems) {
            Vec3d diff = self.getPos().subtract(other.getPos());
            double dist = Math.max(diff.length(), 0.1);
            double strength = Math.min(0.005 / (dist * dist), 0.02);
            self.addVelocity(diff.normalize().multiply(strength));
        }
    }*/

    @Inject(
            method = "<init>(Lnet/minecraft/entity/ItemEntity;)V",
            at = @At("TAIL")
    )
    private void neverAge(ItemEntity entity, CallbackInfo ci) {
        ItemEntity self = (ItemEntity)(Object) this;
        if (self.getStack().getItem() instanceof InfinityStoneItem
                || self.getStack().getItem() instanceof InfinityGauntletItem)
            self.setNeverDespawn();
    }
}
