package net.alvin.infinityforge.world.data;

import net.alvin.infinityforge.InfinityForge;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.PersistentState;

import java.util.ArrayList;
import java.util.List;

public class SnappedEntitiesState extends PersistentState {
    public static final String KEY = InfinityForge.MOD_ID + "_snapped_entities";
    private final List<SnappedEntry> entries = new ArrayList<>();

    public record SnappedEntry(
            String dimensionId,
            Identifier entityType,
            double x, double y, double z
    ) {}

    public void addEntry(String dimensionId, Identifier entityTypeId, Vec3d pos) {
        entries.add(new SnappedEntry(dimensionId, entityTypeId, pos.getX(), pos.getY(), pos.getZ()));
        markDirty();
    }

    public List<SnappedEntry> popAll() {
        List<SnappedEntry> copy = new ArrayList<>(entries);
        entries.clear();
        markDirty();
        return copy;
    }

    public List<SnappedEntry> getEntries() {
        return entries;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtList list = new NbtList();
        for (SnappedEntry entry : entries) {
            NbtCompound entryNbt = new NbtCompound();
            entryNbt.putString("DimensionId", entry.dimensionId());
            entryNbt.putString("EntityType", entry.entityType().toString());
            entryNbt.putDouble("X", entry.x());
            entryNbt.putDouble("Y", entry.y());
            entryNbt.putDouble("Z", entry.z());
            list.add(entryNbt);
        }
        nbt.put("Entries", list);
        return nbt;
    }

    public static SnappedEntitiesState fromNbt(NbtCompound nbt) {
        SnappedEntitiesState state = new SnappedEntitiesState();
        NbtList list = nbt.getList("Entries", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size(); i++) {
            NbtCompound entryNbt = list.getCompound(i);
            String dim = entryNbt.getString("DimensionId");
            Identifier typeId = Identifier.tryParse(entryNbt.getString("EntityType"));
            if (typeId == null) continue;
            double x = entryNbt.getDouble("X");
            double y = entryNbt.getDouble("Y");
            double z = entryNbt.getDouble("Z");
            state.entries.add(new SnappedEntry(dim, typeId, x, y, z));
        }
        return state;
    }

    public static SnappedEntitiesState get(ServerWorld world) {
        return world.getServer().getOverworld().getPersistentStateManager()
                .getOrCreate(SnappedEntitiesState::fromNbt, SnappedEntitiesState::new, KEY);
    }
}