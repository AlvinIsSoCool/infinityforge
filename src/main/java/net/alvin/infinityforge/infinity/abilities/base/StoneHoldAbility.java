package net.alvin.infinityforge.infinity.abilities.base;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

@FunctionalInterface
public interface StoneHoldAbility {
    void onHold(ItemStack stack, World world, Entity entity, int slot, boolean selected);
}
