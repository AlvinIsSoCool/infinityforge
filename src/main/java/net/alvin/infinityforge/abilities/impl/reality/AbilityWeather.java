package net.alvin.infinityforge.abilities.impl.reality;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

import static net.alvin.infinityforge.helpers.InfinityStoneColors.REALITY_STONE_ABILITY_COLOR;

public class AbilityWeather extends ActiveAbility {
    public AbilityWeather() {
        super(new Identifier(InfinityForge.MOD_ID, "weather"),
                new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/weather.png"),
                Text.translatable("abilities." + InfinityForge.MOD_ID + ".weather").getString(),
                REALITY_STONE_ABILITY_COLOR,
                600);
    }

    @Override
    public void onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones) {
        ServerWorld serverWorld = (ServerWorld) world;
        Random random = new Random();

        switch (random.nextInt(3)) {
            case 0:
                setClear(serverWorld);
                break;
            case 1:
                setRaining(serverWorld);
                break;
            case 2:
                setThundering(serverWorld);
                break;
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
