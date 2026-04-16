package net.alvin.infinityforge.client.screen;

import net.alvin.infinityforge.infinity.InfinityGauntletItem;
import net.alvin.infinityforge.infinity.InfinityStoneItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
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

    public static final int STONE_SLOT_COUNT = 6;

    private final SimpleInventory stoneInventory = new SimpleInventory(STONE_SLOT_COUNT);
    private final ItemStack gauntletStack;

    // Server-side constructor (opened via factory)
    public GauntletScreenHandler(int syncId, PlayerInventory playerInv, ItemStack gauntletStack) {
        super(ModScreenHandlers.GAUNTLET, syncId);
        this.gauntletStack = gauntletStack;

        loadFromGauntlet((InfinityGauntletItem) gauntletStack.getItem());

        addStoneSlots();
        addPlayerInventory(playerInv);
        addPlayerHotbar(playerInv);

        // Whenever a stone slot changes, write back to gauntlet NBT
        stoneInventory.addListener(inv -> syncToGauntlet((InfinityGauntletItem) gauntletStack.getItem()));
    }

    private void loadFromGauntlet(InfinityGauntletItem item) {
        List<InfinityStoneType> stones = item.getAddedStones(gauntletStack);
        for (int i = 0; i < stones.size() && i < STONE_SLOT_COUNT; i++) {
            // Find the matching StoneItem for this type
            InfinityStoneType type = stones.get(i);
            ItemStack stoneStack = findStoneItem(type);
            stoneInventory.setStack(i, stoneStack);
        }
    }

    private void syncToGauntlet(InfinityGauntletItem item) {
        // Rebuild stone list from current slot contents
        List<InfinityStoneType> stones = new ArrayList<>();
        for (int i = 0; i < STONE_SLOT_COUNT; i++) {
            ItemStack stack = stoneInventory.getStack(i);
            if (stack.getItem() instanceof InfinityStoneItem stoneItem) {
                stones.add(stoneItem.getStoneType());
            }
        }
        item.addStones(gauntletStack, stones);
    }

    private void addStoneSlots() {
        for (int i = 0; i < STONE_SLOT_COUNT; i++) {
            addSlot(new InfinityStoneSlot(stoneInventory, i, 44 + i * 18, 20));
        }
    }

    private void addPlayerInventory(PlayerInventory inv) {
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 55 + row * 18));
    }

    private void addPlayerHotbar(PlayerInventory inv) {
        for (int col = 0; col < 9; col++)
            addSlot(new Slot(inv, col, 8 + col * 18, 113));
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getStackInHand(Hand.MAIN_HAND).getItem() instanceof InfinityGauntletItem
                || player.getStackInHand(Hand.OFF_HAND).getItem() instanceof InfinityGauntletItem;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasStack()) return ItemStack.EMPTY;

        ItemStack stack = slot.getStack();
        if (!(stack.getItem() instanceof InfinityStoneItem)) return ItemStack.EMPTY;

        if (!insertItem(stack, 0, STONE_SLOT_COUNT, false)) return ItemStack.EMPTY;

        slot.markDirty();
        return ItemStack.EMPTY;
    }

    // Needed for loadFromGauntlet — finds registered StoneItem for a given type
    private static ItemStack findStoneItem(InfinityStoneType type) {
        for (Item item : Registries.ITEM) {
            if (item instanceof InfinityStoneItem si && si.getStoneType() == type)
                return new ItemStack(item);
        }
        return ItemStack.EMPTY;
    }
}