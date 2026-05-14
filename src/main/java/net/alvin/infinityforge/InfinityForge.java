package net.alvin.infinityforge;

import net.alvin.infinityforge.block.ModBlocks;
import net.alvin.infinityforge.registry.*;
import net.alvin.infinityforge.server.event.GauntletConnectionEvents;
import net.alvin.infinityforge.server.event.InfinityStoneEventHandler;
import net.alvin.infinityforge.server.packet.GauntletPacketHandlers;
import net.alvin.infinityforge.server.tick.GauntletServerTick;
import net.alvin.infinityforge.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinityForge implements ModInitializer {
	public static final String MOD_ID = "infinityforge";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		InfinityStoneTypeRegistry.initialize();
		GauntletAbilityRegistry.initialize();
		ModScreenHandlers.initialize();

		ModWorldGeneration.initialize();
		ModCustomTrades.register();
		ModLootTableModifiers.modify();

		GauntletPacketHandlers.register();
		GauntletServerTick.register();
		GauntletConnectionEvents.register();
		InfinityStoneEventHandler.register();

		ModStones.initialize();
		ModItems.initialize();
		ModBlocks.initialize();
		ModItemGroups.initialize();
	}
}