package net.alvin.infinityforge.registries;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class InfinityStoneTypeRegistry {
    public static final RegistryKey<Registry<InfinityStoneType>> STONE_TYPE_REGISTRY_KEY =
            RegistryKey.ofRegistry(new Identifier(InfinityForge.MOD_ID, "stone_types"));
    public static final Registry<InfinityStoneType> STONE_TYPE_REGISTRY =
            FabricRegistryBuilder.createSimple(STONE_TYPE_REGISTRY_KEY).buildAndRegister();

    public static void initialize() {
        InfinityForge.LOGGER.info("Initializing Stone Type Registry for " + InfinityForge.MOD_ID);
    }
}
