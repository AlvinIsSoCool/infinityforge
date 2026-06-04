package net.alvin.infinityforge.client.state;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.abilities.base.GauntletAbility;
import net.alvin.infinityforge.infinity.abilities.base.HeldAbility;
import net.alvin.infinityforge.infinity.abilities.base.ToggleAbility;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.network.c2s.GauntletAbilityC2SPacket;
import net.alvin.infinityforge.network.c2s.GauntletHeldC2SPacket;
import net.alvin.infinityforge.network.c2s.GauntletToggleC2SPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.*;

import static net.alvin.infinityforge.client.input.GauntletKeybinds.SLOT_KEYS;

public class GauntletClientState {
    public static int scrollOffset = 0;
    public static final Set<Identifier> ACTIVE_TOGGLES = new HashSet<>();
    public static final Set<Identifier> HELD_ACTIVE = new HashSet<>();
    public static final Set<Identifier> HELD_LOCKED_OUT = new HashSet<>();
    public static final Map<Identifier, long[]> COOLDOWNS = new HashMap<>();
    public static final Map<Identifier, int[]> CHARGES = new HashMap<>();

    public static float getChargeProgress(Identifier abilityId) {
        int[] data = CHARGES.get(abilityId);
        if (data == null) return 1f;
        return (float) data[0] / data[1];
    }

    public static float getCooldownProgress(Identifier abilityId, long currentTick) {
        long[] data = COOLDOWNS.get(abilityId);
        if (data == null) return 1f;
        long elapsed = currentTick - data[0];
        if (elapsed >= data[1]) {
            COOLDOWNS.remove(abilityId);
            return 1f;
        }
        return (float) elapsed / data[1];
    }

    public static void scroll(int totalAbilities, int delta) {
        int max = Math.max(0, totalAbilities - 6);
        scrollOffset = Math.min(Math.max(scrollOffset + delta, 0), max);
    }

    public static void clearAll() {
        COOLDOWNS.clear();
        CHARGES.clear();
        ACTIVE_TOGGLES.clear();
        HELD_ACTIVE.clear();
        HELD_LOCKED_OUT.clear();
        scrollOffset = 0;
    }

    public static void onClientTick(MinecraftClient client) {
        if (client.player == null) return;

        ItemStack gauntletStack = InfinityGauntletItem.findGauntlet(client.player);
        List<InfinityStoneType> activeStones = gauntletStack != null
                ? InfinityGauntletItem.getAddedStones(gauntletStack)
                : List.of();
        List<GauntletAbility> abilities = InfinityGauntletItem.getVisibleAbilities(activeStones);

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
                boolean wasHeld = GauntletClientState.HELD_ACTIVE.contains(h.getId());
                boolean lockedOut = GauntletClientState.HELD_LOCKED_OUT.contains(h.getId());

                // Clear lockout once key is physically released
                if (!pressing && lockedOut)
                    GauntletClientState.HELD_LOCKED_OUT.remove(h.getId());

                if (lockedOut) continue;

                if (pressing && !wasHeld) {
                    GauntletClientState.HELD_ACTIVE.add(h.getId());
                    ClientPlayNetworking.send(new GauntletHeldC2SPacket(h.getId(), true));
                } else if (!pressing && wasHeld) {
                    GauntletClientState.HELD_ACTIVE.remove(h.getId());
                    ClientPlayNetworking.send(new GauntletHeldC2SPacket(h.getId(), false));
                }
            }
        }
    }
}