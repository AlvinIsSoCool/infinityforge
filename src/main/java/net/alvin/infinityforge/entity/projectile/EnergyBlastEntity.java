package net.alvin.infinityforge.entity.projectile;

import net.alvin.infinityforge.entity.ModEntities;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.ModStones;
import net.alvin.infinityforge.registry.ModDamageSources;
import net.alvin.infinityforge.server.event.InfinityStoneEventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EnergyBlastEntity extends ProjectileEntity {
    private Vec3d direction = Vec3d.ZERO;
    private InfinityStoneType stoneType = ModStones.POWER;

    public EnergyBlastEntity(EntityType<? extends EnergyBlastEntity> type, World world) {
        super(type, world);
    }

    public EnergyBlastEntity(World world, LivingEntity owner) {
        super(ModEntities.ENERGY_BLAST, world);
        setOwner(owner);
        setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
    }

    public EnergyBlastEntity(World world, LivingEntity owner, InfinityStoneType stoneType) {
        this(world, owner);
        this.stoneType = stoneType;
    }

    @Override
    protected void initDataTracker() {}

    @Override
    public void tick() {
        super.tick();

        HitResult hit = ProjectileUtil.getCollision(this, this::canHit);
        if (hit.getType() != HitResult.Type.MISS) onCollision(hit);

        Vec3d vel = getVelocity();
        Vec3d newPos = getPos().add(vel);
        updateRotation();
        setPosition(newPos.x, newPos.y, newPos.z);

        setVelocity(vel.multiply(0.995));
        if (age > 100) discard();
    }

    @Override
    public Box calculateBoundingBox() {
        EntityDimensions dims = getDimensions(getPose());
        double halfWidth = dims.width / 2.0;
        double halfHeight = dims.height / 2.0;
        Vec3d pos = getPos();
        return new Box(
                pos.x - halfWidth,  pos.y - halfHeight, pos.z - halfWidth,
                pos.x + halfWidth,  pos.y + halfHeight, pos.z + halfWidth
        );
    }

    @Override
    protected boolean canHit(Entity entity) {
        return super.canHit(entity) && entity != getOwner();
    }

    @Override
    protected void onEntityHit(EntityHitResult hitResult) {
        super.onEntityHit(hitResult);
        if (!getWorld().isClient) {
            Entity entity = hitResult.getEntity();
            if (entity instanceof LivingEntity target) {
                InfinityStoneEventHandler.applyDamageInfinity(target,
                        ModDamageSources.powerStone(target.getWorld()),
                        true);
                discard();
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult hitResult) {
        super.onBlockHit(hitResult);
        if (!getWorld().isClient) discard();
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public void setVelocity(double x, double y, double z) {
        super.setVelocity(x, y, z);
        double lenSq = x * x + y * y + z * z;
        if (lenSq > 1.0e-7) this.direction = new Vec3d(x, y, z).normalize();
    }

    public Vec3d getDirection() {
        return direction;
    }
    public InfinityStoneType getStoneType() { return stoneType; }
}
