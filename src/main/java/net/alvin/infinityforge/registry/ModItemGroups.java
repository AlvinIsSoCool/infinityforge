package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {
    public static final ItemGroup INFINITY_GROUP = Registry.register(Registries.ITEM_GROUP,
            new Identifier(InfinityForge.MOD_ID, "power_stone"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.infinity"))
                    .icon(() -> new ItemStack(ModItems.POWER_STONE))
                    .entries(((displayContext, entries) -> {
                        entries.add(ModItems.POWER_STONE);
                        entries.add(ModItems.SPACE_STONE);
                        entries.add(ModItems.REALITY_STONE);
                        entries.add(ModItems.SOUL_STONE);
                        entries.add(ModItems.MIND_STONE);
                        entries.add(ModItems.TIME_STONE);
                        entries.add(ModItems.INFINITY_GAUNTLET);
                    })).build());

    public static void initialize() {
        InfinityForge.LOGGER.info("Registering Item Groups for: " + InfinityForge.MOD_ID);
    }
}
