package net.alvin.infinityforge.abilities;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class AbilityWeather extends ActiveAbility {
    public AbilityWeather() {
        super(new Identifier(InfinityForge.MOD_ID, "weather"),
                new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/weather.png"),
                0xFF1E1E);
    }

    @Override
    public void onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones) {
        System.out.println("Ability2 Triggered!");
        ServerWorld serverWorld = (ServerWorld) world;
        serverWorld.setWeather(0, Integer.MAX_VALUE, true, true);
        serverWorld.setTimeOfDay(18000);
    }
}
