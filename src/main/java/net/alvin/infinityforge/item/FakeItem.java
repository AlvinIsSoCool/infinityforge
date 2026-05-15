package net.alvin.infinityforge.item;

import net.alvin.infinityforge.registry.ModItems;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class FakeItem extends Item {
    public static final String DISGUISE_KEY = "disguise_id";

    public FakeItem() {
        super(new FabricItemSettings());
    }

    public static ItemStack create(Item disguise) {
        ItemStack stack = new ItemStack(ModItems.FAKE_ITEM);
        stack.getOrCreateNbt().putString(DISGUISE_KEY,
                Registries.ITEM.getId(disguise).toString());
        return stack;
    }

    public static Item getDisguise(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        if (nbt == null || !nbt.contains(DISGUISE_KEY)) return Items.BARRIER;
        return Registries.ITEM.get(new Identifier(nbt.getString(DISGUISE_KEY)));
    }

    public static ItemStack getDisguiseStack(ItemStack stack) {
        return new ItemStack(getDisguise(stack));
    }

    @Override
    public Text getName(ItemStack stack) {
        return getDisguise(stack).getName(getDisguiseStack(stack));
    }

    @Override
    public Rarity getRarity(ItemStack stack) {
        return getDisguise(stack).getRarity(getDisguiseStack(stack));
    }
}
