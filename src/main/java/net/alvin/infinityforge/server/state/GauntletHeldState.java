package net.alvin.infinityforge.server.state;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.*;

public class GauntletHeldState {
    private static final Map<UUID, Set<Identifier>> HELD_ACTIVE = new HashMap<>();

    public static boolean isHeld(PlayerEntity player, Identifier abilityId) {
        return HELD_ACTIVE.getOrDefault(player.getUuid(), Set.of())
                .contains(abilityId);
    }

    public static Set<Identifier> getHeld(PlayerEntity player) {
        return HELD_ACTIVE.getOrDefault(player.getUuid(), Set.of());
    }

    public static void setHeld(PlayerEntity player, Identifier abilityId, boolean held) {
        Set<Identifier> active = HELD_ACTIVE.computeIfAbsent(player.getUuid(), k -> new HashSet<>());
        if (held) active.add(abilityId);
        else active.remove(abilityId);
    }

    public static void clear(PlayerEntity player) {
        HELD_ACTIVE.remove(player.getUuid());
    }
}