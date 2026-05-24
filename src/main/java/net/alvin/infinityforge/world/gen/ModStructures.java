package net.alvin.infinityforge.world.gen;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.world.gen.processor.InfinityStoneTempleProcessor;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.SpreadType;
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider;
import net.minecraft.world.gen.heightprovider.HeightProvider;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;

import java.util.Collections;
import java.util.List;

public class ModStructures {
    public static final StructureProcessorType<InfinityStoneTempleProcessor> STONE_TEMPLE_PROCESSOR =
            StructureProcessorType.register("infinityforge:infinity_stone_temple_processor", InfinityStoneTempleProcessor.CODEC);
    public static final RegistryKey<Structure> STONE_TEMPLE_STRUCTURE = RegistryKey.of(RegistryKeys.STRUCTURE, new Identifier(InfinityForge.MOD_ID, "stone_temple"));
    public static final RegistryKey<StructureSet> STONE_TEMPLE_STRUCTURE_SET = RegistryKey.of(RegistryKeys.STRUCTURE_SET, new Identifier(InfinityForge.MOD_ID, "stone_temple_structure_set"));
    public static final RegistryKey<StructurePool> STONE_TEMPLE_STRUCTURE_POOL = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, new Identifier(InfinityForge.MOD_ID, "stone_temple_structure_pool"));
    public static final RegistryKey<StructureProcessorList> STONE_TEMPLE_PROCESSOR_LIST = RegistryKey.of(RegistryKeys.PROCESSOR_LIST, new Identifier(InfinityForge.MOD_ID, "stone_temple_processor_list"));

    public static StructureProcessorList createProcessorList() {
        return new StructureProcessorList(List.of(new InfinityStoneTempleProcessor()));
    }

    public static JigsawStructure createStructure(RegistryEntryList<Biome> biomes, RegistryEntry<StructurePool> startPool) {
        Structure.Config config = new Structure.Config(
                biomes,
                Collections.emptyMap(),
                GenerationStep.Feature.SURFACE_STRUCTURES,
                StructureTerrainAdaptation.BEARD_THIN
        );

        HeightProvider startHeight = ConstantHeightProvider.create(YOffset.fixed(0));
        return new JigsawStructure(config, startPool, 7, startHeight, false, Heightmap.Type.WORLD_SURFACE_WG);
    }

    public static void bootstrapProcessors(Registerable<StructureProcessorList> context) {
        context.register(STONE_TEMPLE_PROCESSOR_LIST, createProcessorList());
    }

    public static void bootstrapPools(Registerable<StructurePool> context) {
        RegistryKey<StructurePool> emptyPoolKey =
                RegistryKey.of(RegistryKeys.TEMPLATE_POOL, new Identifier("empty"));
        RegistryEntry<StructurePool> fallback =
                context.getRegistryLookup(RegistryKeys.TEMPLATE_POOL).getOrThrow(emptyPoolKey);

        context.register(STONE_TEMPLE_STRUCTURE_POOL,
                new StructurePool(fallback, List.of(), StructurePool.Projection.RIGID));
    }

    public static void bootstrapStructure(Registerable<Structure> context) {
        RegistryKey<StructurePool> emptyPoolKey = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, new Identifier("empty"));
        RegistryEntry<StructurePool> emptyPool = context.getRegistryLookup(RegistryKeys.TEMPLATE_POOL)
                .getOrThrow(emptyPoolKey);

        context.register(STONE_TEMPLE_STRUCTURE,
                new JigsawStructure(
                        new Structure.Config(
                                RegistryEntryList.of(), Collections.emptyMap(),
                                GenerationStep.Feature.SURFACE_STRUCTURES, StructureTerrainAdaptation.NONE
                        ),
                        emptyPool,
                        0,
                        ConstantHeightProvider.create(YOffset.fixed(0)),
                        false,
                        Heightmap.Type.WORLD_SURFACE_WG
                )
        );
    }

    public static void bootstrapStructureSet(Registerable<StructureSet> context) {
        RegistryEntry<Structure> structureEntry = context.getRegistryLookup(RegistryKeys.STRUCTURE)
                .getOrThrow(STONE_TEMPLE_STRUCTURE);

        context.register(STONE_TEMPLE_STRUCTURE_SET,
                new StructureSet(structureEntry,
                        new RandomSpreadStructurePlacement(0, 0, SpreadType.LINEAR, 0)));
    }

    public static void initialize() {
        InfinityForge.LOGGER.info("Initializing Mod Structures for: {}", InfinityForge.MOD_ID);
    }
}
