package net.alvin.infinityforge.server.state;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

public class GauntletChargeState {
    private static final Map<PlayerEntity, Map<Identifier, Integer>> CHARGES
            = new IdentityHashMap<>();

    private static final Set<PlayerEntity> PREVIOUSLY_EQUIPPED
            = Collections.newSetFromMap(new IdentityHashMap<>());

    public static boolean wasEquipped(PlayerEntity player) {
        return PREVIOUSLY_EQUIPPED.contains(player); // identity check, no hashing
    }

    public static void setEquipped(PlayerEntity player, boolean equipped) {
        if (equipped) PREVIOUSLY_EQUIPPED.add(player);
        else          PREVIOUSLY_EQUIPPED.remove(player);
    }

    public static int getCharge(PlayerEntity player, Identifier abilityId, int max) {
        Map<Identifier, Integer> inner = CHARGES.get(player); // identity lookup
        if (inner == null) return max;                         // avoids Map.of() allocation
        return inner.getOrDefault(abilityId, max);
    }

    public static void setCharge(PlayerEntity player, Identifier abilityId, int charge) {
        CHARGES.computeIfAbsent(player, k -> new HashMap<>())
                .put(abilityId, charge);
    }

    public static void clearAll(PlayerEntity player) {
        CHARGES.remove(player);
        PREVIOUSLY_EQUIPPED.remove(player);
    }
}