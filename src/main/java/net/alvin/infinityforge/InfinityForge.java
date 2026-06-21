package net.alvin.infinityforge;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.alvin.infinityforge.block.ModBlocks;
import net.alvin.infinityforge.block.entity.ModBlockEntities;
import net.alvin.infinityforge.config.InfinityForgeConfig;
import net.alvin.infinityforge.effect.ModStatusEffects;
import net.alvin.infinityforge.entity.ModEntities;
import net.alvin.infinityforge.infinity.ModStones;
import net.alvin.infinityforge.item.ModItemGroups;
import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.registry.*;
import net.alvin.infinityforge.screen.ModScreenHandlers;
import net.alvin.infinityforge.server.event.GauntletConnectionEvents;
import net.alvin.infinityforge.server.event.InfinityStoneEventHandler;
import net.alvin.infinityforge.server.packet.GauntletPacketHandlers;
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
		AutoConfig.register(InfinityForgeConfig.class, JanksonConfigSerializer::new);

		ModStones.initialize();
		ModItems.initialize();
		ModBlocks.initialize();
		ModBlockEntities.initialize();
		ModEntities.initialise();
		ModStatusEffects.initialize();
		ModItemGroups.register();

		InfinityStoneTypeRegistry.initialize();
		GauntletAbilityRegistry.initialize();
		ModScreenHandlers.initialize();

		ModWorldGeneration.initialize();
		ModStructures.initialize();
		ModCustomTrades.register();
		ModLootTableModifiers.register();

		ModCommands.register();
		WorldGenEventHandler.register();
		GauntletPacketHandlers.register();
		GauntletServerTick.register();
		PlayerForcefieldServerTick.register();
		GauntletConnectionEvents.register();
		InfinityStoneEventHandler.register();
	}
}