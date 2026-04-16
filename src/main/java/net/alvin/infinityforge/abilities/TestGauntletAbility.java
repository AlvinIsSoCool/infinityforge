package net.alvin.infinityforge.abilities;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.WeatherCommand;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.EntityList;
import net.minecraft.world.World;
import net.minecraft.world.level.ServerWorldProperties;

import java.util.List;

public class TestGauntletAbility extends GauntletAbility {
    public TestGauntletAbility() {
        super(new Identifier(InfinityForge.MOD_ID, "test"));
    }

    @Override
    public void onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones) {
        System.out.println("Ability Triggered!");
        ServerWorld serverWorld = (ServerWorld) world;
        ZombieEntity zombie = new ZombieEntity(EntityType.ZOMBIE, serverWorld);
        zombie.refreshPositionAndAngles(player.getBlockPos(), 0f, 0f);
        world.spawnEntity(zombie);

        serverWorld.setWeather(0, 600, true, true);
        serverWorld.setTimeOfDay(18000);
        serverWorld.setThunderGradient(0.0f);
        serverWorld.setRainGradient(0.0f);
    }
}
