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
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.SpreadType;
import net.minecraft.world.gen.chunk.placement.StructurePlacement;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ModWorldGenerator extends FabricDynamicRegistryProvider {
    public ModWorldGenerator(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
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

        RegistryEntryLookup<StructurePool> poolLookup =
                registries.getWrapperOrThrow(RegistryKeys.TEMPLATE_POOL);
        RegistryEntry<StructurePool> startPool =
                poolLookup.getOrThrow(ModStructures.CRYSTAL_HYPERCUBE_STRUCTURE_POOL);

        RegistryWrapper<Biome> biomeWrapper = registries.getWrapperOrThrow(RegistryKeys.BIOME);
        RegistryEntryList<Biome> allBiomes = RegistryEntryList.of(
                biomeWrapper.streamEntries().collect(Collectors.toList())
        );

        JigsawStructure myStructure = ModStructures.createStructure(allBiomes, startPool);
        entries.add(ModStructures.CRYSTAL_HYPERCUBE_STRUCTURE, myStructure);

        RegistryEntry<Structure> structureEntry =
                registries.getWrapperOrThrow(RegistryKeys.STRUCTURE).getOrThrow(ModStructures.CRYSTAL_HYPERCUBE_STRUCTURE);
        StructurePlacement placement = new RandomSpreadStructurePlacement(
                10, 5, SpreadType.LINEAR, 725639014
        );

        StructureSet structureSet = new StructureSet(structureEntry, placement);
        entries.add(ModStructures.CRYSTAL_HYPERCUBE_STRUCTURE_SET, structureSet);
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
    public String getName() {
        return "World Gen";
    }
}
