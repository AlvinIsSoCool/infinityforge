package net.alvin.infinityforge.compat.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryIngredients;
import net.alvin.infinityforge.item.BlueprintItem;
import net.minecraft.item.ItemStack;

import java.util.List;

public class BlueprintDisplay implements Display {
    private final List<EntryIngredient> inputs;
    private final List<EntryIngredient> outputs;
    private final ItemStack blueprintStack;

    public BlueprintDisplay(ItemStack blueprint) {
        this.blueprintStack = blueprint;
        ItemStack ingredient = BlueprintItem.getIngredient(blueprint);
        ItemStack output = BlueprintItem.getOutput(blueprint);

        this.inputs = List.of(
                EntryIngredients.of(ingredient),
                EntryIngredients.of(blueprint)
        );
        this.outputs = List.of(EntryIngredients.of(output));
    }

    @Override
    public List<EntryIngredient> getInputEntries() { return inputs; }

    @Override
    public List<EntryIngredient> getOutputEntries() { return outputs; }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return BlueprintCategory.ID;
    }

    public ItemStack getBlueprintStack() { return blueprintStack; }
}
