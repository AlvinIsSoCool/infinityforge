package net.alvin.infinityforge.abilities.base;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public abstract non-sealed class PassiveAbility implements GauntletAbility {
    private final Identifier id;
    private final Identifier icon;
    private final String name;
    private final int color;

    public PassiveAbility(Identifier id, Identifier icon, String name, int color) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.color = color;
    }

    @Override
    public Identifier getId() { return id; }

    @Override
    public Identifier getIcon() { return icon; }

    @Override
    public String getName() { return name; }

    @Override
    public int getColor() { return color; }

    public abstract void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones);
}
