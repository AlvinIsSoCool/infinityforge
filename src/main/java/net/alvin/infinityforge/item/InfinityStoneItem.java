package net.alvin.infinityforge.item;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class InfinityStoneItem extends Item {
    private final InfinityStoneType stoneType;

    public InfinityStoneItem(InfinityStoneType stoneType) {
        super(new FabricItemSettings().maxDamage(0).fireproof());
        this.stoneType = stoneType;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        return this.stoneType.getUseAbility().onUse(world, user, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        this.stoneType.getHoldAbility().onHold(stack, world, entity, slot, selected);
    }
}
