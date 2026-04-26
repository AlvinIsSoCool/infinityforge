package net.alvin.infinityforge.abilities.impl.reality;

import net.alvin.infinityforge.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public class WeatherAbility extends ActiveAbility {

    public WeatherAbility(Identifier id, Identifier icon,
                          String key, int color,
                          Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks) {
        super(id, icon, key, color, requiredStones, cooldownTicks);
    }

    @Override
    public void onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones) {
        ServerWorld serverWorld = (ServerWorld) world;

        if (!serverWorld.isRaining() && !serverWorld.isThundering()) {
            setRaining(serverWorld);
        } else if (serverWorld.isRaining() && !serverWorld.isThundering()) {
            setThundering(serverWorld);
        } else {
            setClear(serverWorld);
        }
    }

    private void setClear(ServerWorld serverWorld) {
        serverWorld.setWeather(Integer.MAX_VALUE, 0, false, false);
    }

    private void setRaining(ServerWorld serverWorld) {
        serverWorld.setWeather(0, Integer.MAX_VALUE, true, false);
    }

    private void setThundering(ServerWorld serverWorld) {
        serverWorld.setWeather(0, Integer.MAX_VALUE, true, true);
    }
}
