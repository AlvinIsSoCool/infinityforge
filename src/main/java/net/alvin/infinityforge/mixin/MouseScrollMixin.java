package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.infinity.InfinityGauntletItem;
import net.minecraft.client.MinecraftClient;
import net.alvin.infinityforge.client.state.GauntletClientState;
import net.minecraft.client.Mouse;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseScrollMixin {
    @Inject(
            method = "onMouseScroll",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen != null) return;
        if (client.player == null) return;
        if (!client.player.isSneaking()) return;

        ItemStack gauntlet = InfinityGauntletItem.findGauntlet(client.player);
        if (gauntlet == null) return;

        int totalAbilities = ((InfinityGauntletItem) gauntlet.getItem())
                .getVisibleAbilities(gauntlet).size();

        if (totalAbilities <= 6) return;

        GauntletClientState.scroll(totalAbilities, (int) -Math.signum(vertical));
        ci.cancel(); // prevent hotbar slot change
    }
}
