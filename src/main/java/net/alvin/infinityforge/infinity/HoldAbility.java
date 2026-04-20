package net.alvin.infinityforge.infinity;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface HoldAbility {
    void onHold(ItemStack stack, World world, Entity entity, int slot, boolean selected);
}
