package net.alvin.infinityforge.abilities;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class TestGauntletAbility2 extends GauntletAbility {
    public TestGauntletAbility2() {
        super(new Identifier(InfinityForge.MOD_ID, "test2"));
    }

    @Override
    public void onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones) {
        System.out.println("Ability2 Triggered!");
        ServerWorld serverWorld = (ServerWorld) world;
        serverWorld.setWeather(0, 600, true, true);
        serverWorld.setTimeOfDay(18000);
        serverWorld.setThunderGradient(0.0f);
        serverWorld.setRainGradient(0.0f);
    }
}
