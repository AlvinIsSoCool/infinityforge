package net.alvin.infinityforge.server.state;

import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GauntletCooldownState {
    private static final Map<UUID, Map<Identifier, Long>> COOLDOWNS = new HashMap<>();

    public static boolean isOnCooldown(UUID gauntletId, Identifier abilityId, long currentTick) {
        Map<Identifier, Long> map = COOLDOWNS.get(gauntletId);
        if (map == null) return false;
        Long expiry = map.get(abilityId);
        if (expiry == null) return false;
        return currentTick < expiry;
    }

    public static void setCooldown(UUID gauntletId, Identifier abilityId, int durationTicks, long currentTick) {
        COOLDOWNS.computeIfAbsent(gauntletId, k -> new HashMap<>())
                .put(abilityId, currentTick + durationTicks);
    }

    public static void setRawExpiry(UUID gauntletId, Identifier abilityId, long expiry) {
        COOLDOWNS.computeIfAbsent(gauntletId, k -> new HashMap<>()).put(abilityId, expiry);
    }

    public static long getExpiryTick(UUID gauntletId, Identifier abilityId) {
        Map<Identifier, Long> map = COOLDOWNS.get(gauntletId);
        if (map == null) return 0L;
        return map.getOrDefault(abilityId, 0L);
    }

    public static Map<Identifier, Long> getAll(UUID gauntletId) {
        return COOLDOWNS.get(gauntletId);
    }

    public static void clear(UUID gauntletId) {
        COOLDOWNS.remove(gauntletId);
    }
}