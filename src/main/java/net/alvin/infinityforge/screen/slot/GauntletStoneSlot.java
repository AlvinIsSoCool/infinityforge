package net.alvin.infinityforge.screen.slot;

import net.alvin.infinityforge.item.InfinityStoneItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.registry.ModSounds;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class GauntletStoneSlot extends Slot {
    private final InfinityStoneType acceptedType;

    public GauntletStoneSlot(Inventory inventory, int index, int x, int y, InfinityStoneType acceptedType) {
        super(inventory, index, x, y);
        this.acceptedType = acceptedType;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem() instanceof InfinityStoneItem stoneItem
                && stoneItem.getStoneType() == acceptedType;
    }

    @Override
    public void setStack(ItemStack stack) {
        boolean wasEmpty = getStack().isEmpty();
        super.setStack(stack);

        if (wasEmpty && !stack.isEmpty() &&
                FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player == null) return;
            client.player.playSound(ModSounds.EQUIP_STONE, 1.0f, 1.0f);
        }
    }

    @Override
    public int getMaxItemCount() { return 1; }
}
