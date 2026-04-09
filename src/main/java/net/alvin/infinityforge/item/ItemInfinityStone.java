package net.alvin.infinityforge.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemInfinityStone extends Item {
    public ItemInfinityStone() {
        super(new FabricItemSettings().maxDamage(0).fireproof());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        player.playSound(Blocks.AMETHYST_BLOCK.getSoundGroup(Blocks.AMETHYST_BLOCK.getDefaultState()).getBreakSound(), 1.0F, 1.0F);
        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
