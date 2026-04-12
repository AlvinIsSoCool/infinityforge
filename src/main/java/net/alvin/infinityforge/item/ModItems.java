package net.alvin.infinityforge.item;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.InfinityStoneItem;
import net.alvin.infinityforge.infinity.InfinityStones;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item POWER_STONE = registerItem("power_stone", new InfinityStoneItem(InfinityStones.POWER));
    public static final Item SPACE_STONE = registerItem("space_stone", new InfinityStoneItem(InfinityStones.SPACE));
    public static final Item REALITY_STONE = registerItem("reality_stone", new InfinityStoneItem(InfinityStones.REALITY));
    public static final Item SOUL_STONE = registerItem("soul_stone", new InfinityStoneItem(InfinityStones.SOUL));
    public static final Item MIND_STONE = registerItem("mind_stone", new InfinityStoneItem(InfinityStones.MIND));
    public static final Item TIME_STONE = registerItem("time_stone", new InfinityStoneItem(InfinityStones.TIME));
    public static final Item INFINITY_GAUNTLET = registerItem("infinity_gauntlet", new Item(new FabricItemSettings()));

    // ITEMS HERE //

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(InfinityForge.MOD_ID, name), item);
    }

    public static void registerModItems() {
        InfinityForge.LOGGER.info("Registering Mod Items for: " + InfinityForge.MOD_ID);
    }
}
