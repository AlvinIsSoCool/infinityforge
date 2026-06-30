package net.alvin.infinityforge.client.screen;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.block.ModBlocks;
import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.network.c2s.ItemSelectionC2SPacket;
import net.alvin.infinityforge.registry.ModTags;
import net.alvin.infinityforge.screen.ItemSelectionScreenHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ItemSelectionScreen extends HandledScreen<ItemSelectionScreenHandler> {
    private static final Identifier GUI_TEXTURE = new Identifier(InfinityForge.MOD_ID,
            "textures/gui/item_selection.png");
    private static final int COLS = ItemSelectionScreenHandler.COLS;
    private static final int ROWS = ItemSelectionScreenHandler.ROWS;
    private static final int TRACK_X = 226;
    private static final int TRACK_Y = 18;
    private static final int TRACK_H = 160;
    private static final int THUMB_W = 3;
    private static final int THUMB_H = 8;

    private final List<Item> allItems;
    private List<Item> filteredItems;
    private int scrollOffset = 0;
    private TextFieldWidget searchField;

    private boolean scrollbarDragging = false;
    private double dragStartY;
    private int dragStartOffset;

    public ItemSelectionScreen(ItemSelectionScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        backgroundHeight = 190;
        backgroundWidth = 235;
        Predicate<Item> itemFilter = item -> (
                item != Items.AIR
                && item != ModItems.FAKE_ITEM
                && item != ModBlocks.FAKE_BLOCK.asItem()
                && !item.getDefaultStack().isIn(ModTags.Items.INFINITY_ITEMS)
        );
        allItems = Registries.ITEM.stream()
                .filter(itemFilter)
                .collect(Collectors.toList());
        filteredItems = allItems;
    }

    @Override
    protected void init() {
        super.init();
        searchField = new TextFieldWidget(this.textRenderer,
                x + 135, y + 6,
                83, 7, Text.empty());
        searchField.setDrawsBackground(false);
        searchField.setChangedListener(this::onSearchChanged);
        addDrawableChild(searchField);
        updateSlotContents();
    }

    private void onSearchChanged(String query) {
        scrollOffset = 0;
        if (query.isBlank()) {
            filteredItems = allItems;
        } else {
            String lower = query.toLowerCase();
            filteredItems = allItems.stream()
                    .filter(item -> item.getName().getString().toLowerCase().contains(lower)
                            || Registries.ITEM.getId(item).getPath().contains(lower))
                    .collect(Collectors.toList());
        }
        updateSlotContents();
    }

    private void updateSlotContents() {
        for (Slot slot : this.handler.slots) {
            int idx = slot.id + scrollOffset * COLS;
            Item item = idx < filteredItems.size() ? filteredItems.get(idx) : null;
            slot.inventory.setStack(slot.getIndex(), item != null ? new ItemStack(item) : ItemStack.EMPTY);
        }
    }

    private int getMaxScrollOffset() {
        return Math.max(0, (int) Math.ceil((double) filteredItems.size() / COLS) - ROWS);
    }

    private int getThumbY() {
        int max = getMaxScrollOffset();
        if (max == 0) return 0;
        return (int) ((float) scrollOffset / max * (TRACK_H - THUMB_H));
    }

    private boolean isOverThumb(double mouseX, double mouseY) {
        int ax = this.x + TRACK_X;
        int ay = this.y + TRACK_Y + getThumbY();
        return mouseX >= ax && mouseX < ax + THUMB_W && mouseY >= ay && mouseY < ay + THUMB_H;
    }

    @Override
    protected void onMouseClick(@Nullable Slot slot, int slotId, int button, SlotActionType actionType) {
        if (slot == null) return;
        ItemStack stack = slot.getStack();
        if (stack.isEmpty()) return;
        boolean shiftClicked = (actionType == SlotActionType.QUICK_MOVE);
        ClientPlayNetworking.send(new ItemSelectionC2SPacket(Registries.ITEM.getId(stack.getItem()), shiftClicked));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        scrollOffset = MathHelper.clamp(scrollOffset + (amount > 0 ? -1 : 1), 0, getMaxScrollOffset());
        updateSlotContents();
        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0 && isOverThumb(mouseX, mouseY)) {
            scrollbarDragging = true;
            dragStartY = mouseY;
            dragStartOffset = scrollOffset;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (scrollbarDragging && button == 0) {
            int max = getMaxScrollOffset();
            if (max > 0) {
                double ratio = (mouseY - dragStartY) / (TRACK_H - THUMB_H);
                scrollOffset = MathHelper.clamp((int) Math.round(dragStartOffset + ratio * max), 0, max);
                updateSlotContents();
            }
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (scrollbarDragging && button == 0) {
            scrollbarDragging = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (searchField.isFocused()) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                searchField.setFocused(false);
                return true;
            }
            searchField.keyPressed(keyCode, scanCode, modifiers);
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (searchField.isFocused()) return searchField.charTyped(chr, modifiers);
        return super.charTyped(chr, modifiers);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {}

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(GUI_TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        int tx = x + TRACK_X;
        int ty = y + TRACK_Y + getThumbY();
        context.drawTexture(GUI_TEXTURE, tx, ty, (scrollbarDragging) ? 250 : 253, 0, THUMB_W, THUMB_H);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        updateSlotContents();
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
