package net.alvin.infinityforge.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;

public class BlueprintItem extends Item {
    private final BlueprintRecipe recipe;

    public BlueprintItem(BlueprintRecipe recipe) {
        super(new FabricItemSettings().maxDamage(0).maxCount(1));
        this.recipe = recipe;
    }

    public BlueprintRecipe getRecipe() { return recipe; }

    @Override
    public boolean isItemBarVisible(ItemStack stack) { return false; }

    @Override
    public Rarity getRarity(ItemStack stack) { return Rarity.RARE; }
}
