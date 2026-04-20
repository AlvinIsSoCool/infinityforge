package net.alvin.infinityforge.abilities;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class AbilityWeatherHeld extends HeldAbility {
    public AbilityWeatherHeld() {
        super(new Identifier(InfinityForge.MOD_ID, "weather_held"),
                new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/weather.png"),
                Text.translatable("abilities." + InfinityForge.MOD_ID + ".weather_held").getString(),
                0xFFFF1E1E,
                200,
                2);
    }

    @Override
    public void onStart(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        world.setWeather(0, Integer.MAX_VALUE, true, true);
        world.setTimeOfDay(18000);
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        player.heal(0.5f);
    }

    @Override
    public void onStop(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        world.setWeather(Integer.MAX_VALUE, 0, false, false);
        world.setTimeOfDay(0);
    }
}
