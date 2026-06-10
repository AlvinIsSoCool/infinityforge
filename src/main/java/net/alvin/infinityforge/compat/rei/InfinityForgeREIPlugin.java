package net.alvin.infinityforge.compat.rei;

import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.alvin.infinityforge.block.ModBlocks;
import net.alvin.infinityforge.registry.ModBlueprints;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;

@Environment(EnvType.CLIENT)
public class InfinityForgeREIPlugin implements REIClientPlugin {
    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new BlueprintCategory());
        registry.addWorkstations(BlueprintCategory.ID, EntryStacks.of(ModBlocks.BLUEPRINT_TABLE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {
        for (ItemStack blueprint : ModBlueprints.ALL_BLUEPRINTS) {
            registry.add(new BlueprintDisplay(blueprint));
        }
    }
}
