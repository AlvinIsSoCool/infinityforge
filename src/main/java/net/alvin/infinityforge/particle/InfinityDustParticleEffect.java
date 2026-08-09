package net.alvin.infinityforge.particle;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector3f;

import java.util.Locale;

public class InfinityDustParticleEffect implements ParticleEffect {
    private final Vector3f color;
    private final float scale;
    private final boolean glowing;
    private final boolean respectVelocity;

    @SuppressWarnings("deprecation")
    public static final ParticleEffect.Factory<InfinityDustParticleEffect> FACTORY =
            new ParticleEffect.Factory<>() {
                @Override
                public InfinityDustParticleEffect read(ParticleType<InfinityDustParticleEffect> type, StringReader reader) throws CommandSyntaxException {
                    reader.expect(' ');
                    float r = reader.readFloat();
                    reader.expect(' ');
                    float g = reader.readFloat();
                    reader.expect(' ');
                    float b = reader.readFloat();
                    reader.expect(' ');
                    float scale = reader.readFloat();
                    reader.expect(' ');
                    boolean glowing = reader.readBoolean();
                    boolean respectVelocity = reader.readBoolean();
                    return new InfinityDustParticleEffect(new Vector3f(r, g, b), scale, glowing, respectVelocity);
                }

                @Override
                public InfinityDustParticleEffect read(ParticleType<InfinityDustParticleEffect> type, PacketByteBuf buf) {
                    return new InfinityDustParticleEffect(
                            new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat()),
                            buf.readFloat(),
                            buf.readBoolean(),
                            buf.readBoolean()
                    );
                }
            };

    public InfinityDustParticleEffect(Vector3f color, float scale, boolean glowing, boolean respectVelocity) {
        this.color = color;
        this.scale = MathHelper.clamp(scale, 0.01F, 4.0F);
        this.glowing = glowing;
        this.respectVelocity = respectVelocity;
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticleEffects.INFINITY_DUST;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeFloat(this.color.x());
        buf.writeFloat(this.color.y());
        buf.writeFloat(this.color.z());
        buf.writeFloat(this.scale);
        buf.writeBoolean(this.glowing);
        buf.writeBoolean(this.respectVelocity);
    }

    @Override
    public String asString() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %b %b",
                Registries.PARTICLE_TYPE.getId(getType()),
                color.x(), color.y(), color.z(), scale, glowing, respectVelocity);
    }

    public Vector3f getColor() { return this.color; }
    public float getScale() { return this.scale; }
    public boolean shouldGlow() { return this.glowing; }
    public boolean shouldRespectVelocity() { return this.respectVelocity; }
}
