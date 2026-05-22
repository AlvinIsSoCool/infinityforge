package net.alvin.infinityforge.item;

import net.alvin.infinityforge.registry.ModItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BlueprintItem extends Item {
    private static final String OUTPUT_KEY = "output";
    private static final String INGREDIENT_KEY = "ingredient";
    private static final String CRAFTING_TIME_KEY = "crafting_time";

    public BlueprintItem() { super(new FabricItemSettings().maxCount(1)); }

    public static ItemStack create(ItemStack output, ItemStack ingredient, int craftingTime) {
        ItemStack stack = new ItemStack(ModItems.BLUEPRINT);
        NbtCompound nbt = stack.getOrCreateNbt();

        NbtCompound outputNbt = new NbtCompound();
        output.writeNbt(outputNbt);
        nbt.put(OUTPUT_KEY, outputNbt);

        NbtCompound ingredientNbt = new NbtCompound();
        ingredient.writeNbt(ingredientNbt);
        nbt.put(INGREDIENT_KEY, ingredientNbt);

        nbt.putInt(CRAFTING_TIME_KEY, craftingTime);
        return stack;
    }

    public static ItemStack getOutput(ItemStack blueprint) {
        NbtCompound nbt = blueprint.getNbt();
        if (nbt == null || !nbt.contains(OUTPUT_KEY)) return ItemStack.EMPTY;
        return ItemStack.fromNbt(nbt.getCompound(OUTPUT_KEY));
    }

    public static ItemStack getIngredient(ItemStack blueprint) {
        NbtCompound nbt = blueprint.getNbt();
        if (nbt == null || !nbt.contains(INGREDIENT_KEY)) return ItemStack.EMPTY;
        return ItemStack.fromNbt(nbt.getCompound(INGREDIENT_KEY));
    }

    public static int getCraftingTime(ItemStack blueprint) {
        NbtCompound nbt = blueprint.getNbt();
        if (nbt == null || !nbt.contains(CRAFTING_TIME_KEY)) return 0;
        return nbt.getInt(CRAFTING_TIME_KEY);
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) { return false; }

    @Override
    public Rarity getRarity(ItemStack stack) { return Rarity.RARE; }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        ItemStack output = BlueprintItem.getOutput(stack);
        ItemStack ingredient = BlueprintItem.getIngredient(stack);

        if (output.isEmpty() || ingredient.isEmpty()) {
            tooltip.add(Text.literal("Empty").formatted(Formatting.GRAY));
            return;
        }

        tooltip.add(Text.literal("Output: ").formatted(Formatting.GRAY));
        tooltip.add(Text.literal(String.format("  %dx ", output.getCount()))
                .append(output.getName().copy().formatted(output.getRarity().formatting))
        );
        tooltip.add(Text.empty());
        tooltip.add(Text.literal("Ingredient: ").formatted(Formatting.GRAY));
        tooltip.add(Text.literal(String.format("  %dx ", ingredient.getCount()))
                .append(ingredient.getName().copy().formatted(ingredient.getRarity().formatting))
        );

        super.appendTooltip(stack, world, tooltip, context);
    }
}