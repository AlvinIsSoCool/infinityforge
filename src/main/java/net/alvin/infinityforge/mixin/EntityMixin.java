package net.alvin.infinityforge.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.helpers.InfinityStoneHelper;
import net.alvin.infinityforge.infinity.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneItem;
import net.alvin.infinityforge.infinity.InfinityTesseractItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(
            method = "setVelocity(Lnet/minecraft/util/math/Vec3d;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cancelVelocity(Vec3d vec, CallbackInfo ci) {
        if ((Object)this instanceof PlayerEntity player) {
            // NOTE: If making this into an event, check whether the player is
            // flying or jumping or sprinting or swimming
            // before allowing knockback handling.
            if (InfinityStoneHelper.isHoldingPowerStone(player) && vec.length() > 0.1) {
                InfinityForge.LOGGER.info("EntityMixin: Holding Stone, Velocity Perfect");
                ci.cancel();
            }
        }
    }

    @ModifyReturnValue(
            method = "isPushable()Z",
            at = @At("RETURN")
    )
    private boolean makeCollidable(boolean original) {
        if (original) return true;
        if (!((Object)this instanceof ItemEntity itemEntity)) return false;
        Item item = itemEntity.getStack().getItem();
        return item instanceof InfinityStoneItem
                || item instanceof InfinityGauntletItem
                || item instanceof InfinityTesseractItem;
    }

    @ModifyReturnValue(
            method = "canHit()Z",
            at = @At("RETURN")
    )
    private boolean makeHittable(boolean original) {
        if (original) return true;
        return ((Object)this instanceof ItemEntity itemEntity)
                && itemEntity.getStack().getItem() instanceof InfinityTesseractItem;
    }
}
