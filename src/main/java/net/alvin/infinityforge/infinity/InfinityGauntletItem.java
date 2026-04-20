package net.alvin.infinityforge.infinity;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.abilities.base.*;
import net.alvin.infinityforge.screen.GauntletScreenHandler;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class InfinityGauntletItem extends Item {
    private static final String STONES_KEY = "Stones";

    public InfinityGauntletItem() {
        super(new FabricItemSettings().maxDamage(0).fireproof());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            user.openHandledScreen(new ExtendedScreenHandlerFactory() {
                @Override
                public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                    return new GauntletScreenHandler(syncId, playerInventory, user.getStackInHand(hand));
                }

                @Override
                public Text getDisplayName() {
                    return Text.translatable("gui." + InfinityForge.MOD_ID + ".gauntlet");
                }

                @Override
                public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                    buf.writeEnumConstant(hand);
                }
            });
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public List<InfinityStoneType> getAddedStones(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null || !nbt.contains(STONES_KEY)) return List.of();

        NbtList list = nbt.getList(STONES_KEY, NbtElement.STRING_TYPE);
        return list.stream()
                .map(e -> InfinityStoneTypeRegistry.STONE_TYPE_REGISTRY.get(new Identifier(e.asString())))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void addStone(ItemStack stack, InfinityStoneType stone) {
        NbtCompound nbt = stack.getOrCreateNbt();
        NbtList list = nbt.getList(STONES_KEY, NbtElement.STRING_TYPE);
        list.add(NbtString.of(InfinityStoneTypeRegistry.STONE_TYPE_REGISTRY.getId(stone).toString()));
        nbt.put(STONES_KEY, list);
        System.out.println("Added " + InfinityStoneTypeRegistry.STONE_TYPE_REGISTRY.getId(stone) + " to the gauntlet!");
    }

    public void addStones(ItemStack stack, List<InfinityStoneType> stones) {
        stack.getOrCreateNbt().put(STONES_KEY, new NbtList());
        stones.forEach(stone -> addStone(stack, stone));
    }

    public List<GauntletAbility> getGauntletAbilities(ItemStack stack) {
        return getAddedStones(stack).stream()
                .flatMap(s -> s.gauntletAbilities().stream())
                .collect(Collectors.toList());
    }

    public List<ActiveAbility> getActiveAbilities(ItemStack stack) {
        return getAddedStones(stack).stream()
                .flatMap(s -> s.gauntletAbilities().stream())
                .filter(a -> a instanceof ActiveAbility)
                .map(a -> (ActiveAbility) a)
                .collect(Collectors.toList());
    }

    public List<PassiveAbility> getPassiveAbilities(ItemStack stack) {
        return getAddedStones(stack).stream()
                .flatMap(s -> s.gauntletAbilities().stream())
                .filter(a -> a instanceof PassiveAbility)
                .map(a -> (PassiveAbility) a)
                .collect(Collectors.toList());
    }

    public List<ToggleAbility> getToggleAbilities(ItemStack stack) {
        return getAddedStones(stack).stream()
                .flatMap(s -> s.gauntletAbilities().stream())
                .filter(a -> a instanceof ToggleAbility)
                .map(a -> (ToggleAbility) a)
                .collect(Collectors.toList());
    }

    public List<HeldAbility> getHeldAbilities(ItemStack stack) {
        return getAddedStones(stack).stream()
                .flatMap(s -> s.gauntletAbilities().stream())
                .filter(a -> a instanceof HeldAbility)
                .map(a -> (HeldAbility) a)
                .collect(Collectors.toList());
    }

    public List<GauntletAbility> getVisibleAbilities(ItemStack stack) {
        return getAddedStones(stack).stream()
                .flatMap(s -> s.gauntletAbilities().stream())
                .filter(a -> a instanceof ActiveAbility
                        || a instanceof ToggleAbility
                        || a instanceof HeldAbility)
                .collect(Collectors.toList());
    }

    @Nullable
    public static ItemStack findGauntlet(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() instanceof InfinityGauntletItem) return stack;
        }
        return null;
    }
}
