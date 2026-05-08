package net.alvin.infinityforge.server.state;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

public class GauntletToggleState {
    private static final Map<PlayerEntity, Set<Identifier>> ACTIVE_TOGGLES
            = new IdentityHashMap<>();

    public static boolean isActive(PlayerEntity player, Identifier abilityId) {
        Set<Identifier> set = ACTIVE_TOGGLES.get(player);
        return set != null && set.contains(abilityId);
    }

    public static Set<Identifier> getActive(PlayerEntity player) {
        Set<Identifier> set = ACTIVE_TOGGLES.get(player);
        return set != null ? set : Collections.emptySet();
    }

    public static boolean flip(PlayerEntity player, Identifier abilityId) {
        Set<Identifier> set = ACTIVE_TOGGLES.computeIfAbsent(player, k -> new HashSet<>());
        if (set.contains(abilityId)) {
            set.remove(abilityId);
            return false;
        } else {
            set.add(abilityId);
            return true;
        }
    }

    public static void setActive(PlayerEntity player, Identifier abilityId, boolean state) {
        if (state) {
            ACTIVE_TOGGLES.computeIfAbsent(player, k -> new HashSet<>()).add(abilityId);
        } else {
            Set<Identifier> set = ACTIVE_TOGGLES.get(player);
            if (set != null) set.remove(abilityId);
        }
    }

    public static void clear(PlayerEntity player) {
        ACTIVE_TOGGLES.remove(player);
    }
}
