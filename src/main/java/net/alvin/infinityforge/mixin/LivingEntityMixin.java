package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.infinity.InfinityStoneItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(
            method = "takeKnockback",
            at = @At("HEAD"),
            cancellable = true
    )
    private void cancelKnockback(double strength, double x, double z, CallbackInfo ci) {
        if ((LivingEntity)(Object)this instanceof PlayerEntity player) {
            if (isHoldingPowerStone(player)) ci.cancel();
        }
    }

    // INCOMPLETE.
    private boolean isHoldingPowerStone(PlayerEntity player) {
        ItemStack mainHandStack = player.getMainHandStack();
        ItemStack offHandStack = player.getOffHandStack();

        if (mainHandStack.getItem() instanceof InfinityStoneItem || offHandStack.getItem() instanceof InfinityStoneItem) {
            return true;
        }
        else return false;
    }
}
