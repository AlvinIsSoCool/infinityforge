package net.alvin.infinityforge.abilities.impl.soul;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.abilities.base.PassiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class AbilityHealing extends PassiveAbility {
    public AbilityHealing() {
        super(new Identifier(InfinityForge.MOD_ID, "healing"),
                new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/healing.png"),
                Text.translatable("abilities." + InfinityForge.MOD_ID + ".healing").getString(),
                0xFF000000);
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        if (world.getTime() % 2 == 0) player.heal(0.5f);
    }
}
