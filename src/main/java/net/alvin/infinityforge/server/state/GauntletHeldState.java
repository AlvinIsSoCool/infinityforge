package net.alvin.infinityforge.server.state;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

public class GauntletHeldState {
    private static final Map<PlayerEntity, Set<Identifier>> HELD_ACTIVE
            = new IdentityHashMap<>();

    public static boolean isHeld(PlayerEntity player, Identifier abilityId) {
        Set<Identifier> set = HELD_ACTIVE.get(player);
        return set != null && set.contains(abilityId);
    }

    public static Set<Identifier> getHeld(PlayerEntity player) {
        Set<Identifier> set = HELD_ACTIVE.get(player);
        return set != null ? set : Collections.emptySet();
    }

    public static void setHeld(PlayerEntity player, Identifier abilityId, boolean held) {
        if (held) {
            HELD_ACTIVE.computeIfAbsent(player, k -> new HashSet<>()).add(abilityId);
        } else {
            Set<Identifier> set = HELD_ACTIVE.get(player);
            if (set != null) set.remove(abilityId);
        }
    }

    public static void clear(PlayerEntity player) {
        HELD_ACTIVE.remove(player);
    }
}