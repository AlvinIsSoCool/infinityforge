package net.alvin.infinityforge.mixin;

import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.effect.ModStatusEffects;
import net.minecraft.client.MinecraftClient;
import net.alvin.infinityforge.client.state.GauntletClientState;
import net.minecraft.client.Mouse;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

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

        if (client.player.hasStatusEffect(ModStatusEffects.SCROLL_LOCKED_EFFECT)) {
            ci.cancel();
            return;
        }

        if (client.player.isSneaking()) {
            ItemStack gauntletStack = InfinityGauntletItem.findGauntlet(client.player);
            if (gauntletStack == null) return;

            List<InfinityStoneType> activeStones = InfinityGauntletItem.getAddedStones(gauntletStack);
            int totalAbilities = InfinityGauntletItem
                    .getVisibleAbilities(activeStones).size();
            if (totalAbilities <= 6) return;

            GauntletClientState.scroll(totalAbilities, (int) -Math.signum(vertical));
            ci.cancel();
        }
    }
}
