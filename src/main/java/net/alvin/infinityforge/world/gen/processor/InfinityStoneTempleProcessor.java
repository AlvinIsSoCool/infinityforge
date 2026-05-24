package net.alvin.infinityforge.world.gen.processor;

import com.mojang.serialization.Codec;
import net.alvin.infinityforge.world.gen.ModStructures;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class InfinityStoneTempleProcessor extends StructureProcessor {
    public static final Codec<InfinityStoneTempleProcessor> CODEC = Codec.unit(InfinityStoneTempleProcessor::new);

    @Override
    public @Nullable StructureTemplate.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlacementData data) {
        return currentBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructures.STONE_TEMPLE_PROCESSOR;
    }
}
