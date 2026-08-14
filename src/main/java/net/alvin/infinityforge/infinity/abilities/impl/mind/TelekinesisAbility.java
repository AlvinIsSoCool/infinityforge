package net.alvin.infinityforge.infinity.abilities.impl.mind;

import net.alvin.infinityforge.config.client.InfinityForgeClientConfig;
import net.alvin.infinityforge.entity.GrabbedBlockEntity;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.base.ThrowOnAttack;
import net.alvin.infinityforge.infinity.abilities.icon.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.AbilityState;
import net.alvin.infinityforge.infinity.abilities.base.HeldAbility;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.particle.InfinityDustParticleEffect;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import java.util.List;
import java.util.function.Supplier;

public class TelekinesisAbility extends HeldAbility implements AbilityState<Entity>, ThrowOnAttack {
    private static final double ABILITY_REACH = 5.0;
    private static final double HOLD_DISTANCE = 4.0;
    private static final double PULL_STRENGTH = 1.25;
    private static final double THROW_STRENGTH = 2.0;
    private static final double PARTICLE_SPACING = 0.4;

    public TelekinesisAbility(Identifier id, AbilityIcon icon, Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones, int maxChargeTicks, int refillRateTicks) {
        super(id, icon, color, requiredStones, maxChargeTicks, refillRateTicks);
    }

    @Override
    public boolean onStart(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        Entity entityTarget = raycastEntity(player);
        if (entityTarget != null) {
            setState(player, entityTarget);
            return true;
        }

        BlockHitResult blockHit = raycastBlock(world, player);
        if (blockHit == null || blockHit.getType() != HitResult.Type.BLOCK) return false;

        BlockPos pos = blockHit.getBlockPos();
        BlockState state = world.getBlockState(pos);
        if (!isGrabbable(world, pos, state)) return false;

        GrabbedBlockEntity grabbedBlock = GrabbedBlockEntity.spawnFromBlock(world, pos, state);
        setState(player, grabbedBlock);
        return true;
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        Entity grabbed = getState(player);
        if (grabbed == null || grabbed.isRemoved() || grabbed.getWorld() != world) {
            setState(player, null);
            return;
        }

        Vec3d targetPos = player.getCameraPosVec(1.0f)
                .add(player.getRotationVec(1.0f).multiply(HOLD_DISTANCE));
        Vec3d delta = targetPos.subtract(grabbed.getPos());

        if (grabbed instanceof ServerPlayerEntity grabbedPlayer) {
            grabbedPlayer.networkHandler.requestTeleport(targetPos.x, targetPos.y, targetPos.z,
                    grabbedPlayer.getYaw(), grabbedPlayer.getPitch());
        } else {
            grabbed.setVelocity(delta.multiply(PULL_STRENGTH));
            grabbed.velocityModified = true;
            grabbed.fallDistance = 0.0f;
        }
        spawnBeamParticles(world, player, grabbed);
    }

    @Override
    public void onStop(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        release(player);
    }

    @Override
    public void onThrow(ServerWorld world, ServerPlayerEntity player) {
        Entity grabbed = getState(player);
        if (grabbed == null) return;

        Vec3d throwVelocity = player.getRotationVec(1.0f).multiply(THROW_STRENGTH);
        if (grabbed instanceof GrabbedBlockEntity grabbedBlock) {
            grabbedBlock.drop();
            grabbedBlock.setVelocity(throwVelocity);
            grabbedBlock.velocityModified = true;
        } else if (grabbed instanceof ServerPlayerEntity grabbedPlayer) {
            grabbedPlayer.setVelocity(throwVelocity);
            grabbedPlayer.velocityModified = true;
            grabbedPlayer.networkHandler.sendPacket(new EntityVelocityUpdateS2CPacket(grabbedPlayer));
        } else {
            grabbed.setVelocity(throwVelocity);
            grabbed.velocityModified = true;
        }
        setState(player, null);
        this.forceStop(world, player, InfinityGauntletItem.getAddedStones(
                InfinityGauntletItem.findGauntlet(player)));
    }

    @Override
    public Class<Entity> getType() {
        return Entity.class;
    }

    private void release(PlayerEntity player) {
        Entity grabbed = getState(player);
        if (grabbed instanceof GrabbedBlockEntity grabbedBlock)
            grabbedBlock.drop();
        setState(player, null);
    }

    private Entity raycastEntity(ServerPlayerEntity player) {
        Vec3d start = player.getCameraPosVec(1.0f);
        Vec3d look = player.getRotationVec(1.0f);
        Vec3d end = start.add(look.multiply(ABILITY_REACH));

        Box searchBox = player.getBoundingBox().stretch(look.multiply(ABILITY_REACH)).expand(1.0);
        EntityHitResult hit = ProjectileUtil.raycast(
                player, start, end, searchBox,
                e -> e.canHit() && !e.isSpectator() && !(e instanceof PlayerEntity),
                ABILITY_REACH * ABILITY_REACH
        );
        return hit != null ? hit.getEntity() : null;
    }

    private BlockHitResult raycastBlock(ServerWorld world, ServerPlayerEntity player) {
        Vec3d start = player.getCameraPosVec(1.0f);
        Vec3d end = start.add(player.getRotationVec(1.0f).multiply(ABILITY_REACH));
        RaycastContext ctx = new RaycastContext(
                start, end,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                player
        );
        return world.raycast(ctx);
    }

    private boolean isGrabbable(ServerWorld world, BlockPos pos, BlockState state) {
        if (state.isAir() || !state.getFluidState().isEmpty()) return false;
        return world.getBlockEntity(pos) == null;
    }

    private void spawnBeamParticles(ServerWorld world, ServerPlayerEntity player, Entity grabbed) {
        Vec3d start = player.getCameraPosVec(1.0f);
        Vec3d end = grabbed instanceof GrabbedBlockEntity
                ? grabbed.getBoundingBox().getCenter()
                : grabbed.getPos().add(0, grabbed.getHeight() / 2.0, 0);

        double distance = start.distanceTo(end);
        int points = Math.max(1, (int) (distance / PARTICLE_SPACING));
        ParticleEffect effect = new InfinityDustParticleEffect(Vec3d.unpackRgb(
                InfinityForgeClientConfig.get().stoneGlintColors.mindStone).toVector3f(),
                0.325f + world.random.nextFloat() * 0.4f, true, false);

        for (int i = 0; i <= points; i++) {
            double t = (double) i / points;
            Vec3d point = start.lerp(end, t);
            world.spawnParticles(effect, point.x, point.y, point.z, 1, 0.0, 0.0, 0.0, 0.0);
        }
    }
}
