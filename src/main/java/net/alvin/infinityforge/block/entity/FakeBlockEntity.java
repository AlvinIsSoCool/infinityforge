package net.alvin.infinityforge.block.entity;

import net.alvin.infinityforge.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class FakeBlockEntity extends BlockEntity {
    public static final String DISGUISE_KEY = "disguise_id";
    private static final Map<FakeBlockEntity, Long> CREATION_TIME_CACHE = new WeakHashMap<>();
    private String disguiseId = null;

    public FakeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FAKE_BLOCK_ENTITY, pos, state);
    }

    public static void place(World world, BlockPos pos, Block disguise) {
        world.setBlockState(pos, ModBlocks.FAKE_BLOCK.getDefaultState());
        if (world.getBlockEntity(pos) instanceof FakeBlockEntity be) {
            be.disguiseId = Registries.BLOCK.getId(disguise).toString();
            be.markDirty();
        }
    }

    public BlockState getDisguiseState() {
        if (disguiseId == null) return null;
        Block block = Registries.BLOCK.get(new Identifier(disguiseId));
        return block == Blocks.AIR ? null : block.getDefaultState();
    }

    public static void tick(World world, BlockPos pos, BlockState state, FakeBlockEntity be) {
        if (world.isClient()) return;

        Long cached = CREATION_TIME_CACHE.get(be);
        long created;
        if (cached == null) {
            created = world.getTime();
            CREATION_TIME_CACHE.put(be, created);
        } else {
            created = cached;
        }

        if (world.getTime() - created >= 200) {
            world.removeBlock(pos, false);

            ServerWorld serverWorld = (ServerWorld) world;
            double cx = pos.getX() + 0.5;
            double cy = pos.getY() + 0.5;
            double cz = pos.getZ() + 0.5;
            serverWorld.spawnParticles(ParticleTypes.BUBBLE_POP, cx, cy, cz, 50, 0.3, 0.4, 0.3, 0.005);
            serverWorld.spawnParticles(ParticleTypes.BUBBLE_POP, cx, cy, cz, 50, 0.5, 0.6, 0.5, 0.025);
            /*ParticleEffect effect = new DustParticleEffect(new Vector3f(1f, 0f, 0f), 0.5f);
            serverWorld.spawnParticles(effect, cx, cy, cz,
                    50, 0.3, 0.4, 0.3, 0.005
            );
            serverWorld.spawnParticles(effect, cx, cy, cz,
                    50, 0.5, 0.6, 0.5, 0.025
            ); */
            CREATION_TIME_CACHE.remove(be);
        }
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        if (nbt.contains(DISGUISE_KEY)) disguiseId = nbt.getString(DISGUISE_KEY);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (disguiseId != null) nbt.putString(DISGUISE_KEY, disguiseId);
    }
}
