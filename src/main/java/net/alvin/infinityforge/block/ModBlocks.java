package net.alvin.infinityforge.block;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;

public class ModBlocks {
    public static final Block TITANIUM_ORE = registerBlock("titanium_ore",
            new ExperienceDroppingBlock(FabricBlockSettings.copyOf(Blocks.DIAMOND_ORE).strength(4.5f, 6.0f), UniformIntProvider.create(4, 9)));
    public static final Block DEEPSLATE_TITANIUM_ORE = registerBlock("deepslate_titanium_ore",
            new ExperienceDroppingBlock(FabricBlockSettings.copyOf(Blocks.DEEPSLATE_DIAMOND_ORE).strength(6.0f, 7.0f), UniformIntProvider.create(4, 9)));
    public static final Block BLUEPRINT_TABLE = registerBlock("blueprint_table",
            new BlueprintTableBlock(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK).nonOpaque()));
    public static final Block FAKE_BLOCK = registerBlock("fake_block",
            new FakeBlock(FabricBlockSettings.copyOf(Blocks.DIRT).nonOpaque()));

    private static Block registerBlock(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(InfinityForge.MOD_ID, name),
                new BlockItem(block, new FabricItemSettings()));
        return Registry.register(Registries.BLOCK, new Identifier(InfinityForge.MOD_ID, name), block);
    }

    public static void initialize() {
        InfinityForge.LOGGER.info("Registering blocks for " + InfinityForge.MOD_ID);
    }
}
