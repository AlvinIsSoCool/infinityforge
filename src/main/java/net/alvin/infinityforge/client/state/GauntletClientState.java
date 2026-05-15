package net.alvin.infinityforge.client.state;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
}