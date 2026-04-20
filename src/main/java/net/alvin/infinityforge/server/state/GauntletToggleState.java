package net.alvin.infinityforge.server.state;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

public class GauntletToggleState {
    private static final Map<UUID, Set<Identifier>> ACTIVE_TOGGLES = new HashMap<>();

    public static boolean isActive(PlayerEntity player, Identifier abilityId) {
        return ACTIVE_TOGGLES
                .getOrDefault(player.getUuid(), Set.of())
                .contains(abilityId);
    }

    public static Set<Identifier> getActive(PlayerEntity player) {
        return ACTIVE_TOGGLES.getOrDefault(player.getUuid(), Set.of());
    }

    // Used for player-initiated toggle only
    public static boolean flip(PlayerEntity player, Identifier abilityId) {
        Set<Identifier> active = ACTIVE_TOGGLES.computeIfAbsent(player.getUuid(), k -> new HashSet<>());
        if (active.contains(abilityId)) {
            active.remove(abilityId);
            return false;
        } else {
            active.add(abilityId);
            return true;
        }
    }

    // Used for force disable — explicit, never accidentally flips back on
    public static void setActive(PlayerEntity player, Identifier abilityId, boolean active) {
        Set<Identifier> set = ACTIVE_TOGGLES.computeIfAbsent(player.getUuid(), k -> new HashSet<>());
        if (active) set.add(abilityId);
        else set.remove(abilityId);
    }

    public static void clear(PlayerEntity player) {
        ACTIVE_TOGGLES.remove(player.getUuid());
    }
}
