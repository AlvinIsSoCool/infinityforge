package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.infinity.InfinityStoneItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {
    @Inject(
            method = "onPlayerCollision",
            at = @At("HEAD"),
            cancellable = true
    )
    private void noAutoPickup(PlayerEntity player, CallbackInfo ci) {
        ItemEntity self = (ItemEntity)(Object)this;
        if (self.getStack().getItem() instanceof InfinityStoneItem) {
            ci.cancel();
        }
    }
}
