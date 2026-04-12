package net.alvin.infinityforge;

import net.alvin.infinityforge.events.InfinityForgeEvents;
import net.fabricmc.api.ClientModInitializer;

public class InfinityForgeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        InfinityForgeEvents.registerEventsClient();
    }
}
