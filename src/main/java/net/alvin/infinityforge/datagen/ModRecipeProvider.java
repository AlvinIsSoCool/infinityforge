package net.alvin.infinityforge.datagen;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output) { super(output); }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter) {
        CookingRecipeJsonBuilder.createSmelting(
                    Ingredient.ofItems(ModItems.RAW_TITANIUM),
                    RecipeCategory.MISC,
                    ModItems.TITANIUM_INGOT,
                    2.0f, 1200)
                .criterion(hasItem(ModItems.RAW_TITANIUM), conditionsFromItem(ModItems.RAW_TITANIUM))
                .offerTo(exporter, new Identifier(InfinityForge.MOD_ID, "titanium_ingot_from_smelting"));

        CookingRecipeJsonBuilder.createBlasting(
                        Ingredient.ofItems(ModItems.RAW_TITANIUM),
                        RecipeCategory.MISC,
                        ModItems.TITANIUM_INGOT,
                        2.0f, 600)
                .criterion(hasItem(ModItems.RAW_TITANIUM), conditionsFromItem(ModItems.RAW_TITANIUM))
                .offerTo(exporter, new Identifier(InfinityForge.MOD_ID, "titanium_ingot_from_blasting"));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.GOLD_TITANIUM_ALLOY_INGOT)
                .input(Items.GOLD_INGOT, 4)
                .input(ModItems.TITANIUM_INGOT, 4)
                .criterion(hasItem(Items.GOLD_INGOT), conditionsFromItem(Items.GOLD_INGOT))
                .criterion(hasItem(ModItems.TITANIUM_INGOT), conditionsFromItem(ModItems.TITANIUM_INGOT))
                .offerTo(exporter);
    }
}
