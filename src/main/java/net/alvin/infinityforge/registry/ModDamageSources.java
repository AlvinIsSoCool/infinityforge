package net.alvin.infinityforge.registry;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.world.World;

public class ModDamageSources {
    public static DamageSource powerStone(World world) {
        RegistryEntry<DamageType> entry = world.getRegistryManager()
                .get(RegistryKeys.DAMAGE_TYPE)
                .entryOf(ModDamageTypes.POWER_STONE_TYPE);
        return new DamageSource(entry);
    }

    public static DamageSource blackHole(World world) {
        RegistryEntry<DamageType> entry = world.getRegistryManager()
                .get(RegistryKeys.DAMAGE_TYPE)
                .entryOf(ModDamageTypes.BLACK_HOLE_TYPE);
        return new DamageSource(entry);
    }

    public static DamageSource healthDrain(World world) {
        RegistryEntry<DamageType> entry = world.getRegistryManager()
                .get(RegistryKeys.DAMAGE_TYPE)
                .entryOf(ModDamageTypes.HEALTH_DRAIN_TYPE);
        return new DamageSource(entry);
    }
}
