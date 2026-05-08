package net.alvin.infinityforge.server.state;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.IdentityHashMap;
import java.util.Map;

public class PendingInfinityItemPickups {
    private static final Map<ServerPlayerEntity, Long> PENDING = new IdentityHashMap<>();

    public static void markPending(ServerPlayerEntity player) {
        PENDING.put(player, player.getServerWorld().getTime());
    }

    public static boolean isPending(ServerPlayerEntity player) {
        Long tick = PENDING.get(player);
        if (tick == null) return false;
        if (player.getServerWorld().getTime() - tick > 5) {
            PENDING.remove(player);
            return false;
        }
        return true;
    }

    public static void clear(ServerPlayerEntity player) {
        PENDING.remove(player);
    }
}