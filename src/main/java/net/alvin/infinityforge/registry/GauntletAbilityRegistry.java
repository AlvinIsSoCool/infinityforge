package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.abilities.base.GauntletAbility;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class GauntletAbilityRegistry {
    public static final RegistryKey<Registry<GauntletAbility>> REGISTRY_KEY =
            RegistryKey.ofRegistry(new Identifier(InfinityForge.MOD_ID, "gauntlet_abilities"));
    public static final Registry<GauntletAbility> REGISTRY =
            FabricRegistryBuilder.createSimple(REGISTRY_KEY).buildAndRegister();

    public static GauntletAbility register(GauntletAbility ability) {
        return Registry.register(REGISTRY, ability.getId(), ability);
    }

    public static GauntletAbility get(Identifier id) {
        return REGISTRY.get(id);
    }

    public static void initialize() {
        InfinityForge.LOGGER.info("Initializing Gauntlet Ability Registry for: {}", InfinityForge.MOD_ID);
    }
}