package net.alvin.infinityforge.abilities.state;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

public class GauntletToggleState {
    private static final Map<UUID, Set<Identifier>> ACTIVE_TOGGLES = new HashMap<>();

    public static boolean isActive(PlayerEntity player, Identifier abilityId) {
        return ACTIVE_TOGGLES.getOrDefault(player.getUuid(), Set.of())
                .contains(abilityId);
    }

    public static boolean flip(PlayerEntity player, Identifier abilityId) {
        Set<Identifier> active = ACTIVE_TOGGLES.computeIfAbsent(player.getUuid(), k -> new HashSet<>());
        if (active.contains(abilityId)) {
            active.remove(abilityId);
            return false; // now off
        } else {
            active.add(abilityId);
            return true;  // now on
        }
    }

    public static void clear(PlayerEntity player) {
        ACTIVE_TOGGLES.remove(player.getUuid());
    }
}
