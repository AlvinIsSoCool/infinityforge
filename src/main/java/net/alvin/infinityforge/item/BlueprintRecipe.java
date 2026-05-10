package net.alvin.infinityforge.item;

import net.minecraft.item.ItemStack;

public record BlueprintRecipe(ItemStack input, ItemStack output, int craftingTime) {
    public BlueprintRecipe {
        input = input.copy();
        output = output.copy();
    }
}
