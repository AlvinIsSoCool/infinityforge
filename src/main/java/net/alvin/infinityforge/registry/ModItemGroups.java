package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup INFINITY_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(InfinityForge.MOD_ID, "infinity_itemgroup"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.infinity"))
                    .icon(() -> new ItemStack(ModItems.INFINITY_GAUNTLET))
                    .entries(((displayContext, entries) -> {
                        entries.add(ModItems.POWER_STONE);
                        entries.add(ModItems.SPACE_STONE);
                        entries.add(ModItems.REALITY_STONE);
                        entries.add(ModItems.SOUL_STONE);
                        entries.add(ModItems.MIND_STONE);
                        entries.add(ModItems.TIME_STONE);

                        entries.add(ModItems.POWER_TESSERACT);
                        entries.add(ModItems.SPACE_TESSERACT);
                        entries.add(ModItems.REALITY_TESSERACT);
                        entries.add(ModItems.SOUL_TESSERACT);
                        entries.add(ModItems.MIND_TESSERACT);
                        entries.add(ModItems.TIME_TESSERACT);

                        entries.add(ModItems.INFINITY_GAUNTLET);
                        entries.add(ModBlueprints.INFINITY_GAUNTLET_BLUEPRINT);

                        entries.add(ModItems.RAW_TITANIUM);
                        entries.add(ModItems.TITANIUM_INGOT);
                        entries.add(ModItems.GOLD_TITANIUM_ALLOY_INGOT);

                        entries.add(ModBlocks.TITANIUM_ORE);
                        entries.add(ModBlocks.DEEPSLATE_TITANIUM_ORE);
                        entries.add(ModBlocks.BLUEPRINT_TABLE);
                    })).build());

    public static void initialize() {
        InfinityForge.LOGGER.info("Registering Item Groups for: " + InfinityForge.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT)
                .register(entries -> entries.addAfter(Items.NETHERITE_AXE, ModItems.INFINITY_GAUNTLET));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.addAfter(Items.RAW_GOLD, ModItems.RAW_TITANIUM);
            entries.addAfter(Items.NETHERITE_INGOT, ModItems.TITANIUM_INGOT, ModItems.GOLD_TITANIUM_ALLOY_INGOT);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL)
                .register(entries -> entries.addAfter(Blocks.DEEPSLATE_DIAMOND_ORE, ModBlocks.TITANIUM_ORE, ModBlocks.DEEPSLATE_TITANIUM_ORE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL)
                .register(entries -> entries.addBefore(Blocks.CRAFTING_TABLE, ModBlocks.BLUEPRINT_TABLE));
    }
}
