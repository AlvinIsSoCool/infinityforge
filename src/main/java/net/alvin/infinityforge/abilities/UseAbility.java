package net.alvin.infinityforge.abilities;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

@FunctionalInterface
public interface UseAbility {
    TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand);
}
