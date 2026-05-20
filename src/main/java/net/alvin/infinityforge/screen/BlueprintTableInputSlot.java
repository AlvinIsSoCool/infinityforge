package net.alvin.infinityforge.screen;

import net.alvin.infinityforge.item.BlueprintItem;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class BlueprintTableInputSlot extends Slot {
    public BlueprintTableInputSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof BlueprintItem;
    }
}
