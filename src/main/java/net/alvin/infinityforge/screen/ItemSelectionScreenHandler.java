package net.alvin.infinityforge.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;

public class ItemSelectionScreenHandler extends ScreenHandler {
    public static final int COLS = 12;
    public static final int ROWS = 9;
    private final SimpleInventory itemInventory = new SimpleInventory(COLS * ROWS);
    private final Identifier abilityId;

    public ItemSelectionScreenHandler(int syncId, Identifier abilityId) {
        super(ModScreenHandlers.ITEM_SELECTION_SCREEN_HANDLER, syncId);
        this.abilityId = abilityId;
        addItemSlots();
    }

    public ItemSelectionScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, null);
    }

    public Identifier getAbilityId() { return abilityId; }

    private void addItemSlots() {
        for (int row = 0; row < ROWS; row++)
            for (int col = 0; col < COLS; col++)
                addSlot(new NoInsertSlot(itemInventory, col + row * COLS, 8 + col * 18, 18 + row * 18));
    }

    @Override
    public void onSlotClick(int slotIndex, int button, SlotActionType actionType, PlayerEntity player) {}

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) { return ItemStack.EMPTY; }

    @Override
    public boolean canUse(PlayerEntity player) { return true; }
}
