package net.alvin.infinityforge.abilities;

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

    @Override
    public int getCooldownTicks() { return 0; }

    @Override
    public int getMaxChargeTicks() { return 0; }

    @Override
    public int getRefillRateTicks() { return 0; }

    public abstract void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones);
}
