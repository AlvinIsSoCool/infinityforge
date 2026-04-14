package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.infinity.InfinityStoneItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(
            method = "interact(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/Hand;)Lnet/minecraft/util/ActionResult;",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onInteract(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if(!(entity instanceof ItemEntity itemEntity)) return;
        if (!(itemEntity.getStack().getItem() instanceof InfinityStoneItem)) return;

        PlayerEntity self = (PlayerEntity)(Object) this;
        if (!self.getWorld().isClient) {
            self.getInventory().insertStack(itemEntity.getStack().copy());
            itemEntity.discard();
        }
        cir.setReturnValue(ActionResult.SUCCESS);
    }
}
