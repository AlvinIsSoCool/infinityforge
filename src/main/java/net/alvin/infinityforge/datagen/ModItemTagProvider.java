package net.alvin.infinityforge.datagen;

import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.registry.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends FabricTagProvider.ItemTagProvider {

    public ModItemTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup lookup) {
        getOrCreateTagBuilder(ModTags.Items.INFINITY_STONES)
                .add(ModItems.POWER_STONE)
                .add(ModItems.SPACE_STONE)
                .add(ModItems.REALITY_STONE)
                .add(ModItems.SOUL_STONE)
                .add(ModItems.TIME_STONE)
                .add(ModItems.MIND_STONE);
        getOrCreateTagBuilder(ModTags.Items.INFINITY_TESSERACTS)
                .add(ModItems.POWER_TESSERACT)
                .add(ModItems.SPACE_TESSERACT)
                .add(ModItems.REALITY_TESSERACT)
                .add(ModItems.SOUL_TESSERACT)
                .add(ModItems.TIME_TESSERACT)
                .add(ModItems.MIND_TESSERACT);
        getOrCreateTagBuilder(ModTags.Items.INFINITY_ITEMS)
                .add(ModItems.INFINITY_GAUNTLET)
                .addTag(ModTags.Items.INFINITY_STONES)
                .addTag(ModTags.Items.INFINITY_TESSERACTS);
    }
}