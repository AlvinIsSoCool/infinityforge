package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.item.BlueprintItem;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ModBlueprints {
    public static final ItemStack INFINITY_GAUNTLET_BLUEPRINT = BlueprintItem.create(
            new ItemStack(ModItems.INFINITY_GAUNTLET),
            new ItemStack(ModItems.GOLD_TITANIUM_ALLOY_INGOT, 16),
            1200
    );

    public static final List<ItemStack> ALL_BLUEPRINTS = List.of(
            INFINITY_GAUNTLET_BLUEPRINT
    );
}
