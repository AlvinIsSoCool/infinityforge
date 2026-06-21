package net.alvin.infinityforge.server.state;

import net.minecraft.util.Identifier;

import java.util.*;

public class GauntletChargeState {
    private static final Map<UUID, Map<Identifier, Integer>> CHARGES = new HashMap<>();

    public static int getCharge(UUID gauntletId, Identifier abilityId, int max) {
        Map<Identifier, Integer> map = CHARGES.get(gauntletId);
        if (map == null) return max;
        return map.getOrDefault(abilityId, max);
    }

    public static void setCharge(UUID gauntletId, Identifier abilityId, int charge) {
        CHARGES.computeIfAbsent(gauntletId, k -> new HashMap<>()).put(abilityId, charge);
    }

    public static Map<Identifier, Integer> getAll(UUID gauntletId) {
        return CHARGES.get(gauntletId);
    }

    public static void clear(UUID gauntletId) {
        CHARGES.remove(gauntletId);
    }
}