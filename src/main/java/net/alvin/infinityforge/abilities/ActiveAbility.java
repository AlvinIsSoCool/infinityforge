package net.alvin.infinityforge.abilities;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public abstract non-sealed class ActiveAbility implements GauntletAbility{
    private final Identifier id;
    private final Identifier icon;
    private final int color;

    public ActiveAbility(Identifier id, Identifier icon, int color) {
        this.id = id;
        this.icon = icon;
        this.color = color;
    }

    @Override
    public Identifier getId() { return id; }

    @Override
    public Identifier getIcon() { return icon; }

    @Override
    public int getColor() { return color; }

    public abstract void onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones);
}
