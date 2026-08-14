package net.alvin.infinityforge.infinity.abilities.impl.power;

import net.alvin.infinityforge.entity.EnergyBeamEntity;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.icon.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.HeldAbility;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class EnergyBeamAbility extends HeldAbility {
    public EnergyBeamAbility(Identifier id, AbilityIcon icon,
                             Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones,
                             int maxChargeTicks, int refillRateTicks) {
        super(id, icon, color, requiredStones, maxChargeTicks, refillRateTicks);
    }

    @Override
    public boolean onStart(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        EnergyBeamEntity entity = new EnergyBeamEntity(world, player);
        world.spawnEntity(entity);
        return true;
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {

    }

    @Override
    public void onStop(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {

    }
}
