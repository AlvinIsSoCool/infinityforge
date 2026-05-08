package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
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
            new Identifier(InfinityForge.MOD_ID, "power_stone"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.infinity"))
                    .icon(() -> new ItemStack(ModItems.INFINITY_GAUNTLET))
                    .entries(((displayContext, entries) -> {
                        entries.add(ModItems.POWER_STONE);
                        entries.add(ModItems.SPACE_STONE);
                        entries.add(ModItems.REALITY_STONE);
                        entries.add(ModItems.SOUL_STONE);
                        entries.add(ModItems.MIND_STONE);
                        entries.add(ModItems.TIME_STONE);

                        entries.add(ModItems.INFINITY_GAUNTLET);

                        entries.add(ModItems.POWER_TESSERACT);
                        entries.add(ModItems.SPACE_TESSERACT);
                        entries.add(ModItems.REALITY_TESSERACT);
                        entries.add(ModItems.SOUL_TESSERACT);
                        entries.add(ModItems.MIND_TESSERACT);
                        entries.add(ModItems.TIME_TESSERACT);
                    })).build());

    public static void initialize() {
        InfinityForge.LOGGER.info("Registering Item Groups for: " + InfinityForge.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> entries.addAfter(Items.NETHERITE_AXE, ModItems.INFINITY_GAUNTLET));
    }
}
