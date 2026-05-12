package net.alvin.infinityforge.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class InfinityTesseractItem extends Item {
    private final Item stoneItem;

    public InfinityTesseractItem(Item stoneItem) {
        super(new FabricItemSettings().maxDamage(0).fireproof());
        this.stoneItem = stoneItem;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) { return false; }

    public Item getStoneItem() {
        return stoneItem;
    }
}
