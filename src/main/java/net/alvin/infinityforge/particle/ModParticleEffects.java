package net.alvin.infinityforge.particle;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticleEffects {
    public static final ParticleType<InfinityDustParticleEffect> INFINITY_DUST =
            FabricParticleTypes.complex(InfinityDustParticleEffect.FACTORY);

    public static void register() {
        InfinityForge.LOGGER.info("Registering Particles for: {}", InfinityForge.MOD_ID);
        Registry.register(Registries.PARTICLE_TYPE,
                new Identifier(InfinityForge.MOD_ID, "infinity_dust"), INFINITY_DUST);
    }
}
