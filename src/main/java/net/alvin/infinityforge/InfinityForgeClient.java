package net.alvin.infinityforge;

import net.alvin.infinityforge.client.event.GauntletHudEvents;
import net.alvin.infinityforge.client.input.GauntletKeybinds;
import net.alvin.infinityforge.client.packet.GauntletClientPacketHandlers;
import net.alvin.infinityforge.client.screen.ModScreens;
import net.alvin.infinityforge.client.tick.GauntletClientTick;
import net.alvin.infinityforge.client.render.ModItemRenderers;
import net.fabricmc.api.ClientModInitializer;

public class InfinityForgeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModItemRenderers.initialize();
        ModScreens.initialize();
        GauntletClientPacketHandlers.initialize();
        GauntletClientTick.initialize();
        GauntletHudEvents.initialize();
        GauntletKeybinds.initialize();
    }
}
