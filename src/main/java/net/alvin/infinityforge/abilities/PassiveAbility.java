package net.alvin.infinityforge.abilities;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public abstract non-sealed class PassiveAbility implements GauntletAbility {
    private final Identifier id;
    private final Identifier icon;
    private final int color;

    public PassiveAbility(Identifier id, Identifier icon, int color) {
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

    public abstract void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones);
    public abstract void cleanup();
}
