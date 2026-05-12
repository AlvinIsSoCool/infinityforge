package net.alvin.infinityforge.screen;

import net.alvin.infinityforge.item.InfinityGauntletItem;
import net.alvin.infinityforge.item.InfinityStoneItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.registry.ModScreenHandlers;
import net.alvin.infinityforge.registry.ModStones;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;

import java.util.ArrayList;
import java.util.List;

public class GauntletScreenHandler extends ScreenHandler {
    public static final InfinityStoneType[] stoneOrder = {
            ModStones.POWER,
            ModStones.SPACE,
            ModStones.REALITY,
            ModStones.SOUL,
            ModStones.MIND,
            ModStones.TIME
    };

    private final SimpleInventory stoneInventory = new SimpleInventory(stoneOrder.length);
    private final ItemStack gauntletStack;

    public GauntletScreenHandler(int syncId, PlayerInventory playerInv, ItemStack gauntletStack) {
        super(ModScreenHandlers.GAUNTLET, syncId);
        this.gauntletStack = gauntletStack;

        loadFromGauntlet();

        addStoneSlots();
        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);

        stoneInventory.addListener(inv -> syncToGauntlet());
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof InfinityGauntletItem
                || player.getStackInHand(Hand.OFF_HAND).getItem() instanceof InfinityGauntletItem;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        Slot sourceSlot = slots.get(index);
        if (!sourceSlot.hasStack()) return ItemStack.EMPTY;

        ItemStack stack = sourceSlot.getStack();
        if (!(stack.getItem() instanceof InfinityStoneItem)) return ItemStack.EMPTY;

        if (index < stoneOrder.length) {
            if (!insertItem(stack, stoneOrder.length, slots.size(), true)) return ItemStack.EMPTY;
            sourceSlot.setStack(ItemStack.EMPTY);
        }
        else {
            for (int i = 0; i < stoneOrder.length; i++) {
                Slot stoneSlot = slots.get(i);
                if (!stoneSlot.hasStack() && stoneSlot.canInsert(stack)) {
                    stoneSlot.setStack(stack.copy());
                    sourceSlot.setStack(ItemStack.EMPTY);
                    return ItemStack.EMPTY;
                }
            }
        }

        return ItemStack.EMPTY;
    }

    private void loadFromGauntlet() {
        List<InfinityStoneType> stones = InfinityGauntletItem.getAddedStones(gauntletStack);
        for (int i = 0; i < stoneOrder.length; i++) {
            if (stones.contains(stoneOrder[i])) {
                stoneInventory.setStack(i, findStoneItem(stoneOrder[i]));
            }
        }
    }

    private void syncToGauntlet() {
        List<InfinityStoneType> stones = new ArrayList<>();
        for (int i = 0; i < stoneOrder.length; i++) {
            ItemStack stack = stoneInventory.getStack(i);
            if (stack.getItem() instanceof InfinityStoneItem stoneItem) {
                stones.add(stoneItem.getStoneType());
            }
        }
        InfinityGauntletItem.addStones(gauntletStack, stones);
    }

    private void addStoneSlots() {
        addSlot(new StoneSlot(stoneInventory, 0, 43, 28, stoneOrder[0]));
        addSlot(new StoneSlot(stoneInventory, 1, 64, 28, stoneOrder[1]));
        addSlot(new StoneSlot(stoneInventory, 2, 85, 28, stoneOrder[2]));
        addSlot(new StoneSlot(stoneInventory, 3, 106, 28, stoneOrder[3]));
        addSlot(new StoneSlot(stoneInventory, 4, 74, 58, stoneOrder[4]));
        addSlot(new StoneSlot(stoneInventory, 5, 134, 38, stoneOrder[5]));
    }

    private void addPlayerInventory(PlayerInventory inv) {
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 129 + row * 18));
    }

    private void addPlayerHotbar(PlayerInventory inv) {
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(inv, col, 8 + col * 18, 187));
    }

    private static ItemStack findStoneItem(InfinityStoneType type) {
        for (Item item : Registries.ITEM) {
            if (item instanceof InfinityStoneItem stoneItem && stoneItem.getStoneType() == type)
                return new ItemStack(item);
        }
        return ItemStack.EMPTY;
    }
}