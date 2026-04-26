package net.alvin.infinityforge.server.state;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StatefulAbilityState {
    private static final Map<UUID, Map<Identifier, Object>> STATE = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T get(PlayerEntity player, Identifier abilityId) {
        Map<Identifier, Object> map = STATE.get(player.getUuid());
        if (map == null) return null;
        return (T) map.get(abilityId);
    }

    public static void set(PlayerEntity player, Identifier abilityId, Object state) {
        STATE.computeIfAbsent(player.getUuid(), k -> new HashMap<>())
                .put(abilityId, state);
    }

    public static void clear(PlayerEntity player) {
        STATE.remove(player.getUuid());
    }
}
