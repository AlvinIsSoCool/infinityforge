package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.registry.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    @Inject(
            method = "tickNewAi",
            at = @At("TAIL")
    )
    private void onTickNewAi(CallbackInfo ci) {
        MobEntity mob = (MobEntity)(Object)this;
        LivingEntity target = mob.getTarget();
        if (!(target instanceof ServerPlayerEntity player)) return;

        boolean isHoldingMindStoneMainHand = player.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.MIND_STONE);
        boolean isHoldingMindStoneOffHand = player.getStackInHand(Hand.OFF_HAND).isOf(ModItems.MIND_STONE);

        if (!isHoldingMindStoneMainHand && !isHoldingMindStoneOffHand) return;

        mob.setTarget(null);
        mob.setAttacker(null);
        mob.getNavigation().stop();

        if (mob instanceof Angerable a) {
            a.setAngerTime(0);
            a.setAngryAt(null);
        }
    }

    @Inject(
            method = "tryAttack",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onTryAttack(Entity target, CallbackInfoReturnable<Boolean> cir) {
        if (!(target instanceof ServerPlayerEntity player)) return;

        boolean isHoldingMindStoneMainHand = player.getStackInHand(Hand.MAIN_HAND).isOf(ModItems.MIND_STONE);
        boolean isHoldingMindStoneOffHand = player.getStackInHand(Hand.OFF_HAND).isOf(ModItems.MIND_STONE);

        if (isHoldingMindStoneMainHand || isHoldingMindStoneOffHand) cir.setReturnValue(false);
    }
}
