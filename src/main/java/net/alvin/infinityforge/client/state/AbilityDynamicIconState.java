package net.alvin.infinityforge.client.state;

import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class AbilityDynamicIconState {
    private static final Map<Identifier, ItemStack> ICONS = new HashMap<>();

    public static ItemStack get(Identifier id) { return ICONS.getOrDefault(id, ItemStack.EMPTY); }
    public static void put(Identifier id, ItemStack stack) { ICONS.put(id, stack); }
    public static void clear() { ICONS.clear(); }
}
