package net.alvin.infinityforge.item;

import net.minecraft.item.ItemStack;

/**
 * The record class that holds info about the crafting recipe related to the blueprint table block.
 * @param input The ItemStack that is taken by the blueprint table.
 *              The blueprint item is an input by default and is not
 *              to be specified as an input item.
 * @param output The ItemStack output of the crafting recipe.
 * @param craftingTime The time the crafting process takes.
 */
public record BlueprintRecipe(ItemStack input, ItemStack output, int craftingTime) {
    public BlueprintRecipe {
        input = input.copy();
        output = output.copy();
    }
}
