package net.alvin.infinityforge.infinity.abilities.impl.power;

import net.alvin.infinityforge.config.InfinityForgeConfig;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.HeldAbility;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.particle.InfinityDustParticleEffect;
import net.alvin.infinityforge.server.state.GauntletChargeState;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.joml.Vector3f;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class ExplosionAbility extends HeldAbility {
    public ExplosionAbility(Identifier id, AbilityIcon icon,
                            Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones,
                            int maxChargeTicks, int refillRateTicks) {
        super(id, icon, color, requiredStones, maxChargeTicks, refillRateTicks);
    }

    @Override
    public boolean onStart(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        ItemStack gauntletStack = InfinityGauntletItem.findGauntlet(player);
        if (gauntletStack == null) return false;
        UUID gauntletId = InfinityGauntletItem.getOrCreateGauntletId(gauntletStack);
        int charge = GauntletChargeState.getCharge(gauntletId, getId(), getMaxChargeTicks());
        return charge >= getMaxChargeTicks();
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        Vector3f color = Vec3d.unpackRgb(
                InfinityForgeConfig.get().colorOptions.stoneGlintColors.powerStone).toVector3f();
        spawnRadialBurst(world, color, player.getPos(), 10, 5.0);

        ItemStack gauntletStack = InfinityGauntletItem.findGauntlet(player);
        if (gauntletStack == null) return;
        UUID gauntletId = InfinityGauntletItem.getOrCreateGauntletId(gauntletStack);
        int charge = GauntletChargeState.getCharge(gauntletId, getId(), getMaxChargeTicks());

        if (charge == 1) {
            spawnRadialBurst(world, color, player.getPos(), 500, 20.0);
            Vec3d pos = player.getPos();
            world.createExplosion(player, pos.x, pos.y, pos.z, 8.0F, World.ExplosionSourceType.TNT);
        }
    }

    @Override
    public void onStop(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {}

    private static void spawnRadialBurst(ServerWorld world, Vector3f color, Vec3d center, int count, double baseSpeed) {
        var random = world.random;
        for (int i = 0; i < count; i++) {
            double gx = random.nextGaussian();
            double gy = random.nextGaussian();
            double gz = random.nextGaussian();
            double len = Math.sqrt(gx * gx + gy * gy + gz * gz);
            double dx = gx / len;
            double dy = gy / len;
            double dz = gz / len;

            double speed = baseSpeed * (0.35 + random.nextDouble() * 1.35);
            double spawnJitter = 0.25;
            double sx = center.x + (random.nextDouble() - 0.5) * spawnJitter;
            double sy = center.y + (random.nextDouble() - 0.5) * spawnJitter;
            double sz = center.z + (random.nextDouble() - 0.5) * spawnJitter;

            ParticleEffect effect = new InfinityDustParticleEffect(
                    color, 0.75f + random.nextFloat() * 1.75f, true, true);
            world.spawnParticles(effect, sx, sy, sz, 0,
                    dx * speed, dy * speed, dz * speed, 1.0);
        }
    }
}
