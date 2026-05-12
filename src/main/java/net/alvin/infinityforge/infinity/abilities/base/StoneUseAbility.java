package net.alvin.infinityforge.infinity.abilities.base;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@FunctionalInterface
public interface StoneUseAbility {
    TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand);
}
