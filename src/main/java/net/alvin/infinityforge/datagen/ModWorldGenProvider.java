package net.alvin.infinityforge.datagen;

import com.mojang.datafixers.util.Pair;
import net.alvin.infinityforge.world.gen.ModStructures;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.SpreadType;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ModWorldGenProvider extends FabricDynamicRegistryProvider {
    public ModWorldGenProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup registries, Entries entries) {
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.CONFIGURED_FEATURE));
        entries.addAll(registries.getWrapperOrThrow(RegistryKeys.PLACED_FEATURE));

        RegistryEntryLookup<StructureProcessorList> processorLookup = registries.getWrapperOrThrow(RegistryKeys.PROCESSOR_LIST);
        RegistryEntry<StructureProcessorList> processorEntry = processorLookup.getOrThrow(ModStructures.CRYSTAL_HYPERCUBE_PROCESSOR_LIST);

        StructurePool myPool = getStructurePool(processorEntry, registries);
        entries.add(ModStructures.CRYSTAL_HYPERCUBE_STRUCTURE_POOL, myPool);

        RegistryEntryLookup<StructurePool> poolLookup = registries.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL);
        RegistryEntry<StructurePool> startPool = poolLookup.getOrThrow(ModStructures.CRYSTAL_HYPERCUBE_STRUCTURE_POOL);

        RegistryWrapper<Biome> biomeWrapper = registries.getWrapperOrThrow(RegistryKeys.BIOME);
        RegistryEntryList<Biome> overworldBiomes = RegistryEntryList.of(
                biomeWrapper.getOrThrow(BiomeKeys.MUSHROOM_FIELDS),
                biomeWrapper.getOrThrow(BiomeKeys.DEEP_FROZEN_OCEAN),
                biomeWrapper.getOrThrow(BiomeKeys.FROZEN_OCEAN),
                biomeWrapper.getOrThrow(BiomeKeys.DEEP_COLD_OCEAN),
                biomeWrapper.getOrThrow(BiomeKeys.COLD_OCEAN),
                biomeWrapper.getOrThrow(BiomeKeys.DEEP_OCEAN),
                biomeWrapper.getOrThrow(BiomeKeys.OCEAN),
                biomeWrapper.getOrThrow(BiomeKeys.DEEP_LUKEWARM_OCEAN),
                biomeWrapper.getOrThrow(BiomeKeys.LUKEWARM_OCEAN),
                biomeWrapper.getOrThrow(BiomeKeys.WARM_OCEAN),
                biomeWrapper.getOrThrow(BiomeKeys.STONY_SHORE),
                biomeWrapper.getOrThrow(BiomeKeys.SWAMP),
                biomeWrapper.getOrThrow(BiomeKeys.MANGROVE_SWAMP),
                biomeWrapper.getOrThrow(BiomeKeys.SNOWY_SLOPES),
                biomeWrapper.getOrThrow(BiomeKeys.SNOWY_PLAINS),
                biomeWrapper.getOrThrow(BiomeKeys.SNOWY_BEACH),
                biomeWrapper.getOrThrow(BiomeKeys.WINDSWEPT_GRAVELLY_HILLS),
                biomeWrapper.getOrThrow(BiomeKeys.GROVE),
                biomeWrapper.getOrThrow(BiomeKeys.WINDSWEPT_HILLS),
                biomeWrapper.getOrThrow(BiomeKeys.SNOWY_TAIGA),
                biomeWrapper.getOrThrow(BiomeKeys.WINDSWEPT_FOREST),
                biomeWrapper.getOrThrow(BiomeKeys.TAIGA),
                biomeWrapper.getOrThrow(BiomeKeys.PLAINS),
                biomeWrapper.getOrThrow(BiomeKeys.MEADOW),
                biomeWrapper.getOrThrow(BiomeKeys.BEACH),
                biomeWrapper.getOrThrow(BiomeKeys.FOREST),
                biomeWrapper.getOrThrow(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA),
                biomeWrapper.getOrThrow(BiomeKeys.FLOWER_FOREST),
                biomeWrapper.getOrThrow(BiomeKeys.BIRCH_FOREST),
                biomeWrapper.getOrThrow(BiomeKeys.DARK_FOREST),
                biomeWrapper.getOrThrow(BiomeKeys.SAVANNA_PLATEAU),
                biomeWrapper.getOrThrow(BiomeKeys.SAVANNA),
                biomeWrapper.getOrThrow(BiomeKeys.JUNGLE),
                biomeWrapper.getOrThrow(BiomeKeys.BADLANDS),
                biomeWrapper.getOrThrow(BiomeKeys.DESERT),
                biomeWrapper.getOrThrow(BiomeKeys.WOODED_BADLANDS),
                biomeWrapper.getOrThrow(BiomeKeys.JAGGED_PEAKS),
                biomeWrapper.getOrThrow(BiomeKeys.STONY_PEAKS),
                biomeWrapper.getOrThrow(BiomeKeys.FROZEN_RIVER),
                biomeWrapper.getOrThrow(BiomeKeys.RIVER),
                biomeWrapper.getOrThrow(BiomeKeys.ICE_SPIKES),
                biomeWrapper.getOrThrow(BiomeKeys.OLD_GROWTH_PINE_TAIGA),
                biomeWrapper.getOrThrow(BiomeKeys.SUNFLOWER_PLAINS),
                biomeWrapper.getOrThrow(BiomeKeys.OLD_GROWTH_BIRCH_FOREST),
                biomeWrapper.getOrThrow(BiomeKeys.SPARSE_JUNGLE),
                biomeWrapper.getOrThrow(BiomeKeys.BAMBOO_JUNGLE),
                biomeWrapper.getOrThrow(BiomeKeys.ERODED_BADLANDS),
                biomeWrapper.getOrThrow(BiomeKeys.WINDSWEPT_SAVANNA),
                biomeWrapper.getOrThrow(BiomeKeys.CHERRY_GROVE),
                biomeWrapper.getOrThrow(BiomeKeys.FROZEN_PEAKS),
                biomeWrapper.getOrThrow(BiomeKeys.DRIPSTONE_CAVES),
                biomeWrapper.getOrThrow(BiomeKeys.LUSH_CAVES),
                biomeWrapper.getOrThrow(BiomeKeys.DEEP_DARK)
        );
        RegistryEntryList<Biome> netherBiomes = RegistryEntryList.of(
                biomeWrapper.getOrThrow(BiomeKeys.NETHER_WASTES),
                biomeWrapper.getOrThrow(BiomeKeys.SOUL_SAND_VALLEY),
                biomeWrapper.getOrThrow(BiomeKeys.CRIMSON_FOREST),
                biomeWrapper.getOrThrow(BiomeKeys.WARPED_FOREST),
                biomeWrapper.getOrThrow(BiomeKeys.BASALT_DELTAS)
        );
        RegistryEntryList<Biome> endBiomes = RegistryEntryList.of(
                biomeWrapper.getOrThrow(BiomeKeys.END_HIGHLANDS),
                biomeWrapper.getOrThrow(BiomeKeys.END_MIDLANDS),
                biomeWrapper.getOrThrow(BiomeKeys.SMALL_END_ISLANDS),
                biomeWrapper.getOrThrow(BiomeKeys.END_BARRENS)
        );

        // TODO: Configure proper spacing and stuff.
        // Overworld
        JigsawStructure overworldStructure = new JigsawStructure(
                new Structure.Config(
                        overworldBiomes,
                        Collections.emptyMap(),
                        GenerationStep.Feature.SURFACE_STRUCTURES,
                        StructureTerrainAdaptation.BEARD_BOX
                ),
                startPool,
                1,
                UniformHeightProvider.create(YOffset.fixed(30), YOffset.fixed(120)),
                false,
                Heightmap.Type.WORLD_SURFACE_WG
        );
        RegistryEntry<Structure> overworldStructureEntry = entries.add(ModStructures.CRYSTAL_HYPERCUBE_OVERWORLD, overworldStructure);
        entries.add(ModStructures.CRYSTAL_HYPERCUBE_OVERWORLD_SET,
                new StructureSet(overworldStructureEntry,
                        new RandomSpreadStructurePlacement(10, 5, SpreadType.LINEAR, 620567084)));

        // Nether
        JigsawStructure netherStructure = new JigsawStructure(
                new Structure.Config(
                        netherBiomes,
                        Collections.emptyMap(),
                        GenerationStep.Feature.SURFACE_STRUCTURES,
                        StructureTerrainAdaptation.BEARD_BOX
                ),
                startPool,
                1,
                UniformHeightProvider.create(YOffset.fixed(-60), YOffset.fixed(-48)),
                false,
                Heightmap.Type.WORLD_SURFACE_WG
        );
        RegistryEntry<Structure> netherStructureEntry = entries.add(ModStructures.CRYSTAL_HYPERCUBE_NETHER, netherStructure);
        entries.add(ModStructures.CRYSTAL_HYPERCUBE_NETHER_SET,
                new StructureSet(netherStructureEntry,
                        new RandomSpreadStructurePlacement(10, 5, SpreadType.LINEAR, 368333975)));

        // End
        JigsawStructure endStructure = new JigsawStructure(
                new Structure.Config(
                        endBiomes,
                        Collections.emptyMap(),
                        GenerationStep.Feature.SURFACE_STRUCTURES,
                        StructureTerrainAdaptation.BEARD_BOX
                ),
                startPool,
                1,
                UniformHeightProvider.create(YOffset.fixed(0), YOffset.fixed(50)),
                false,
                Heightmap.Type.WORLD_SURFACE_WG
        );
        RegistryEntry<Structure> endStructureEntry = entries.add(ModStructures.CRYSTAL_HYPERCUBE_END, endStructure);
        entries.add(ModStructures.CRYSTAL_HYPERCUBE_END_SET,
                new StructureSet(endStructureEntry,
                        new RandomSpreadStructurePlacement(10, 5, SpreadType.LINEAR, 109817750)));
    }

    private static StructurePool getStructurePool(RegistryEntry<StructureProcessorList> processorEntry,
                                                  RegistryWrapper.WrapperLookup registries) {
        Function<StructurePool.Projection, ? extends StructurePoolElement> elementFunction =
                StructurePoolElement.ofProcessedLegacySingle("infinityforge:crystal_hypercube", processorEntry);

        RegistryKey<StructurePool> emptyPoolKey = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, new Identifier("empty"));
        RegistryEntry<StructurePool> fallback = registries.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL)
                .getOrThrow(emptyPoolKey);

        List<Pair<Function<StructurePool.Projection, ? extends StructurePoolElement>, Integer>> poolElements =
                List.of(new Pair<>(elementFunction, 1));

        return new StructurePool(fallback, poolElements, StructurePool.Projection.RIGID);
    }

    @Override
    public String getName() { return "World Gen"; }
}
