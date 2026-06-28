package net.alvin.infinityforge.client.state;

import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientKnownDimensionsState {
    private static final Set<Identifier> KNOWN_DIMENSIONS = new HashSet<>();

    public static void set(List<Identifier> dims) {
        KNOWN_DIMENSIONS.clear();
        KNOWN_DIMENSIONS.addAll(dims);
    }

    public static boolean exists(Identifier id) {
        return KNOWN_DIMENSIONS.contains(id);
    }
}
