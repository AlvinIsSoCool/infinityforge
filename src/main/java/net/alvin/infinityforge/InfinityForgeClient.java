package net.alvin.infinityforge;

import net.alvin.infinityforge.client.event.GauntletClientConnectionEvents;
import net.alvin.infinityforge.client.event.GauntletHudEvents;
import net.alvin.infinityforge.client.input.GauntletKeybinds;
import net.alvin.infinityforge.client.packet.GauntletClientPacketHandlers;
import net.alvin.infinityforge.client.render.be.ModBERenderers;
import net.alvin.infinityforge.client.screen.ModScreens;
import net.alvin.infinityforge.client.tick.GauntletClientTick;
import net.alvin.infinityforge.client.render.item.ModItemRenderers;
import net.alvin.infinityforge.client.render.ModRenderLayers;
import net.alvin.infinityforge.client.tick.ScaleAnimationClientTick;
import net.fabricmc.api.ClientModInitializer;

public class InfinityForgeClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModRenderLayers.register();
        ModItemRenderers.register();
        ModBERenderers.register();

        ModScreens.register();

        GauntletClientConnectionEvents.register();
        GauntletClientPacketHandlers.register();
        GauntletClientTick.register();
        ScaleAnimationClientTick.register();
        GauntletHudEvents.register();
        GauntletKeybinds.register();
    }
}
