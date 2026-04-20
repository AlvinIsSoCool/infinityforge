package net.alvin.infinityforge.client.state;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GauntletClientState {
    public static int scrollOffset = 0;
    public static final Set<Identifier> activeToggles = new HashSet<>();
    public static final Set<Identifier> heldActive = new HashSet<>();
    public static final Set<Identifier> heldLockedOut = new HashSet<>();
    public static final Map<Identifier, long[]> cooldowns = new HashMap<>();
    public static final Map<Identifier, int[]> charges = new HashMap<>();

    public static float getChargeProgress(Identifier abilityId) {
        int[] data = charges.get(abilityId);
        if (data == null) return 1f;
        return (float) data[0] / data[1];
    }

    public static float getCooldownProgress(Identifier abilityId, long currentTick) {
        long[] data = cooldowns.get(abilityId);
        if (data == null) return 1f;
        long elapsed = currentTick - data[0];
        if (elapsed >= data[1]) {
            cooldowns.remove(abilityId);
            return 1f;
        }
        return (float) elapsed / data[1];
    }

    public static void scroll(int totalAbilities, int delta) {
        int max = Math.max(0, totalAbilities - 6);
        scrollOffset = Math.min(Math.max(scrollOffset + delta, 0), max);
    }
}