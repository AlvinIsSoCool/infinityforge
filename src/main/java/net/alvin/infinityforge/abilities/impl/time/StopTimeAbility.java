package net.alvin.infinityforge.abilities.impl.time;

import net.alvin.infinityforge.abilities.base.ToggleAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

import java.util.List;
import java.util.function.Supplier;

public class StopTimeAbility extends ToggleAbility {
    public StopTimeAbility(Identifier id, Identifier icon, String key, int color, Supplier<List<InfinityStoneType>> requiredStones, int maxChargeTicks, int refillRateTicks) {
        super(id, icon, key, color, requiredStones, maxChargeTicks, refillRateTicks);
    }

    @Override
    public void onEnable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {

    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        long currentTime = world.getTimeOfDay();
        long nextTime = currentTime - 1;
        world.setTimeOfDay(nextTime);
        player.networkHandler.sendPacket(new WorldTimeUpdateS2CPacket(
                world.getTime(),
                nextTime,
                world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)
        ));
    }

    @Override
    public void onDisable(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {

    }
}
