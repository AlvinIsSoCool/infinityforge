package net.alvin.infinityforge.infinity.abilities.impl.reality;

import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.base.AbilityState;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.alvin.infinityforge.item.FakeItem;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class SpawnItemAbility extends ActiveAbility implements AbilityState<Item> {
    private final boolean spawnFake;

    public SpawnItemAbility(Identifier id, AbilityIcon icon,
                            String key, Supplier<Integer> color,
                            Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks,
                            boolean spawnFake) {
        super(id, icon, key, color, requiredStones, cooldownTicks);
        this.spawnFake = spawnFake;
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        ItemStack fakeItem = FakeItem.create(Items.DIAMOND, 64);
        setState(player, FakeItem.getDisguise(fakeItem));
        ItemEntity entity = new ItemEntity(world, player.getX(), player.getY(), player.getZ() + 1.0, fakeItem);
        world.spawnEntity(entity);
        return true;
    }

    @Override
    public Class<Item> getType() { return Item.class; }

    @Override
    public ItemStack getDynamicIcon(Item state) {
        return spawnFake ? FakeItem.create(state, 1) : new ItemStack(state);
    }
}
