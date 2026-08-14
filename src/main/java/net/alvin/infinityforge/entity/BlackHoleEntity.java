package net.alvin.infinityforge.entity;

import net.alvin.infinityforge.infinity.abilities.impl.ModGauntletAbilities;
import net.alvin.infinityforge.registry.ModDamageSources;
import net.alvin.infinityforge.registry.ModTags;
import net.alvin.infinityforge.server.event.InfinityStoneEventHandler;
import net.alvin.infinityforge.server.state.GauntletHeldState;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.UUID;

public class BlackHoleEntity extends Entity {
    private static final TrackedData<Float> RADIUS =
            DataTracker.registerData(BlackHoleEntity.class, TrackedDataHandlerRegistry.FLOAT);
    public static final float MAX_RADIUS = 2.5f;
    private static final float GROW_SPEED = 0.01f;
    private static final float SHRINK_SPEED = 0.02f;
    private static final double EYE_OFFSET = 4.0;
    private static final double LAUNCH_SPEED = 1.0;

    private boolean shrinking = false;
    private boolean launched = false;
    private UUID ownerUuid;

    public BlackHoleEntity(EntityType<?> type, World world) {
        super(type, world);
        this.noClip = true;
        this.setNoGravity(true);
    }

    public void setOwner(UUID ownerUuid) {
        this.ownerUuid = ownerUuid;
    }

    private void updateOwnerLock() {
        if (this.launched) return;
        if (this.ownerUuid == null) return;

        PlayerEntity owner = this.getWorld().getPlayerByUuid(this.ownerUuid);
        if (owner == null || owner.isRemoved() || owner.isDead()) {
            launch(new Vec3d(0, -1, 0));
            return;
        }

        boolean stillHeld = GauntletHeldState.isHeld(owner, ModGauntletAbilities.BLACKHOLE.getId());
        Vec3d lookVec = owner.getRotationVec(1.0f);

        if (!stillHeld) {
            launch(lookVec);
            return;
        }

        Vec3d desiredCenter = owner.getEyePos().add(lookVec.multiply(EYE_OFFSET));
        this.setPosition(desiredCenter.x, desiredCenter.y - this.getRadius(), desiredCenter.z);
    }

    private void launch(Vec3d direction) {
        this.launched = true;
        this.startShrinking();
        Vec3d dir = direction.lengthSquared() > 1.0E-4 ? direction.normalize() : new Vec3d(0, -1, 0);
        this.setVelocity(dir.multiply(LAUNCH_SPEED));
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(RADIUS, 0f);
    }

    public float getRadius() {
        return this.dataTracker.get(RADIUS);
    }

    public void startShrinking() {
        this.shrinking = true;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.dataTracker.set(RADIUS, nbt.getFloat("Radius"));
        this.shrinking = nbt.getBoolean("Shrinking");
        this.launched = nbt.getBoolean("Launched");
        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putFloat("Radius", this.getRadius());
        nbt.putBoolean("Shrinking", this.shrinking);
        nbt.putBoolean("Launched", this.launched);
        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        return false;
    }

    @Override
    public EntityDimensions getDimensions(EntityPose pose) {
        float size = Math.max(this.getRadius() * 2f, 0.1f);
        return EntityDimensions.fixed(size, size);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (RADIUS.equals(data)) this.calculateDimensions();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            tickParticles();
            return;
        }

        updateOwnerLock();
        if (this.launched) tickLaunchedMovement();
        consumeArea();

        float radius = this.getRadius();
        if (!this.shrinking) {
            if (radius < MAX_RADIUS) {
                this.dataTracker.set(RADIUS, Math.min(MAX_RADIUS, radius + GROW_SPEED));
            }
        } else {
            float next = radius - SHRINK_SPEED;
            if (next <= 0f) {
                this.discard();
                return;
            }
            this.dataTracker.set(RADIUS, next);
        }
    }

    private void tickParticles() {
        float radius = this.getRadius();
        if (radius <= 0f) return;

        World world = this.getWorld();
        int count = 15;

        for (int i = 0; i < count; i++) {
            double theta = this.random.nextDouble() * Math.PI;
            double phi = this.random.nextDouble() * 2 * Math.PI;
            double spawnRadius = radius * 1.5;

            double lx = spawnRadius * Math.sin(theta) * Math.cos(phi);
            double ly = spawnRadius * Math.cos(theta);
            double lz = spawnRadius * Math.sin(theta) * Math.sin(phi);

            double px = this.getX() + lx;
            double py = this.getY() + radius + ly;
            double pz = this.getZ() + lz;

            double vx = -lx * 0.02;
            double vy = -ly * 0.02;
            double vz = -lz * 0.02;

            world.addParticle(ParticleTypes.ASH, px, py, pz, vx, vy, vz);
        }
    }

    private void tickLaunchedMovement() {
        this.move(MovementType.SELF, this.getVelocity());
    }

    private void consumeArea() {
        float radius = this.getRadius();
        float scaledRadius = radius * 1.25f;
        if (radius < 0.25f) return;

        Vec3d center = this.getPos().add(0, radius, 0);
        ServerWorld world = (ServerWorld) this.getWorld();

        Box entityBox = new Box(
                center.x - radius, center.y - radius, center.z - radius,
                center.x + radius, center.y + radius, center.z + radius
        );
        for (Entity e : world.getOtherEntities(this, entityBox)) {
            if (e instanceof ItemEntity ie && !ie.getStack().isIn(ModTags.Items.INFINITY_ITEMS)) {
                ie.discard();
                continue;
            }

            if (!(e instanceof LivingEntity living) || !living.isAlive()) continue;
            if (e.getUuid().equals(this.ownerUuid)) continue;
            if (living.getPos().distanceTo(center) > radius) continue;

            InfinityStoneEventHandler.applyDamageInfinity(living,
                    ModDamageSources.blackHole(living.getWorld()), true);
        }

        BlockPos min = BlockPos.ofFloored(center.x - scaledRadius, center.y - scaledRadius,
                center.z - scaledRadius);
        BlockPos max = BlockPos.ofFloored(center.x + scaledRadius, center.y + scaledRadius,
                center.z + scaledRadius);
        for (BlockPos pos : BlockPos.iterate(min, max)) {
            BlockState state = world.getBlockState(pos);
            if (state.isAir()) continue;
            if (Vec3d.ofCenter(pos).distanceTo(center) > scaledRadius) continue;
            world.breakBlock(pos, false);
        }
    }
}
