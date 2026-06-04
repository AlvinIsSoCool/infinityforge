package net.alvin.infinityforge.server.state;

import net.alvin.infinityforge.accessor.PlayerEffectsAccess;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;

public class PlayerForcefieldState {
    private static final Map<ServerPlayerEntity, Long> FORCEFIELD_HITS = new IdentityHashMap<>();
    public static final int FORCEFIELD_HIT_DURATION = 8;

    public static void markHit(ServerPlayerEntity player) {
        ((PlayerEffectsAccess) player).setForcefieldHit(true);
        FORCEFIELD_HITS.put(player, player.getServerWorld().getTime());
    }

    public static boolean isHit(ServerPlayerEntity player) {
        Long tick = FORCEFIELD_HITS.get(player);
        if (tick == null) return false;
        long currentTime = player.getServerWorld().getTime();
        return (currentTime - tick) <= FORCEFIELD_HIT_DURATION;
    }

    public static void onTick(MinecraftServer server) {
        long currentTime = server.getOverworld().getTime();
        Iterator<Map.Entry<ServerPlayerEntity, Long>> iterator = FORCEFIELD_HITS.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<ServerPlayerEntity, Long> entry = iterator.next();
            ServerPlayerEntity player = entry.getKey();
            long hitTick = entry.getValue();
            if (currentTime - hitTick > FORCEFIELD_HIT_DURATION) {
                ((PlayerEffectsAccess) player).setForcefieldHit(false);
                iterator.remove();
            }
        }
    }
}