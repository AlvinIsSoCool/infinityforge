package net.alvin.infinityforge.item;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.ModStones;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    public static final Item POWER_STONE = registerItem("power_stone", new InfinityStoneItem(ModStones.POWER));
    public static final Item SPACE_STONE = registerItem("space_stone", new InfinityStoneItem(ModStones.SPACE));
    public static final Item REALITY_STONE = registerItem("reality_stone", new InfinityStoneItem(ModStones.REALITY));
    public static final Item SOUL_STONE = registerItem("soul_stone", new InfinityStoneItem(ModStones.SOUL));
    public static final Item MIND_STONE = registerItem("mind_stone", new InfinityStoneItem(ModStones.MIND));
    public static final Item TIME_STONE = registerItem("time_stone", new InfinityStoneItem(ModStones.TIME));
    public static final Item INFINITY_GAUNTLET = registerItem("infinity_gauntlet", new InfinityGauntletItem());
    public static final Item POWER_TESSERACT = registerItem("power_tesseract", new InfinityTesseractItem(POWER_STONE));
    public static final Item SPACE_TESSERACT = registerItem("space_tesseract", new InfinityTesseractItem(SPACE_STONE));
    public static final Item REALITY_TESSERACT = registerItem("reality_tesseract", new InfinityTesseractItem(REALITY_STONE));
    public static final Item SOUL_TESSERACT = registerItem("soul_tesseract", new InfinityTesseractItem(SOUL_STONE));
    public static final Item MIND_TESSERACT = registerItem("mind_tesseract", new InfinityTesseractItem(MIND_STONE));
    public static final Item TIME_TESSERACT = registerItem("time_tesseract", new InfinityTesseractItem(TIME_STONE));
    public static final Item FAKE_ITEM = registerItem("fake_item", new FakeItem());
    public static final Item RAW_TITANIUM = registerItem("raw_titanium", new Item(new FabricItemSettings().maxCount(64).fireproof()));
    public static final Item TITANIUM_INGOT = registerItem("titanium_ingot", new Item(new FabricItemSettings().maxCount(64).fireproof()));
    public static final Item GOLD_TITANIUM_ALLOY_INGOT = registerItem("gold_titanium_alloy_ingot", new Item(new FabricItemSettings().maxCount(64).fireproof()));
    public static final Item BLUEPRINT = registerItem("blueprint", new BlueprintItem());

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(InfinityForge.MOD_ID, name), item);
    }

    public static void initialize() {
        InfinityForge.LOGGER.info("Registering Mod Items for: " + InfinityForge.MOD_ID);
    }
}
