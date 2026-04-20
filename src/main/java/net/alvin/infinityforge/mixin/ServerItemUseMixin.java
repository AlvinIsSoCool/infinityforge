package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.server.state.PendingStonePickups;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerItemUseMixin {
    @Inject(
            method = "interactItem(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onInteractItem(ServerPlayerEntity player, World world,
                                ItemStack stack, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (PendingStonePickups.isPending(player)) {
            PendingStonePickups.clear(player);
            cir.setReturnValue(ActionResult.FAIL);
        }
    }
}