package net.alvin.infinityforge.client.state;

import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class GauntletClientState {
    public static int scrollOffset = 0;
    public static final Set<Identifier> activeToggles = new HashSet<>();
    public static final Set<Identifier> heldActive = new HashSet<>();

    public static void scroll(int totalAbilities, int delta) {
        int max = Math.max(0, totalAbilities - 6);
        scrollOffset = Math.min(Math.max(scrollOffset + delta, 0), max);
    }
}
