package net.alvin.infinityforge.item;

import net.alvin.infinityforge.registry.ModItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

import java.util.Map;
import java.util.WeakHashMap;

public class FakeItem extends Item {
    public static final String DISGUISE_KEY = "disguise_id";
    private static final Map<ItemStack, Long> CREATION_TIME_CACHE = new WeakHashMap<>();

    public FakeItem() {
        super(new FabricItemSettings());
    }

    public static ItemStack create(Item disguise, int count) {
        ItemStack stack = new ItemStack(ModItems.FAKE_ITEM, count);
        stack.getOrCreateNbt().putString(DISGUISE_KEY,
                Registries.ITEM.getId(disguise).toString());
        return stack;
    }

    public static Item getDisguise(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null || !nbt.contains(DISGUISE_KEY)) return ModItems.FAKE_ITEM;
        return Registries.ITEM.get(new Identifier(nbt.getString(DISGUISE_KEY)));
    }

    @Override
    public Text getName(ItemStack stack) {
        Item disguiseItem = getDisguise(stack);
        if (disguiseItem instanceof FakeItem) return Text.literal("Fake Item");
        else return disguiseItem.getName(new ItemStack(disguiseItem));
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        Item disguiseItem = getDisguise(stack);
        if (disguiseItem instanceof FakeItem) return Rarity.COMMON;
        else return disguiseItem.getRarity(new ItemStack(disguiseItem));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient()) return;

        long created;
        Long cached = CREATION_TIME_CACHE.get(stack);

        if (cached == null) {
            created = world.getTime();
            CREATION_TIME_CACHE.put(stack, created);
        } else {
            created = cached;
        }

        if (world.getTime() - created >= 200) {
            if (entity instanceof PlayerEntity player)
                player.getInventory().removeOne(stack);
            CREATION_TIME_CACHE.remove(stack);
        }
    }
}
