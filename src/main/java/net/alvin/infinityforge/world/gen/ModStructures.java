package net.alvin.infinityforge.world.gen;

import net.alvin.infinityforge.InfinityForge;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
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
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.SpreadType;
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;

import java.util.Collections;
import java.util.List;

public class ModStructures {
    public static final StructureProcessorType<CrystalHypercubeProcessor> CRYSTAL_HYPERCUBE_PROCESSOR =
            StructureProcessorType.register("infinityforge:crystal_hypercube_processor", CrystalHypercubeProcessor.CODEC);
    public static final RegistryKey<Structure> CRYSTAL_HYPERCUBE_OVERWORLD = RegistryKey.of(RegistryKeys.STRUCTURE, new Identifier(InfinityForge.MOD_ID, "crystal_hypercube_overworld"));
    public static final RegistryKey<StructureSet> CRYSTAL_HYPERCUBE_OVERWORLD_SET = RegistryKey.of(RegistryKeys.STRUCTURE_SET, new Identifier(InfinityForge.MOD_ID, "crystal_hypercube_overworld_set"));
    public static final RegistryKey<Structure> CRYSTAL_HYPERCUBE_NETHER = RegistryKey.of(RegistryKeys.STRUCTURE, new Identifier(InfinityForge.MOD_ID, "crystal_hypercube_nether"));
    public static final RegistryKey<StructureSet> CRYSTAL_HYPERCUBE_NETHER_SET = RegistryKey.of(RegistryKeys.STRUCTURE_SET, new Identifier(InfinityForge.MOD_ID, "crystal_hypercube_nether_set"));
    public static final RegistryKey<Structure> CRYSTAL_HYPERCUBE_END = RegistryKey.of(RegistryKeys.STRUCTURE, new Identifier(InfinityForge.MOD_ID, "crystal_hypercube_end"));
    public static final RegistryKey<StructureSet> CRYSTAL_HYPERCUBE_END_SET = RegistryKey.of(RegistryKeys.STRUCTURE_SET, new Identifier(InfinityForge.MOD_ID, "crystal_hypercube_end_set"));
    public static final RegistryKey<StructurePool> CRYSTAL_HYPERCUBE_STRUCTURE_POOL = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, new Identifier(InfinityForge.MOD_ID, "crystal_hypercube_structure_pool"));
    public static final RegistryKey<StructureProcessorList> CRYSTAL_HYPERCUBE_PROCESSOR_LIST = RegistryKey.of(RegistryKeys.PROCESSOR_LIST, new Identifier(InfinityForge.MOD_ID, "crystal_hypercube_processor_list"));

    public static void bootstrapProcessors(Registerable<StructureProcessorList> context) {
        context.register(CRYSTAL_HYPERCUBE_PROCESSOR_LIST,
                new StructureProcessorList(List.of(new CrystalHypercubeProcessor())));
    }

    public static void bootstrapPools(Registerable<StructurePool> context) {
        RegistryKey<StructurePool> emptyPoolKey =
                RegistryKey.of(RegistryKeys.TEMPLATE_POOL, new Identifier("empty"));
        RegistryEntry<StructurePool> fallback =
                context.getRegistryLookup(RegistryKeys.TEMPLATE_POOL).getOrThrow(emptyPoolKey);

        context.register(CRYSTAL_HYPERCUBE_STRUCTURE_POOL,
                new StructurePool(fallback, List.of(), StructurePool.Projection.RIGID));
    }

    public static void bootstrapStructure(Registerable<Structure> context) {
        RegistryKey<StructurePool> emptyPoolKey = RegistryKey.of(RegistryKeys.TEMPLATE_POOL, new Identifier("empty"));
        RegistryEntry<StructurePool> emptyPool = context.getRegistryLookup(RegistryKeys.TEMPLATE_POOL)
                .getOrThrow(emptyPoolKey);
        JigsawStructure emptyStructure = new JigsawStructure(
                new Structure.Config(
                        RegistryEntryList.of(),
                        Collections.emptyMap(),
                        GenerationStep.Feature.SURFACE_STRUCTURES,
                        StructureTerrainAdaptation.NONE
                ),
                emptyPool,
                0,
                ConstantHeightProvider.create(YOffset.fixed(0)),
                false,
                Heightmap.Type.WORLD_SURFACE_WG
        );

        context.register(CRYSTAL_HYPERCUBE_OVERWORLD, emptyStructure);
        context.register(CRYSTAL_HYPERCUBE_NETHER, emptyStructure);
        context.register(CRYSTAL_HYPERCUBE_END, emptyStructure);
    }

    public static void bootstrapStructureSet(Registerable<StructureSet> context) {
        RegistryEntryLookup<Structure> structureLookup = context.getRegistryLookup(RegistryKeys.STRUCTURE);

        context.register(CRYSTAL_HYPERCUBE_OVERWORLD_SET,
                new StructureSet(structureLookup.getOrThrow(CRYSTAL_HYPERCUBE_OVERWORLD),
                        new RandomSpreadStructurePlacement(0, 0, SpreadType.LINEAR, 0)));

        context.register(CRYSTAL_HYPERCUBE_NETHER_SET,
                new StructureSet(structureLookup.getOrThrow(CRYSTAL_HYPERCUBE_NETHER),
                        new RandomSpreadStructurePlacement(0, 0, SpreadType.LINEAR, 0)));

        context.register(CRYSTAL_HYPERCUBE_END_SET,
                new StructureSet(structureLookup.getOrThrow(CRYSTAL_HYPERCUBE_END),
                        new RandomSpreadStructurePlacement(0, 0, SpreadType.LINEAR, 0)));
    }

    public static void initialize() {
        InfinityForge.LOGGER.info("Initializing Mod Structures for: {}", InfinityForge.MOD_ID);
    }
}
