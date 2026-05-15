package net.alvin.infinityforge.server.state;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.*;

public class GauntletChargeState {
    private static final Map<UUID, Map<Identifier, Integer>> CHARGES = new HashMap<>();
    private static final Set<PlayerEntity> PREVIOUSLY_EQUIPPED
            = Collections.newSetFromMap(new IdentityHashMap<>());
    private static final Map<PlayerEntity, ItemStack> LAST_KNOWN_STACK
            = new IdentityHashMap<>();

    public static boolean wasEquipped(PlayerEntity player) {
        return PREVIOUSLY_EQUIPPED.contains(player);
    }

    public static void setEquipped(PlayerEntity player, boolean equipped, ItemStack stack) {
        if (equipped) {
            PREVIOUSLY_EQUIPPED.add(player);
            LAST_KNOWN_STACK.put(player, stack);
        } else {
            PREVIOUSLY_EQUIPPED.remove(player);
            LAST_KNOWN_STACK.remove(player);
        }
    }

    public static ItemStack getLastKnownStack(PlayerEntity player) {
        return LAST_KNOWN_STACK.get(player);
    }

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

    public static void clearPlayer(PlayerEntity player) {
        PREVIOUSLY_EQUIPPED.remove(player);
        LAST_KNOWN_STACK.remove(player);
    }
}