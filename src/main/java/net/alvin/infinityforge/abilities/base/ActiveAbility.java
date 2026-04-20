package net.alvin.infinityforge.abilities.base;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;

public abstract non-sealed class ActiveAbility implements GauntletAbility {
    private final Identifier id;
    private final Identifier icon;
    private final String name;
    private final int color;
    private final int cooldownTicks;

    public ActiveAbility(Identifier id, Identifier icon, String name, int color, int cooldownTicks) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.color = color;
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    public Identifier getId() { return id; }

    @Override
    public Identifier getIcon() { return icon; }

    @Override
    public String getName() { return name; }

    @Override
    public int getColor() { return color; }

    public int getCooldownTicks() { return cooldownTicks; }

    public abstract void onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones);
}
