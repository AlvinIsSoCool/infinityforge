package net.alvin.infinityforge.entity;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.ModStones;
import net.alvin.infinityforge.infinity.abilities.impl.ModGauntletAbilities;
import net.alvin.infinityforge.server.event.InfinityStoneEventHandler;
import net.alvin.infinityforge.server.state.GauntletHeldState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class EnergyBeamEntity extends Entity {
    private static final TrackedData<Float> DISTANCE =
            DataTracker.registerData(EnergyBeamEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final float MAX_RANGE = 20.0f;
    private static final float EXPLOSION_POWER = 2.0f;
    private static final int EXPLOSION_INTERVAL = 4;
    private static final boolean CREATE_FIRE = true;

    private PlayerEntity owner;
    private int explosionCooldown = 0;
    private InfinityStoneType stoneType = ModStones.POWER;

    public EnergyBeamEntity(EntityType<? extends EnergyBeamEntity> type, World world) {
        super(type, world);
        this.ignoreCameraFrustum = true;
    }

    public EnergyBeamEntity(World world, PlayerEntity owner) {
        this(ModEntities.ENERGY_BEAM, world);
        this.owner = owner;
        setPosition(owner.getEyePos().x, owner.getEyePos().y, owner.getEyePos().z);
    }

    public EnergyBeamEntity(World world, PlayerEntity owner, InfinityStoneType stoneType) {
        this(world, owner);
        this.stoneType = stoneType;
    }

    @Override
    protected void initDataTracker() {
        dataTracker.startTracking(DISTANCE, MAX_RANGE);
    }

    @Override
    public void tick() {
        super.tick();
        if (getWorld().isClient) return;

        if (owner == null || !owner.isAlive() || owner.isRemoved()
                || !GauntletHeldState.isHeld(owner, ModGauntletAbilities.ENERGY_BEAM.getId())) {
            discard();
            return;
        }

        Vec3d eyePos = owner.getEyePos();
        Vec3d look = owner.getRotationVec(1.0f);
        Vec3d endPoint = eyePos.add(look.multiply(MAX_RANGE));

        setPosition(eyePos.x, eyePos.y, eyePos.z);
        setRotation(owner.getYaw(), owner.getPitch());

        BlockHitResult blockHit = getWorld().raycast(new RaycastContext(
                eyePos, endPoint,
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                owner
        ));

        EntityHitResult entityHit = ProjectileUtil.raycast(
                owner, eyePos, endPoint,
                new Box(eyePos, endPoint).expand(1.0),
                e -> !e.isSpectator() && e.canHit() && e != owner,
                MAX_RANGE * MAX_RANGE
        );

        double blockDist = blockHit.getType() != HitResult.Type.MISS
                ? eyePos.distanceTo(blockHit.getPos()) : MAX_RANGE;
        double entityDist = entityHit != null
                ? eyePos.distanceTo(entityHit.getPos()) : MAX_RANGE;
        double finalDist = Math.min(blockDist, entityDist);
        dataTracker.set(DISTANCE, (float) finalDist);

        Vec3d hitPoint = eyePos.add(look.multiply(finalDist));
        if (entityHit != null && entityHit.getEntity() instanceof LivingEntity target
                && entityDist <= blockDist) {
            InfinityStoneEventHandler.applyDamageInfinity(target,
                    getDamageSources().explosion(target, owner), true);
        }
        if (explosionCooldown-- <= 0) {
            getWorld().createExplosion(
                    owner, hitPoint.x, hitPoint.y, hitPoint.z,
                    EXPLOSION_POWER, CREATE_FIRE,
                    World.ExplosionSourceType.TNT);
            explosionCooldown = EXPLOSION_INTERVAL;
        }
    }

    @Override
    public boolean shouldRender(double distance) {
        double maxRenderDistance = MAX_RANGE + 16.0;
        return distance < maxRenderDistance * maxRenderDistance;
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {}

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {}

    public float getDistance() { return dataTracker.get(DISTANCE); }
    public InfinityStoneType getStoneType() { return stoneType; }
}