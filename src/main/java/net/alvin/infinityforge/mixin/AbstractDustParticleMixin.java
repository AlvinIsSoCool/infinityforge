package net.alvin.infinityforge.mixin;

import net.minecraft.client.particle.AbstractDustParticle;
import net.minecraft.client.render.LightmapTextureManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AbstractDustParticle.class)
public abstract class AbstractDustParticleMixin {
    @Unique
    public int getBrightness(float tint) {
        return LightmapTextureManager.MAX_LIGHT_COORDINATE;
    }
}
