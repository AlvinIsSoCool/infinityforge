package net.alvin.infinityforge.server.state;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class StatefulAbilityState {
    private static final Map<PlayerEntity, Map<Identifier, Object>> STATE
            = new IdentityHashMap<>();

    public static <T> T get(PlayerEntity player, Identifier abilityId, Class<T> type) {
        Map<Identifier, Object> map = STATE.get(player);
        if (map == null) return null;
        Object value = map.get(abilityId);
        if (value == null) return null;
        return type.cast(value);
    }

    public static void set(PlayerEntity player, Identifier abilityId, Object state) {
        if (state == null) {
            clear(player, abilityId);
            return;
        }
        STATE.computeIfAbsent(player, k -> new HashMap<>())
                .put(abilityId, state);
    }

    public static void clear(PlayerEntity player, Identifier abilityId) {
        Map<Identifier, Object> map = STATE.get(player);
        if (map != null) map.remove(abilityId);
    }

    public static void clear(PlayerEntity player) {
        STATE.remove(player);
    }
}
