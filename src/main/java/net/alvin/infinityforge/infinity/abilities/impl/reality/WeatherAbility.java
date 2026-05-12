package net.alvin.infinityforge.infinity.abilities.impl.reality;

import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class WeatherAbility extends ActiveAbility {

    public WeatherAbility(Identifier id, Identifier icon,
                          String key, int color,
                          Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks) {
        super(id, icon, key, color, requiredStones, cooldownTicks);
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        if (!world.isRaining() && !world.isThundering()) {
            world.setWeather(0, Integer.MAX_VALUE, true, false);
        } else if (world.isRaining() && !world.isThundering()) {
            world.setWeather(0, Integer.MAX_VALUE, true, true);
        } else {
            world.setWeather(Integer.MAX_VALUE, 0, false, false);
        }

        return true;
    }
}
