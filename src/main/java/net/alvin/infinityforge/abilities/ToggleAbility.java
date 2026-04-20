package net.alvin.infinityforge.abilities;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

import java.util.List;

public abstract non-sealed class ToggleAbility implements GauntletAbility {
    private final Identifier id;
    private final Identifier icon;
    private final String name;
    private final int color;
    private final int maxChargeTicks;
    private final int refillRateTicks;

    public ToggleAbility(Identifier id, Identifier icon, String name, int color, int maxChargeTicks, int refillRateTicks) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.color = color;
        this.maxChargeTicks = maxChargeTicks;
        this.refillRateTicks = refillRateTicks;
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
    public int getMaxChargeTicks() { return maxChargeTicks; }

    @Override
    public int getRefillRateTicks() { return refillRateTicks; }

    public abstract void onEnable(ServerWorld world, ServerPlayerEntity player,
                                  List<InfinityStoneType> activeStones);
    public abstract void onDisable(ServerWorld world, ServerPlayerEntity player,
                          List<InfinityStoneType> activeStones);
    public abstract void onTick(ServerWorld world, ServerPlayerEntity player,
                       List<InfinityStoneType> activeStones);
}
