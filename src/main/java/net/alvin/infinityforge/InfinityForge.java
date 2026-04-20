package net.alvin.infinityforge;

import net.alvin.infinityforge.registry.*;
import net.alvin.infinityforge.infinity.InfinityStoneTypeRegistry;
import net.alvin.infinityforge.server.event.GauntletConnectionEvents;
import net.alvin.infinityforge.server.packet.GauntletPacketHandlers;
import net.alvin.infinityforge.server.tick.GauntletServerTick;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinityForge implements ModInitializer {
	public static final String MOD_ID = "infinityforge";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.initialize();
		ModItemGroups.initialize();
		//ModBlocks.initialize();
		ModScreenHandlers.initialize();
		ModStones.initialize();
		InfinityStoneTypeRegistry.initialize();
		GauntletPacketHandlers.initialize();
		GauntletServerTick.initialize();
		GauntletConnectionEvents.initialize();
	}
}