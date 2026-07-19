package net.alvin.infinityforge;

import net.alvin.infinityforge.datagen.*;
import net.alvin.infinityforge.registry.ModDamageTypes;
import net.alvin.infinityforge.world.ModConfiguredFeatures;
import net.alvin.infinityforge.world.ModPlacedFeatures;
import net.alvin.infinityforge.world.gen.ModStructures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class InfinityForgeDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModLanguageProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModBlockTagProvider::new);
		pack.addProvider(ModLootTableProvider::new);
		pack.addProvider(ModWorldGenProvider::new);
		pack.addProvider(ModDamageTypeProvider::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.PROCESSOR_LIST, ModStructures::bootstrapProcessors);
		registryBuilder.addRegistry(RegistryKeys.TEMPLATE_POOL, ModStructures::bootstrapPools);
		registryBuilder.addRegistry(RegistryKeys.STRUCTURE, ModStructures::bootstrapStructure);
		registryBuilder.addRegistry(RegistryKeys.STRUCTURE_SET, ModStructures::bootstrapStructureSet);
		registryBuilder.addRegistry(RegistryKeys.DAMAGE_TYPE, ModDamageTypes::bootstrap);
	}
}
