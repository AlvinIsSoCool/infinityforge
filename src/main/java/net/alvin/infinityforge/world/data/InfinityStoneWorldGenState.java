package net.alvin.infinityforge.world.data;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.registry.InfinityStoneTypeRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InfinityStoneWorldGenState extends PersistentState {
    public static final String KEY = InfinityForge.MOD_ID + "_stone_tracker";
    public record SpawnRecord(Identifier stoneId, BlockPos pos, Identifier dimension, long timestamp) {
        public NbtCompound toNbt() {
            NbtCompound nbt = new NbtCompound();
            nbt.putString("stone", stoneId.toString());
            nbt.putInt("x", pos.getX());
            nbt.putInt("y", pos.getY());
            nbt.putInt("z", pos.getZ());
            nbt.putString("dimension", dimension.toString());
            nbt.putLong("timestamp", timestamp);
            return nbt;
        }

        public static SpawnRecord fromNbt(NbtCompound nbt) {
            return new SpawnRecord(
                    new Identifier(nbt.getString("stone")),
                    new BlockPos(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z")),
                    new Identifier(nbt.getString("dimension")),
                    nbt.getLong("timestamp")
            );
        }
    }

    private final List<SpawnRecord> spawnRecords = new ArrayList<>();

    public void recordSpawn(InfinityStoneType stone, BlockPos pos, RegistryKey<World> dimension, long worldTime) {
        spawnRecords.add(new SpawnRecord(
                InfinityStoneTypeRegistry.REGISTRY.getId(stone),
                pos,
                dimension.getValue(),
                worldTime
        ));
        markDirty();
    }

    public List<BlockPos> getSpawnedPositions(InfinityStoneType stone) {
        Identifier id = InfinityStoneTypeRegistry.REGISTRY.getId(stone);
        return getSpawnedPositions(id);
    }

    public List<BlockPos> getSpawnedPositions(Identifier stoneId) {
        return spawnRecords.stream()
                .filter(r -> r.stoneId().equals(stoneId))
                .map(SpawnRecord::pos)
                .toList();
    }

    public List<Map.Entry<BlockPos, Identifier>> getSpawnedPositionsWithDimId(InfinityStoneType stone) {
        Identifier id = InfinityStoneTypeRegistry.REGISTRY.getId(stone);
        return getSpawnedPositionsWithDimId(id);
    }

    public List<Map.Entry<BlockPos, Identifier>> getSpawnedPositionsWithDimId(Identifier stoneId) {
        return spawnRecords.stream()
                .filter(r -> r.stoneId().equals(stoneId))
                .map(r -> Map.entry(r.pos(), r.dimension()))
                .toList();
    }

    public List<BlockPos> getAllSpawnedPositions() {
        return spawnRecords.stream()
                .map(SpawnRecord::pos)
                .toList();
    }

    public List<Map.Entry<BlockPos, Identifier>> getAllSpawnedPositionsWithDimId() {
        return spawnRecords.stream()
                .map(r -> Map.entry(r.pos(), r.dimension()))
                .toList();
    }

    public boolean hasBeenSpawned(InfinityStoneType stone) {
        Identifier id = InfinityStoneTypeRegistry.REGISTRY.getId(stone);
        return hasBeenSpawned(id);
    }

    public boolean hasBeenSpawned(Identifier stoneId) {
        return spawnRecords.stream().anyMatch(r -> r.stoneId().equals(stoneId));
    }

    // TODO: Add config value for number of infinity stone sets to spawn.
    public boolean canSpawn(InfinityStoneType chosenStone) {
        int maxSets = 1; // -1 for unlimited.
        if (maxSets == -1) return true;

        Identifier id = InfinityStoneTypeRegistry.REGISTRY.getId(chosenStone);
        long stoneCount = spawnRecords.stream()
                .filter(r -> r.stoneId().equals(id))
                .count();

        return stoneCount < maxSets;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList list = new NbtList();
        for (SpawnRecord record : spawnRecords) {
            list.add(record.toNbt());
        }
        nbt.put("records", list);
        return nbt;
    }

    public static InfinityStoneWorldGenState readNbt(NbtCompound nbt) {
        InfinityStoneWorldGenState state = new InfinityStoneWorldGenState();
        NbtList list = nbt.getList("records", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size(); i++) {
            state.spawnRecords.add(SpawnRecord.fromNbt(list.getCompound(i)));
        }
        return state;
    }

    public static InfinityStoneWorldGenState get(ServerWorld world) {
        return world.getServer().getOverworld().getPersistentStateManager()
                .getOrCreate(InfinityStoneWorldGenState::readNbt, InfinityStoneWorldGenState::new, KEY);
    }
}
