package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.item.ModItems;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.function.SetNbtLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

public class ModLootTableModifiers {
    private static final Identifier VILLAGE_ARMORER_CHEST_ID = new Identifier("minecraft", "chests/village/village_armorer");

    @SuppressWarnings("deprecation")
    public static void register() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (id.equals(VILLAGE_ARMORER_CHEST_ID)) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.1f))
                        .with(ItemEntry.builder(ModItems.BLUEPRINT)
                                .apply(SetNbtLootFunction.builder(
                                        ModBlueprints.INFINITY_GAUNTLET_BLUEPRINT.getNbt())))
                        .apply(SetCountLootFunction.builder(
                                UniformLootNumberProvider.create(1.0f, 1.0f)).build());
                tableBuilder.pool(poolBuilder.build());
            }
        });
    }
}
