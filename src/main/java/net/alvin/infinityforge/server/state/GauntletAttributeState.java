package net.alvin.infinityforge.server.state;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

public class GauntletAttributeState {
    private static final Map<PlayerEntity, Set<Identifier>> ACTIVE_MODIFIERS
            = new IdentityHashMap<>();

    public static void markActive(PlayerEntity player, Identifier abilityId) {
        ACTIVE_MODIFIERS.computeIfAbsent(player, k -> new HashSet<>()).add(abilityId);
    }

    public static void markInactive(PlayerEntity player, Identifier abilityId) {
        Set<Identifier> set = ACTIVE_MODIFIERS.get(player);
        if (set != null) set.remove(abilityId);
    }

    public static boolean isActive(PlayerEntity player, Identifier abilityId) {
        Set<Identifier> set = ACTIVE_MODIFIERS.get(player);
        return set != null && set.contains(abilityId);
    }

    public static Set<Identifier> getActive(PlayerEntity player) {
        Set<Identifier> set = ACTIVE_MODIFIERS.get(player);
        return set != null ? Collections.unmodifiableSet(set) : Collections.emptySet();
    }

    public static void clear(PlayerEntity player) {
        ACTIVE_MODIFIERS.remove(player);
    }
}
