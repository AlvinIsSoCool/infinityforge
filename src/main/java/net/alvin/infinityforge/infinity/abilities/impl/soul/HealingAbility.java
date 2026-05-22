package net.alvin.infinityforge.infinity.abilities.impl.soul;

import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.PassiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public class HealingAbility extends PassiveAbility {

    public HealingAbility(Identifier id, AbilityIcon icon,
                          String key, int color) {
        super(id, icon, key, color);
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        if (player.getHealth() < player.getMaxHealth() && world.getTime() % 10 == 0)
            player.heal(0.5f);
    }
}
