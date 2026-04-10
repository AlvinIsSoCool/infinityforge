package net.alvin.infinityforge;

import net.alvin.infinityforge.infinity.InfinityStones;
import net.alvin.infinityforge.item.ModItemGroups;
import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.registries.InfinityStoneTypeRegistry;
import net.fabricmc.api.ModInitializer;

import net.minecraft.client.render.item.ItemRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinityForge implements ModInitializer {
	public static final String MOD_ID = "infinityforge";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		InfinityStoneTypeRegistry.initialize();
		InfinityStones.initialize();

		ModItemGroups.registerItemGroups();
		ModItems.registerModItems();
	}
}