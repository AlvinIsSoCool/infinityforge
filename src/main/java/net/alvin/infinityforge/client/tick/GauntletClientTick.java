package net.alvin.infinityforge.client.tick;

import net.alvin.infinityforge.client.state.GauntletClientState;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class GauntletClientTick {
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(GauntletClientState::onClientTick);
    }
}
