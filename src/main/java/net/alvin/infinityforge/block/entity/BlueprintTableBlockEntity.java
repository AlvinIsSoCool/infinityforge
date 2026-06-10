package net.alvin.infinityforge.block.entity;

import net.alvin.infinityforge.item.BlueprintItem;
import net.alvin.infinityforge.screen.BlueprintTableScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class BlueprintTableBlockEntity extends BlockEntity
        implements ExtendedScreenHandlerFactory, Inventory {
    private final DefaultedList<ItemStack> inventory = DefaultedList.ofSize(3, ItemStack.EMPTY);

    protected final PropertyDelegate propertyDelegate;
    private int progress = 0;
    private int maxProgress = 0;
    private int craftingState = 0; // 0: idle, 1: crafting, 2: invalid state

    private static final String PROGRESS_KEY = "crafting_progress";

    public BlueprintTableBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BLUEPRINT_TABLE_BLOCK_ENTITY, pos, state);
        this.propertyDelegate = new PropertyDelegate() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> BlueprintTableBlockEntity.this.progress;
                    case 1 -> BlueprintTableBlockEntity.this.maxProgress;
                    case 2 -> BlueprintTableBlockEntity.this.craftingState;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> BlueprintTableBlockEntity.this.progress = value;
                    case 1 -> BlueprintTableBlockEntity.this.maxProgress = value;
                    case 2 -> BlueprintTableBlockEntity.this.craftingState = value;
                }
            }

            @Override
            public int size() {
                return 3;
            }
        };
    }

    @Override public int size() { return inventory.size(); }
    @Override public boolean isEmpty() { return inventory.stream().allMatch(ItemStack::isEmpty); }
    @Override public ItemStack getStack(int slot) { return inventory.get(slot); }
    @Override public ItemStack removeStack(int slot, int amount) { markDirty(); return Inventories.splitStack(inventory, slot, amount); }
    @Override public ItemStack removeStack(int slot) { markDirty(); return Inventories.removeStack(inventory, slot); }
    @Override public void setStack(int slot, ItemStack stack) { inventory.set(slot, stack); markDirty(); }
    @Override public boolean canPlayerUse(PlayerEntity player) { return Inventory.canPlayerUse(this, player); }

    @Override public void clear() {
        inventory.clear();
        markDirty();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("gui.infinityforge.blueprint_table");
    }

    @Override
    public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
        return new BlueprintTableScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        Inventories.writeNbt(nbt, inventory);
        nbt.putInt(PROGRESS_KEY, progress);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        Inventories.readNbt(nbt, inventory);
        progress = nbt.getInt(PROGRESS_KEY);
    }

    public void tick(World world, BlockPos pos, BlockState state) {
        if (world.isClient()) return;

        ItemStack blueprintStack = getStack(0);
        ItemStack ingredientStack = getStack(1);

        if (!(blueprintStack.getItem() instanceof BlueprintItem)) {
            progress = 0;
            maxProgress = 0;
            craftingState = 0;
            return;
        }

        ItemStack requiredIngredient = BlueprintItem.getIngredient(blueprintStack);
        ItemStack output = BlueprintItem.getOutput(blueprintStack);
        int craftingTime = BlueprintItem.getCraftingTime(blueprintStack);

        if (requiredIngredient.isEmpty() || output.isEmpty()) {
            progress = 0;
            maxProgress = 0;
            craftingState = requiredIngredient.isEmpty() ? 2 : 0;
            return;
        }

        if (!ItemStack.areItemsEqual(ingredientStack, requiredIngredient)) {
            progress = 0;
            maxProgress = 0;
            craftingState = ingredientStack.isEmpty() ? 0 : 2;
            return;
        }

        if (!getStack(2).isEmpty()) return;

        if (progress == 0) {
            maxProgress = craftingTime;
        }

        craftingState = 1;
        progress++;
        markDirty();

        if (progress >= maxProgress) {
            setStack(2, output.copy());
            removeStack(1, requiredIngredient.getCount());
            progress = 0;
            maxProgress = 0;
        }
    }

    public DefaultedList<ItemStack> getInventory() {
        return inventory;
    }
}
