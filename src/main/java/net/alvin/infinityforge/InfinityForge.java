package net.alvin.infinityforge;

import net.alvin.infinityforge.block.ModBlocks;
import net.alvin.infinityforge.block.entity.ModBlockEntities;
import net.alvin.infinityforge.config.client.InfinityForgeClientConfig;
import net.alvin.infinityforge.config.server.InfinityForgeServerConfig;
import net.alvin.infinityforge.entity.effect.ModStatusEffects;
import net.alvin.infinityforge.entity.ModEntities;
import net.alvin.infinityforge.infinity.ModStones;
import net.alvin.infinityforge.item.ModItemGroups;
import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.particle.ModParticleEffects;
import net.alvin.infinityforge.registry.*;
import net.alvin.infinityforge.screen.ModScreenHandlers;
import net.alvin.infinityforge.server.event.GauntletConnectionEvents;
import net.alvin.infinityforge.server.event.InfinityStoneEventHandler;
import net.alvin.infinityforge.server.packet.GauntletPacketHandler;
import net.alvin.infinityforge.server.tick.GauntletServerTick;
import net.alvin.infinityforge.server.tick.PlayerForcefieldServerTick;
import net.alvin.infinityforge.world.event.WorldGenEventHandler;
import net.alvin.infinityforge.world.gen.ModStructures;
import net.alvin.infinityforge.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinityForge implements ModInitializer {
	public static final String MOD_ID = "infinityforge";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		InfinityForgeServerConfig.init();
		InfinityForgeClientConfig.HANDLER.load();

		ModStones.initialize();
		ModItems.initialize();
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModEntities.initialise();
		ModStatusEffects.initialize();
		ModSounds.initialize();
		ModItemGroups.register();

		InfinityStoneTypeRegistry.initialize();
		GauntletAbilityRegistry.initialize();
		ModScreenHandlers.initialize();

		ModWorldGeneration.initialize();
		ModStructures.initialize();
		ModCustomTrades.register();
		ModLootTableModifiers.register();

		ModParticleEffects.register();
		ModCommands.register();
		WorldGenEventHandler.register();
		GauntletPacketHandler.register();
		GauntletServerTick.register();
		PlayerForcefieldServerTick.register();
		GauntletConnectionEvents.register();
		InfinityStoneEventHandler.register();
	}
}