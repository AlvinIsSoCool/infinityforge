package net.alvin.infinityforge.abilities;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public class AbilityHealing extends PassiveAbility {

    public AbilityHealing() {
        super(new Identifier(InfinityForge.MOD_ID, "healing"),
                new Identifier(InfinityForge.MOD_ID, "textures/gui/abilities/healing.png"),
                0x000000);
    }

    @Override
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        player.heal(0.5f);
    }

    @Override
    public void cleanup() {}
}
