package net.alvin.infinityforge.client.tick;

import net.alvin.infinityforge.client.state.PlayerScaleAnimationState;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public class ScaleAnimationClientTick {
    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(PlayerScaleAnimationState::onClientTick);
    }
}
