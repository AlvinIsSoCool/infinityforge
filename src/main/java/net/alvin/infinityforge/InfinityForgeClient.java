package net.alvin.infinityforge;

import net.alvin.infinityforge.client.event.GauntletHudEvents;
import net.alvin.infinityforge.client.input.GauntletKeybinds;
import net.alvin.infinityforge.client.packet.GauntletClientPacketHandlers;
import net.alvin.infinityforge.client.screen.ModScreens;
import net.alvin.infinityforge.client.tick.GauntletClientTick;
import net.alvin.infinityforge.client.render.ModItemRenderers;
import net.alvin.infinityforge.client.render.ModRenderLayers;
import net.fabricmc.api.ClientModInitializer;

public class InfinityForgeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModRenderLayers.initialize();
        ModItemRenderers.initialize();
        ModScreens.initialize();
        GauntletClientPacketHandlers.register();
        GauntletClientTick.register();
        GauntletHudEvents.register();
        GauntletKeybinds.register();
    }
}
