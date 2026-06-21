package net.alvin.infinityforge.infinity.abilities.impl.space;

import net.alvin.infinityforge.entity.BlackHoleEntity;
import net.alvin.infinityforge.entity.ModEntities;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.HeldAbility;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class BlackHoleAbility extends HeldAbility {
    public BlackHoleAbility(Identifier id, AbilityIcon icon, String key, Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones, int maxChargeTicks, int refillRateTicks) {
        super(id, icon, key, color, requiredStones, maxChargeTicks, refillRateTicks);
    }

    @Override
    public void onStart(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        BlackHoleEntity bh = new BlackHoleEntity(ModEntities.BLACKHOLE_ENTITY, world);
        bh.setPosition(player.getEyePos().add(player.getRotationVec(1.0F).multiply(4.0)));
        bh.setOwner(player.getUuid());
        world.spawnEntity(bh);
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {

    }

    @Override
    public void onStop(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {

    }
}
