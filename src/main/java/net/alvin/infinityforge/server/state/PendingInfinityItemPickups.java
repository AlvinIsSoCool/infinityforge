package net.alvin.infinityforge.server.state;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.IdentityHashMap;
import java.util.Map;

public class PendingInfinityItemPickups {
    private static final Map<ServerPlayerEntity, Long> PENDING_PICKUPS = new IdentityHashMap<>();

    public static void markPending(ServerPlayerEntity player) {
        PENDING_PICKUPS.put(player, player.getServerWorld().getTime());
    }

    public static boolean isPending(ServerPlayerEntity player) {
        Long tick = PENDING_PICKUPS.get(player);
        if (tick == null) return false;
        if (player.getServerWorld().getTime() - tick > 5) {
            PENDING_PICKUPS.remove(player);
            return false;
        }
        return true;
    }

    public static void clear(ServerPlayerEntity player) {
        PENDING_PICKUPS.remove(player);
    }
}