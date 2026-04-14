package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.helpers.InfinityStoneHelper;
import net.alvin.infinityforge.infinity.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(
            method = "setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cancelVelocity(Vec3d vec, CallbackInfo ci) {
        if ((Entity)(Object)this instanceof PlayerEntity player) {
            // NOTE: If making this into an event, check whether the player is
            // flying or jumping or sprinting or swimming
            // before allowing knockback handling.
            if (InfinityStoneHelper.isHoldingPowerStone(player) && vec.length() > 0.1) {
                System.out.println("EntityMixin: Holding Stone, Velocity Perfect");
                ci.cancel();
            }
        }
    }

    @Inject(
            method = "isPushable()Z",
            at = @At("RETURN"),
            cancellable = true
    )
    private void makeCollidable(CallbackInfoReturnable<Boolean> cir) {
        if ((Object)this instanceof ItemEntity itemEntity) {
            if (itemEntity.getStack().getItem() instanceof InfinityStoneItem
                    || itemEntity.getStack().getItem() instanceof InfinityGauntletItem) {
                cir.setReturnValue(true);
            }
        }
    }
}
