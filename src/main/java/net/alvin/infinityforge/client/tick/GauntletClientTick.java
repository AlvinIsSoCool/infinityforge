package net.alvin.infinityforge.client.tick;

import net.alvin.infinityforge.abilities.base.ActiveAbility;
import net.alvin.infinityforge.abilities.base.GauntletAbility;
import net.alvin.infinityforge.abilities.base.HeldAbility;
import net.alvin.infinityforge.abilities.base.ToggleAbility;
import net.alvin.infinityforge.client.state.GauntletClientState;
import net.alvin.infinityforge.infinity.InfinityGauntletItem;
import net.alvin.infinityforge.network.c2s.GauntletAbilityC2SPacket;
import net.alvin.infinityforge.network.c2s.GauntletHeldC2SPacket;
import net.alvin.infinityforge.network.c2s.GauntletToggleC2SPacket;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

import java.util.List;

import static net.alvin.infinityforge.client.input.GauntletKeybinds.SLOT_KEYS;

public class GauntletClientTick {
    public static void initialize() {
        ClientTickEvents.END_CLIENT_TICK.register(GauntletClientTick::onTick);
    }

    private static void onTick(MinecraftClient client) {
        if (client.player == null) return;

        ItemStack gauntlet = InfinityGauntletItem.findGauntlet(client.player);
        InfinityGauntletItem gauntletItem = gauntlet != null
                ? (InfinityGauntletItem) gauntlet.getItem() : null;

        List<GauntletAbility> abilities = gauntletItem != null
                ? gauntletItem.getVisibleAbilities(gauntlet)
                : List.of();

        GauntletClientState.scrollOffset = abilities.size() <= 6 ? 0
                : Math.min(Math.max(GauntletClientState.scrollOffset, 0), abilities.size() - 6);

        for (int slot = 0; slot < 6; slot++) {
            int index = GauntletClientState.scrollOffset + slot;
            if (index >= abilities.size()) continue;

            GauntletAbility ability = abilities.get(index);

            if (ability instanceof ActiveAbility a) {
                while (SLOT_KEYS[slot].wasPressed())
                    ClientPlayNetworking.send(new GauntletAbilityC2SPacket(a.getId()));

            } else if (ability instanceof ToggleAbility t) {
                while (SLOT_KEYS[slot].wasPressed())
                    ClientPlayNetworking.send(new GauntletToggleC2SPacket(t.getId()));

            } else if (ability instanceof HeldAbility h) {
                boolean pressing = SLOT_KEYS[slot].isPressed();
                boolean wasHeld = GauntletClientState.heldActive.contains(h.getId());
                boolean lockedOut = GauntletClientState.heldLockedOut.contains(h.getId());

                // Clear lockout once key is physically released
                if (!pressing && lockedOut)
                    GauntletClientState.heldLockedOut.remove(h.getId());

                if (lockedOut) continue;

                if (pressing && !wasHeld) {
                    GauntletClientState.heldActive.add(h.getId());
                    ClientPlayNetworking.send(new GauntletHeldC2SPacket(h.getId(), true));
                } else if (!pressing && wasHeld) {
                    GauntletClientState.heldActive.remove(h.getId());
                    ClientPlayNetworking.send(new GauntletHeldC2SPacket(h.getId(), false));
                }
            }
        }
    }
}
