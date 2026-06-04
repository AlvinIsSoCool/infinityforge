package net.alvin.infinityforge.infinity.abilities.base;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface StoneHoldAbility {
    /**
     * Called from {@code Item.inventoryTick(ItemStack, World, Entity, int, boolean)}
     * override in the InfinityStoneItem class.
     * @see Item#inventoryTick(ItemStack, World, Entity, int, boolean)
     */
    void onHold(ItemStack stack, World world, Entity entity, int slot, boolean selected);
}
