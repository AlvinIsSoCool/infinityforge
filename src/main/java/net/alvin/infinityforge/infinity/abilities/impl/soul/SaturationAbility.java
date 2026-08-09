package net.alvin.infinityforge.infinity.abilities.impl.soul;

import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.PassiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class SaturationAbility extends PassiveAbility {
    public SaturationAbility(Identifier id, AbilityIcon icon, Supplier<Integer> color) {
        super(id, icon, color, List::of);
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        HungerManager hungerManager = player.getHungerManager();

        if (hungerManager.getSaturationLevel() < 20) hungerManager.setSaturationLevel(20);
        if (hungerManager.isNotFull()) hungerManager.setFoodLevel(20);
    }
}
