package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.abilities.base.GauntletAbility;
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class GauntletAbilityRegistry {
    public static final RegistryKey<Registry<GauntletAbility>> ABILITY_REGISTRY_KEY =
            RegistryKey.ofRegistry(new Identifier(InfinityForge.MOD_ID, "gauntlet_abilities"));
    public static final Registry<GauntletAbility> ABILITY_REGISTRY =
            FabricRegistryBuilder.createSimple(ABILITY_REGISTRY_KEY).buildAndRegister();

    public static GauntletAbility register(GauntletAbility ability) {
        return Registry.register(ABILITY_REGISTRY, ability.getId(), ability);
    }

    public static GauntletAbility get(Identifier id) {
        return ABILITY_REGISTRY.get(id);
    }

    public static void initialize() {
        InfinityForge.LOGGER.info("Initializing Gauntlet Ability Registry for " + InfinityForge.MOD_ID);
    }
}