package net.alvin.infinityforge.server.state;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GauntletCooldownState {
    // UUID -> abilityId -> game tick when cooldown expires
    private static final Map<UUID, Map<Identifier, Long>> COOLDOWNS = new HashMap<>();

    public static boolean isOnCooldown(PlayerEntity player, Identifier abilityId) {
        Map<Identifier, Long> map = COOLDOWNS.get(player.getUuid());
        if (map == null) return false;
        Long expiry = map.get(abilityId);
        if (expiry == null) return false;
        return player.getWorld().getTime() < expiry;
    }

    public static void setCooldown(PlayerEntity player, Identifier abilityId, int durationTicks) {
        COOLDOWNS.computeIfAbsent(player.getUuid(), k -> new HashMap<>())
                .put(abilityId, player.getWorld().getTime() + durationTicks);
    }

    public static long getExpiryTick(PlayerEntity player, Identifier abilityId) {
        Map<Identifier, Long> map = COOLDOWNS.get(player.getUuid());
        if (map == null) return 0;
        return map.getOrDefault(abilityId, 0L);
    }

    public static void clear(PlayerEntity player) {
        COOLDOWNS.remove(player.getUuid());
    }
}
