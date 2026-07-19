package net.alvin.infinityforge.infinity.abilities.impl.reality;

import net.alvin.infinityforge.config.InfinityForgeConfig;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ToggleAbility;
import net.alvin.infinityforge.accessor.PlayerEffectsAccess;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

import java.util.List;
import java.util.function.Supplier;

public class InvisibilityAbility extends ToggleAbility {
    public InvisibilityAbility(Identifier id, AbilityIcon icon, String key, Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones, int maxChargeTicks, int refillRateTicks) {
        super(id, icon, key, color, requiredStones, maxChargeTicks, refillRateTicks);
    }

    @Override
    public boolean onEnable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        PlayerEffectsAccess access = (PlayerEffectsAccess) player;
        access.setCustomInvisible(true);
        spawnInvisibilityParticles(world, player, 75);
        return true;
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {}

    @Override
    public void onDisable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        PlayerEffectsAccess access = (PlayerEffectsAccess) player;
        access.setCustomInvisible(false);
        spawnInvisibilityParticles(world, player, 75);
    }

    public static void spawnInvisibilityParticles(ServerWorld world, PlayerEntity player, int count) {
        Box box = player.getBoundingBox();
        double width = box.maxX - box.minX;
        double depth = box.maxZ - box.minZ;
        Random random = world.random;

        double centerX = box.minX + width / 2;
        double centerZ = box.minZ + depth / 2;

        for (int i = 0; i < count; i++) {
            // pick a random point on one of the box's 4 vertical side faces
            double px, pz;
            double t = random.nextDouble();
            switch (random.nextInt(4)) {
                case 0 -> { px = box.minX + width * t; pz = box.minZ; }
                case 1 -> { px = box.minX + width * t; pz = box.maxZ; }
                case 2 -> { px = box.minX; pz = box.minZ + depth * t; }
                default -> { px = box.maxX; pz = box.minZ + depth * t; }
            }
            double py = box.minY + random.nextDouble() * (box.maxY - box.minY);

            // slight outward drift away from center, plus a short downward fall
            double vx = (px - centerX) * 0.025;
            double vz = (pz - centerZ) * 0.025;
            double vy = -0.05 - random.nextDouble() * 0.05;

            ParticleEffect effect = new DustParticleEffect(Vec3d.unpackRgb(
                    InfinityForgeConfig.get().colorOptions.stoneGlintColors.realityStone).toVector3f(),
                    0.3f + random.nextFloat() * 0.3f);
            world.spawnParticles(effect, px, py, pz, 0, vx, vy, vz, 1.0);
        }
    }
}
