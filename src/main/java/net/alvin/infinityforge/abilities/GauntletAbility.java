package net.alvin.infinityforge.abilities;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public class GauntletAbility {
    private final Identifier id;

    public GauntletAbility(Identifier id) {
        this.id = id;
    }

    public Identifier getId() { return id; }

    public void onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones) {

    }
}
