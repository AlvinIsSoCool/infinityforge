package net.alvin.infinityforge.infinity.abilities.impl.reality;

import net.alvin.infinityforge.infinity.abilities.base.AbilityDynamicIcon;
import net.alvin.infinityforge.infinity.abilities.icon.AbilityIcon;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.base.AbilityState;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.alvin.infinityforge.item.FakeItem;
import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.screen.ItemSelectionScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class SpawnItemAbility extends ActiveAbility
        implements AbilityState<ItemStack>, AbilityDynamicIcon<ItemStack> {
    private final boolean spawnFake;

    public SpawnItemAbility(Identifier id, AbilityIcon icon,
                            Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones,
                            int cooldownTicks, boolean spawnFake) {
        super(id, icon, color, requiredStones, cooldownTicks);
        this.spawnFake = spawnFake;
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        ItemStack state = getState(player);
        if (state == null || player.isSneaking()) {
            final Identifier abilityId = getId();
            player.openHandledScreen(new ExtendedScreenHandlerFactory() {
                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                    return new ItemSelectionScreenHandler(syncId, abilityId);
                }

                @Override
                public Text getDisplayName() {
                    return Text.empty();
                }

                @Override
                public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                }
            });
            return false;
        } else {
            ItemStack stack = (spawnFake) ? FakeItem.create(state.getItem(), state.getCount()) : state;
            ItemEntity entity = new ItemEntity(
                    world,
                    player.getX() + 0.5,
                    player.getY() + 0.5,
                    player.getZ() + 0.5,
                    stack
            );
            world.spawnEntity(entity);
            return true;
        }
    }

    @Override
    public Class<ItemStack> getType() { return ItemStack.class; }

    @Override
    public ItemStack getDynamicIcon(ItemStack state) {
        return state == null ? ModItems.REALITY_STONE.getDefaultStack()
                : spawnFake ? FakeItem.create(state.getItem(), state.getCount()) : state;
    }
}
