package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class InfinityStoneTypeRegistry {
    public static final RegistryKey<Registry<InfinityStoneType>> REGISTRY_KEY =
            RegistryKey.ofRegistry(new Identifier(InfinityForge.MOD_ID, "stone_types"));
    public static final Registry<InfinityStoneType> REGISTRY =
            FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();

    public static String getStoneNameFromType(InfinityStoneType stoneType, String suffix, boolean capitalize) {
        Identifier typeId = REGISTRY.getId(stoneType);
        if (typeId == null) return "";

        String path = typeId.getPath();
        if (capitalize && !path.isEmpty())
            path = Character.toUpperCase(path.charAt(0)) + path.substring(1);

        return path + suffix;
    }

    public static Identifier getStoneIdFromType(InfinityStoneType stoneType, String suffix) {
        Identifier typeId = REGISTRY.getId(stoneType);
        if (typeId == null) return new Identifier("");

        return typeId.withPath(typeId.getPath() + suffix);
    }

    public static ItemStack findItemFromStoneType(InfinityStoneType type, String suffix) {
        Identifier stoneId = InfinityStoneTypeRegistry.getStoneIdFromType(type, suffix);
        return Registries.ITEM.getOrEmpty(stoneId)
                .map(ItemStack::new)
                .orElse(ItemStack.EMPTY);
    }

    public static void initialize() {
        InfinityForge.LOGGER.info("Initializing Stone Type Registry for: {}", InfinityForge.MOD_ID);
    }
}
