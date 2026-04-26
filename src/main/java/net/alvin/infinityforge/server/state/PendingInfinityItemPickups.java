package net.alvin.infinityforge.server.state;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PendingInfinityItemPickups {
    // UUID -> game tick when pickup packet was received
    private static final Map<UUID, Long> PENDING = new HashMap<>();

    public static void markPending(ServerPlayerEntity player) {
        PENDING.put(player.getUuid(), player.getServerWorld().getTime());
    }

    public static boolean isPending(ServerPlayerEntity player) {
        Long tick = PENDING.get(player.getUuid());
        if (tick == null) return false;

        // Clear after 5 ticks to avoid permanent suppression.
        // (Handled anyway in ServerPlayConnectionEvents.DISCONNECT event).
        if (player.getServerWorld().getTime() - tick > 5) {
            PENDING.remove(player.getUuid());
            return false;
        }
        return true;
    }

    public static void clear(ServerPlayerEntity player) {
        PENDING.remove(player.getUuid());
    }
}