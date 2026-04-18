package net.alvin.infinityforge.abilities;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public abstract non-sealed class HeldAbility implements GauntletAbility {
    private final Identifier id;
    private final Identifier icon;
    private final int color;

    public HeldAbility(Identifier id, Identifier icon, int color) {
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

    // Called once when key is first pressed
    public abstract void onStart(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones);

    // Called every tick while key is held
    public abstract void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones);

    // Called once when key is released
    public abstract void onStop(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones);
}
