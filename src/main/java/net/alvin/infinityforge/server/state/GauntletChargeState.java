package net.alvin.infinityforge.server.state;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

public class GauntletChargeState {
    private static final Map<UUID, Map<Identifier, Integer>> CHARGES = new HashMap<>();
    private static final Set<UUID> PREVIOUSLY_EQUIPPED = new HashSet<>();

    public static boolean wasEquipped(PlayerEntity player) {
        return PREVIOUSLY_EQUIPPED.contains(player.getUuid());
    }

    public static void setEquipped(PlayerEntity player, boolean equipped) {
        if (equipped) PREVIOUSLY_EQUIPPED.add(player.getUuid());
        else          PREVIOUSLY_EQUIPPED.remove(player.getUuid());
    }

    public static int getCharge(PlayerEntity player, Identifier abilityId, int max) {
        return CHARGES.getOrDefault(player.getUuid(), Map.of())
                .getOrDefault(abilityId, max);
    }

    public static void setCharge(PlayerEntity player, Identifier abilityId, int charge) {
        CHARGES.computeIfAbsent(player.getUuid(), k -> new HashMap<>())
                .put(abilityId, charge);
    }

    // Call clear only on disconnect
    public static void clearAll(PlayerEntity player) {
        CHARGES.remove(player.getUuid());
        PREVIOUSLY_EQUIPPED.remove(player.getUuid());
    }
}