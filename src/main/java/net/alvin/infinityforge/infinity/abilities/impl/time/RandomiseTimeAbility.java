package net.alvin.infinityforge.infinity.abilities.impl.time;

import net.alvin.infinityforge.infinity.abilities.icon.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameRules;

import java.util.List;
import java.util.function.Supplier;

public class RandomiseTimeAbility extends ActiveAbility {
    public RandomiseTimeAbility(Identifier id, AbilityIcon icon,
                                Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones,
                                int cooldownTicks) {
        super(id, icon, color, requiredStones, cooldownTicks);
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        long currentTime = world.getTimeOfDay();
        long randomTimeOffset = world.getRandom().nextInt(24000);
        long nextTime = currentTime + randomTimeOffset;
        world.setTimeOfDay(nextTime);
        player.networkHandler.sendPacket(new WorldTimeUpdateS2CPacket(
                world.getTime(),
                nextTime,
                world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)));
        return true;
    }
}
