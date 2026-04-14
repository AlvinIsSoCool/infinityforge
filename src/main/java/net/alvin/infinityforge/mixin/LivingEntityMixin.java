package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.helpers.InfinityStoneHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(
            method = "takeKnockback(DDD)V",
            at = @At("HEAD"),
            cancellable = true,
            require = 1
    )
    private void cancelKnockback(double strength, double x, double z, CallbackInfo ci) {
        System.out.println("LivingEntityMixin: Mixin called!");

        if ((Object)this instanceof PlayerEntity player) {
            System.out.println("LivingEntityMixin: Is Player.");
            if (InfinityStoneHelper.isHoldingPowerStone(player)) {
                System.out.println("LivingEntityMixin: Knockback cancelled!");
                ci.cancel();
            }
        }
    }
}
