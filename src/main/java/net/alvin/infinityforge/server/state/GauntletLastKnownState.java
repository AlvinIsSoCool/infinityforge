package net.alvin.infinityforge.server.state;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.*;

public class GauntletLastKnownState {
    private static final Set<PlayerEntity> PREVIOUSLY_EQUIPPED
            = Collections.newSetFromMap(new IdentityHashMap<>());
    private static final Map<PlayerEntity, ItemStack> LAST_KNOWN_STACK
            = new IdentityHashMap<>();
    private static final Map<PlayerEntity, List<InfinityStoneType>> LAST_KNOWN_STONES
            = new IdentityHashMap<>();

    public static boolean wasEquipped(PlayerEntity player) {
        return PREVIOUSLY_EQUIPPED.contains(player);
    }

    public static void setEquipped(PlayerEntity player, boolean equipped, ItemStack stack) {
        if (equipped) {
            PREVIOUSLY_EQUIPPED.add(player);
            LAST_KNOWN_STACK.put(player, stack);
        } else {
            PREVIOUSLY_EQUIPPED.remove(player);
            LAST_KNOWN_STACK.remove(player);
        }
    }

    public static ItemStack getLastKnownStack(PlayerEntity player) {
        return LAST_KNOWN_STACK.get(player);
    }

    public static List<InfinityStoneType> getLastKnownStones(PlayerEntity player) {
        return LAST_KNOWN_STONES.getOrDefault(player, List.of());
    }

    public static void setLastKnownStones(PlayerEntity player, List<InfinityStoneType> stones) {
        LAST_KNOWN_STONES.put(player, stones);
    }

    public static void clearPlayer(PlayerEntity player) {
        PREVIOUSLY_EQUIPPED.remove(player);
        LAST_KNOWN_STACK.remove(player);
    }
}
