package net.alvin.infinityforge.infinity.abilities.impl.soul;

import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.PassiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class HealingAbility extends PassiveAbility {
    private final float amount;
    private final int frequency;

    public HealingAbility(Identifier id, AbilityIcon icon, Supplier<Integer> color,
                          float amount, int frequency) {
        super(id, icon, color, List::of);
        this.amount = amount;
        this.frequency = frequency;
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        if (player.getHealth() < player.getMaxHealth()
                && world.getTime() % frequency == 0)
            player.heal(amount);
    }
}
