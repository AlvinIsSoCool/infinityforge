package net.alvin.infinityforge.particle;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;

public class ModParticleHelper {
    /**
     * Spawns Infinity Dust Particles around the player.
     * Particles don't respect velocity.
     * @param world The world in which the player is in.
     * @param player The player to spawn particles around.
     * @param rgb The color of the particle in RGB format.
     * @param count The number of particles to spawn.
     */
    public static void spawnParticlesPlayer(ServerWorld world, PlayerEntity player, int rgb, int count) {
        Box box = player.getBoundingBox();
        double width = box.maxX - box.minX;
        double depth = box.maxZ - box.minZ;
        Random random = world.random;

        double centerX = box.minX + width / 2;
        double centerZ = box.minZ + depth / 2;

        for (int i = 0; i < count; i++) {
            double px, pz;
            double t = random.nextDouble();
            switch (random.nextInt(4)) {
                case 0 -> { px = box.minX + width * t; pz = box.minZ; }
                case 1 -> { px = box.minX + width * t; pz = box.maxZ; }
                case 2 -> { px = box.minX; pz = box.minZ + depth * t; }
                default -> { px = box.maxX; pz = box.minZ + depth * t; }
            }
            double py = box.minY + random.nextDouble() * (box.maxY - box.minY);

            double vx = (px - centerX) * 0.025;
            double vz = (pz - centerZ) * 0.025;
            double vy = -0.05 - random.nextDouble() * 0.05;

            ParticleEffect effect = new InfinityDustParticleEffect(Vec3d.unpackRgb(rgb).toVector3f(),
                    0.3f + random.nextFloat() * 0.4f, true, false);
            world.spawnParticles(effect, px, py, pz, 0, vx, vy, vz, 1.0);
        }
    }
}
