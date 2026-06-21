package net.alvin.infinityforge.entity;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PortalEntity extends Entity {
    private double destX, destY, destZ;
    private RegistryKey<World> destWorld = World.OVERWORLD;
    @Nullable
    private UUID partnerId = null;
    private boolean closing = false;
    private static final TrackedData<Float> ANIMATION_PROGRESS =
            DataTracker.registerData(PortalEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final float ANIMATION_SPEED = 0.03f;
    private final Map<UUID, Integer> teleportCooldowns = new HashMap<>();

    public PortalEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public void setDestination(double x, double y, double z, RegistryKey<World> world) {
        this.destX = x;
        this.destY = y;
        this.destZ = z;
        this.destWorld = world;
    }

    public static void spawnLinkedPair(
            ServerWorld worldA, double ax, double ay, double az, float yawA, float pitchA,
            ServerWorld worldB, double bx, double by, double bz, float yawB, float pitchB
    ) {
        PortalEntity portalA = new PortalEntity(ModEntities.PORTAL_ENTITY, worldA);
        portalA.setPosition(ax, ay, az);
        portalA.setYaw(yawA);
        portalA.prevYaw = yawA;
        portalA.setPitch(pitchA);
        portalA.prevPitch = pitchA;

        PortalEntity portalB = new PortalEntity(ModEntities.PORTAL_ENTITY, worldB);
        portalB.setPosition(bx, by, bz);
        portalB.setYaw(yawB);
        portalB.prevYaw = yawB;
        portalB.setPitch(pitchB);
        portalB.prevPitch = pitchB;

        portalA.setDestination(bx, by, bz, worldB.getRegistryKey());
        portalA.setPartnerId(portalB.getUuid());

        portalB.setDestination(ax, ay, az, worldA.getRegistryKey());
        portalB.setPartnerId(portalA.getUuid());

        worldA.spawnEntity(portalA);
        worldB.spawnEntity(portalB);
    }

    public void setPartnerId(@Nullable UUID id) {
        this.partnerId = id;
    }

    @Nullable
    private PortalEntity findPartner(MinecraftServer server) {
        if (this.partnerId == null) return null;
        for (ServerWorld world : server.getWorlds()) {
            Entity e = world.getEntity(this.partnerId);
            if (e instanceof PortalEntity portal) return portal;
        }
        return null;
    }

    @Override
    public void tick() {
        super.tick();
        float progress = this.getAnimationProgress();

        if (this.getWorld().isClient()) {
            tickParticles(progress);
            return;
        }

        if (!closing) {
            if (progress < 1f) {
                this.dataTracker.set(ANIMATION_PROGRESS, Math.min(1f, progress + ANIMATION_SPEED));
                return;
            }
        } else {
            float next = progress - ANIMATION_SPEED;
            if (next <= 0f) {
                this.discard();
                return;
            }
            this.dataTracker.set(ANIMATION_PROGRESS, next);
            return;
        }

        this.teleportCooldowns.entrySet().removeIf(e -> {
            e.setValue(e.getValue() - 1);
            return e.getValue() <= 0;
        });

        ServerWorld serverWorld = (ServerWorld) this.getWorld();
        ServerWorld destination = serverWorld.getServer().getWorld(this.destWorld);
        if (destination == null) return;

        PortalEntity partner = findPartner(serverWorld.getServer());
        Box box = this.getBoundingBox();
        for (Entity e : serverWorld.getOtherEntities(this, box)) {
            if (!(e instanceof ServerPlayerEntity player)) continue;
            if (this.teleportCooldowns.containsKey(player.getUuid())) continue;

            this.teleportCooldowns.put(player.getUuid(), 80);
            if (partner != null) partner.teleportCooldowns.put(player.getUuid(), 80);

            Vec3d offset = getArrivalOffset(player);
            FabricDimensions.teleport(player, destination, new TeleportTarget(
                    new Vec3d(destX + offset.x, destY, destZ + offset.z),
                    Vec3d.ZERO,
                    player.getYaw(),
                    player.getPitch()
            ));
        }
    }

    private void tickParticles(float progress) {
        if (progress <= 0f) return;

        float a = progress;
        float b = 1.5f * progress;
        float cosYaw   = MathHelper.cos((float)Math.toRadians(-this.getYaw()));
        float sinYaw   = MathHelper.sin((float)Math.toRadians(-this.getYaw()));
        float cosPitch = MathHelper.cos((float)Math.toRadians(this.getPitch()));
        float sinPitch = MathHelper.sin((float)Math.toRadians(this.getPitch()));
        double cx = this.getX();
        double cy = this.getY() + 1.5;
        double cz = this.getZ();
        int count = 30;

        for (int i = 0; i < count; i++) {
            double angle = (2 * Math.PI * i / count) + (this.age * 0.03);
            float lx = (float)(a * Math.cos(angle));
            float ly = (float)(b * Math.sin(angle));
            float lz = 0.05f;
            float px = lx;
            float py = ly * cosPitch - lz * sinPitch;
            float pz = ly * sinPitch + lz * cosPitch;
            float wx = px * cosYaw + pz * sinYaw;
            float wy = py;
            float wz = -px * sinYaw + pz * cosYaw;

            this.getWorld().addParticle(ParticleTypes.PORTAL,
                    cx + wx,
                    cy + wy,
                    cz + wz,
                    wx * 0.01, wy * 0.01, wz * 0.01
            );
        }
    }

    private Vec3d getArrivalOffset(ServerPlayerEntity player) {
        Vec3d velocity = player.getVelocity();
        double horizontalSpeed = Math.sqrt(velocity.x * velocity.x + velocity.z * velocity.z);

        Vec3d direction;
        if (horizontalSpeed > 0.001) {
            direction = new Vec3d(velocity.x, 0, velocity.z).normalize();
        } else {
            Vec3d look = player.getRotationVec(1.0f);
            direction = new Vec3d(look.x, 0, look.z).normalize();
        }
        return direction.multiply(1.5);
    }

    @Override
    public boolean canHit() {
        return true;
    }

    @Override
    public boolean canBeHitByProjectile() {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.getWorld().isClient()) return false;
        if (this.closing) return false;
        Entity attacker = source.getAttacker();
        if (!(attacker instanceof PlayerEntity player)) return false;
        if (InfinityGauntletItem.findGauntlet(player) != null) {
            this.closing = true;
            InfinityForge.LOGGER.info("Damage called for PortalEntity!");

            if (this.getWorld() instanceof ServerWorld sw) {
                PortalEntity partner = findPartner(sw.getServer());
                if (partner != null) partner.closing = true;
            }
        }
        return true;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ANIMATION_PROGRESS, 0f);
    }

    public float getAnimationProgress() {
        return this.dataTracker.get(ANIMATION_PROGRESS);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        this.destX = nbt.getDouble("DestX");
        this.destY = nbt.getDouble("DestY");
        this.destZ = nbt.getDouble("DestZ");
        this.destWorld = RegistryKey.of(
                RegistryKeys.WORLD,
                new Identifier(nbt.getString("DestWorld"))
        );
        this.closing = nbt.getBoolean("Closing");
        this.dataTracker.set(ANIMATION_PROGRESS, nbt.getFloat("AnimationProgress"));
        if (nbt.containsUuid("PartnerId")) this.partnerId = nbt.getUuid("PartnerId");
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putDouble("DestX", this.destX);
        nbt.putDouble("DestY", this.destY);
        nbt.putDouble("DestZ", this.destZ);
        nbt.putString("DestWorld", this.destWorld.getValue().toString());
        nbt.putBoolean("Closing", this.closing);
        nbt.putFloat("AnimationProgress", this.getAnimationProgress());
        if (this.partnerId != null) nbt.putUuid("PartnerId", this.partnerId);
    }
}
