package net.alvin.infinityforge.client.screen;

import net.alvin.infinityforge.infinity.InfinityStoneItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class InfinityStoneSlot extends Slot {
    private final InfinityStoneType acceptedType;

    public InfinityStoneSlot(Inventory inventory, int index, int x, int y, InfinityStoneType acceptedType) {
        super(inventory, index, x, y);
        this.acceptedType = acceptedType;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof InfinityStoneItem stoneItem
                && stoneItem.getStoneType() == acceptedType;
    }

    @Override
    public int getMaxItemCount() { return 1; }
}
