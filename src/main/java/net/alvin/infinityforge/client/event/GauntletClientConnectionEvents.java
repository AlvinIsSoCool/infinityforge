package net.alvin.infinityforge.client.event;

import net.alvin.infinityforge.client.state.GauntletClientState;
import net.alvin.infinityforge.client.state.AbilityDynamicIconState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class GauntletClientConnectionEvents {
    public static void register() {
        ClientPlayConnectionEvents.DISCONNECT.register(
                (handler, client) -> clearAll());
    }

    public static void clearAll() {
        GauntletClientState.clearAll();
        AbilityDynamicIconState.clear();
    }
}
