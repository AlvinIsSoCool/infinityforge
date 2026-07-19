package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.minecraft.entity.damage.*;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModDamageTypes {
    public static final RegistryKey<DamageType> POWER_STONE_TYPE =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(InfinityForge.MOD_ID,
                    "power_stone"));
    public static final RegistryKey<DamageType> BLACK_HOLE_TYPE =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(InfinityForge.MOD_ID,
                    "black_hole"));
    public static final RegistryKey<DamageType> HEALTH_DRAIN_TYPE =
            RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(InfinityForge.MOD_ID,
                    "health_drain"));

    public static void bootstrap(Registerable<DamageType> registerable) {
        registerable.register(POWER_STONE_TYPE,
                new DamageType("infinityforge.power_stone",
                        DamageScaling.NEVER, 0.0f,
                        DamageEffects.HURT, DeathMessageType.DEFAULT));
        registerable.register(BLACK_HOLE_TYPE,
                new DamageType("infinityforge.black_hole",
                        DamageScaling.NEVER, 0.0f,
                        DamageEffects.HURT, DeathMessageType.DEFAULT));
        registerable.register(HEALTH_DRAIN_TYPE,
                new DamageType("infinityforge.health_drain",
                        DamageScaling.NEVER, 2.0f,
                        DamageEffects.HURT, DeathMessageType.DEFAULT));
    }
}
