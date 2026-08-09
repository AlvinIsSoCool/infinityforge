package net.alvin.infinityforge.client.particle;

import net.alvin.infinityforge.particle.ModParticleEffects;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

public class ModParticles {
    public static void register() {
        ParticleFactoryRegistry.getInstance().register(
                ModParticleEffects.INFINITY_DUST, InfinityDustParticle.Factory::new);
    }
}
