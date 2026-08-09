package net.alvin.infinityforge.client.particle;

import net.alvin.infinityforge.particle.InfinityDustParticleEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class InfinityDustParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
    private final boolean glowing;

    protected InfinityDustParticle(
            ClientWorld world, double x, double y, double z,
            double velocityX, double velocityY, double velocityZ,
            InfinityDustParticleEffect parameters, SpriteProvider spriteProvider
    ) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);
        this.velocityMultiplier = 0.90F + this.random.nextFloat() * 0.08F;
        this.ascending = true;
        this.spriteProvider = spriteProvider;
        this.gravityStrength = 0.025F + this.random.nextFloat() * 0.05F;

        float f = this.random.nextFloat() * 0.4F + 0.6F;
        this.red = this.darken(parameters.getColor().x(), f);
        this.green = this.darken(parameters.getColor().y(), f);
        this.blue = this.darken(parameters.getColor().z(), f);
        this.scale = this.scale * (0.75F * parameters.getScale());
        this.glowing = parameters.shouldGlow();

        if (!parameters.shouldRespectVelocity()) {
            this.velocityX *= 0.1F;
            this.velocityY *= 0.1F;
            this.velocityZ *= 0.1F;
        }

        int i = (int) (8.0 / (this.random.nextDouble() * 0.8 + 0.2));
        this.maxAge = (int) Math.max(i * parameters.getScale(), 2.0F);
        this.setSpriteForAge(spriteProvider);
    }

    protected float darken(float colorComponent, float multiplier) {
        return (this.random.nextFloat() * 0.2F + 0.8F) * colorComponent * multiplier;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public float getSize(float tickDelta) {
        return this.scale * MathHelper.clamp((this.age + tickDelta) / this.maxAge * 32.0F,
                0.0F, 1.0F);
    }

    @Override
    protected int getBrightness(float tint) {
        return (this.glowing) ? LightmapTextureManager.MAX_LIGHT_COORDINATE
                : super.getBrightness(tint);
    }

    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
    }

    @Environment(EnvType.CLIENT)
    public static class Factory implements ParticleFactory<InfinityDustParticleEffect> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Override
        public Particle createParticle(
                InfinityDustParticleEffect parameters, ClientWorld world,
                double x, double y, double z, double vx, double vy, double vz
        ) {
            return new InfinityDustParticle(world, x, y, z, vx, vy, vz, parameters, this.spriteProvider);
        }
    }
}