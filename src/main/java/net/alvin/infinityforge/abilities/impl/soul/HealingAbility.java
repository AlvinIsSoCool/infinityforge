package net.alvin.infinityforge.abilities.impl.soul;

import net.alvin.infinityforge.abilities.base.PassiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class HealingAbility extends PassiveAbility {

    public HealingAbility(Identifier id, Identifier icon,
                          String key, int color,
                          Supplier<List<InfinityStoneType>> requiredStones) {
        super(id, icon, key, color, requiredStones);
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        if (player.getHealth() < player.getMaxHealth() && world.getTime() % 10 == 0)
            player.heal(0.5f);
    }
}
