package net.alvin.infinityforge.item;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item POWER_STONE = registerItem("power_stone", new ItemInfinityStone());
    public static final Item SPACE_STONE = registerItem("space_stone", new Item(new FabricItemSettings()));
    public static final Item REALITY_STONE = registerItem("reality_stone", new Item(new FabricItemSettings()));
    public static final Item SOUL_STONE = registerItem("soul_stone", new Item(new FabricItemSettings()));
    public static final Item MIND_STONE = registerItem("mind_stone", new Item(new FabricItemSettings()));
    public static final Item TIME_STONE = registerItem("time_stone", new Item(new FabricItemSettings()));
    public static final Item INFINITY_GAUNTLET = registerItem("infinity_gauntlet", new Item(new FabricItemSettings()));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(POWER_STONE);
        entries.add(SPACE_STONE);
        entries.add(REALITY_STONE);
        entries.add(SOUL_STONE);
        entries.add(MIND_STONE);
        entries.add(TIME_STONE);
        entries.add(INFINITY_GAUNTLET);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(InfinityForge.MOD_ID, name), item);
    }

    public static void registerModItems() {
        InfinityForge.LOGGER.info("Registering Mod Items for " + InfinityForge.MOD_ID);

        //ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(ModItems::addItemsToIngredientItemGroup);
    }
}
