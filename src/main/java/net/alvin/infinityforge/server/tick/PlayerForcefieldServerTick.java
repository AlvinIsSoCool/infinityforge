package net.alvin.infinityforge.server.tick;

import net.alvin.infinityforge.server.state.PlayerForcefieldState;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

public class PlayerForcefieldServerTick {
    public static void register() { ServerTickEvents.END_SERVER_TICK.register(PlayerForcefieldState::onTick); }
}
