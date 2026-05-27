package net.alvin.infinityforge.world.gen;

import com.mojang.serialization.Codec;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.registry.InfinityStoneTypeRegistry;
import net.alvin.infinityforge.registry.ModStones;
import net.alvin.infinityforge.world.data.InfinityStoneTrackerState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CrystalHypercubeProcessor extends StructureProcessor {
    public static final Codec<CrystalHypercubeProcessor> CODEC = Codec.unit(CrystalHypercubeProcessor::new);
    private static final Map<BlockPos, InfinityStoneType> CHOSEN_STONES_CACHE = new HashMap<>();
    private static final Set<BlockPos> SPAWNED_PIVOTS_CACHE = new HashSet<>();
    private static final Map<BlockPos, Boolean> CAN_SPAWN_CACHE = new HashMap<>();
    private static final Map<InfinityStoneType, Map<Block, BlockState>> STONE_THEMES = Map.of(
            ModStones.SPACE, Map.of(Blocks.GLASS, Blocks.BLUE_STAINED_GLASS.getDefaultState(),
                    Blocks.IRON_BLOCK, Blocks.DIAMOND_BLOCK.getDefaultState()),
            ModStones.POWER, Map.of(Blocks.GLASS, Blocks.PURPLE_STAINED_GLASS.getDefaultState(),
                    Blocks.IRON_BLOCK, Blocks.NETHERITE_BLOCK.getDefaultState()),
            ModStones.REALITY, Map.of(Blocks.GLASS, Blocks.RED_STAINED_GLASS.getDefaultState(),
                    Blocks.IRON_BLOCK, Blocks.NETHERITE_BLOCK.getDefaultState()),
            ModStones.SOUL, Map.of(Blocks.GLASS, Blocks.ORANGE_STAINED_GLASS.getDefaultState(),
                    Blocks.IRON_BLOCK, Blocks.GOLD_BLOCK.getDefaultState()),
            ModStones.MIND, Map.of(Blocks.GLASS, Blocks.YELLOW_STAINED_GLASS.getDefaultState(),
                    Blocks.IRON_BLOCK, Blocks.GOLD_BLOCK.getDefaultState()),
            ModStones.TIME, Map.of(Blocks.GLASS, Blocks.LIME_STAINED_GLASS.getDefaultState(),
                    Blocks.IRON_BLOCK, Blocks.EMERALD_BLOCK.getDefaultState())
    );
    private static final Map<RegistryKey<World>, List<InfinityStoneType>> DIMENSION_STONES = new HashMap<>();

    static {
        DIMENSION_STONES.put(World.OVERWORLD, List.of(ModStones.SPACE, ModStones.TIME));
        DIMENSION_STONES.put(World.NETHER, List.of(ModStones.REALITY, ModStones.SOUL));
        DIMENSION_STONES.put(World.END, List.of(ModStones.POWER, ModStones.MIND));
    }

    // TODO: Fix nether roof spawning of this structure.
    @Override
    public @Nullable StructureTemplate.StructureBlockInfo process(WorldView world, BlockPos pos, BlockPos pivot, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo currentBlockInfo, StructurePlacementData data) {
        InfinityStoneType chosenStone = CHOSEN_STONES_CACHE.computeIfAbsent(pivot, k -> {
            if (world instanceof ServerWorldAccess swa)
                return chooseStone(pivot, swa.toServerWorld());
            Random random = Random.create(pivot.asLong());
            return ModStones.ALL_STONES.get(random.nextInt(ModStones.ALL_STONES.size()));
        });

        boolean canSpawn = CAN_SPAWN_CACHE.computeIfAbsent(pivot, k -> {
            if (world instanceof ServerWorldAccess swa)
                return InfinityStoneTrackerState.get(swa.toServerWorld()).canSpawn(chosenStone);
            return true;
        });

        if (!canSpawn) return null;

        if (currentBlockInfo.state().isOf(Blocks.SCULK)) {
            BlockPos blockPos = currentBlockInfo.pos();
            if (world instanceof ServerWorldAccess swa && SPAWNED_PIVOTS_CACHE.add(pivot)) {
                ItemStack tesseractStack = InfinityStoneTypeRegistry.findItemFromStoneType(
                        chosenStone, "_tesseract");

                ServerWorld sw = swa.toServerWorld();
                sw.spawnEntity(new ItemEntity(sw,
                        blockPos.getX() + 0.5,
                        blockPos.getY() + 0.5,
                        blockPos.getZ() + 0.5,
                        tesseractStack));
                InfinityStoneTrackerState.get(sw)
                        .recordSpawn(chosenStone, blockPos, sw.getRegistryKey(), sw.getTime());
            }
            return new StructureTemplate.StructureBlockInfo(
                    blockPos, Blocks.AIR.getDefaultState(), null);
        }

        Map<Block, BlockState> theme = STONE_THEMES.get(chosenStone);
        if (theme != null) {
            BlockState replacement = theme.get(currentBlockInfo.state().getBlock());
            if (replacement != null) {
                return new StructureTemplate.StructureBlockInfo(currentBlockInfo.pos(), replacement, null);
            }
        }

        return currentBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ModStructures.CRYSTAL_HYPERCUBE_PROCESSOR;
    }

    private static InfinityStoneType chooseStone(BlockPos pivot, ServerWorld world) {
        RegistryKey<World> dimension = world.getRegistryKey();
        List<InfinityStoneType> pool = DIMENSION_STONES.getOrDefault(dimension, ModStones.ALL_STONES);
        Random random = Random.create(pivot.asLong());
        return pool.get(random.nextInt(pool.size()));
    }

    public static void clearState() {
        CHOSEN_STONES_CACHE.clear();
        SPAWNED_PIVOTS_CACHE.clear();
        CAN_SPAWN_CACHE.clear();
    }
}
