package net.alvin.infinityforge.abilities.base;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public abstract non-sealed class ToggleAbility implements GauntletAbility {
    private final Identifier id;
    private final Identifier icon;
    private final String key;
    private final int color;
    /**
     * Provides the list of stones needed for the ability.
     * Needs to include the stone that registers the ability.
     * e.g. A space stone ability requiring the power stone would provide
     * required stones as so: {@code () -> List.of(ModStones.POWER, ModStones.SPACE)}
     * No requirements as so: {@code List::of}
     */
    private final Supplier<List<InfinityStoneType>> requiredStones;
    /**
     * Controls maximum charge for the ability.
     * -1 indicates no charge. Infinite usage.
     */
    private final int maxChargeTicks;
    /**
     * Controls charge refill speed.
     * Positive: ticks per +1 charge (e.g. 2 = one charge every 2 ticks)
     * Zero: no refill
     * Negative: charges per tick (e.g. -4 = four charges per tick)
     */
    private final int refillRateTicks;

    public ToggleAbility(Identifier id, Identifier icon, String key, int color, Supplier<List<InfinityStoneType>> requiredStones, int maxChargeTicks, int refillRateTicks) {
        this.id = id;
        this.icon = icon;
        this.key = key;
        this.color = color;
        this.requiredStones = requiredStones;
        this.maxChargeTicks = maxChargeTicks;
        this.refillRateTicks = refillRateTicks;
    }

    // Convenience Constructor for ability with no cooldown.
    public ToggleAbility(Identifier id, Identifier icon, String key, int color, Supplier<List<InfinityStoneType>> requiredStones) {
        this.id = id;
        this.icon = icon;
        this.key = key;
        this.color = color;
        this.requiredStones = requiredStones;
        this.maxChargeTicks = -1;
        this.refillRateTicks = 0;
    }

    @Override
    public Identifier getId() { return id; }

    @Override
    public Identifier getIcon() { return icon; }

    @Override
    public String getName() { return Text.translatable(key).getString(); }

    @Override
    public int getColor() { return color; }

    @Override
    public boolean meetsCondition(List<InfinityStoneType> activeStones) {
        return new HashSet<>(activeStones).containsAll(requiredStones.get());
    }

    public int getMaxChargeTicks() { return maxChargeTicks; }
    public int getRefillRateTicks() { return refillRateTicks; }

    /**
     * The function that runs when this ability is toggled.
     * Is dispatched from the server, so all logic contained within should
     * only be server-side.
     * @param world The world in which the ability was used.
     * @param player The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     */
    public abstract void onEnable(ServerWorld world, ServerPlayerEntity player,
                                  List<InfinityStoneType> activeStones);
    /**
     * The function that runs while this ability is toggled on.
     * Is dispatched from the server, so all logic contained within should
     * only be server-side.
     * @param world The world in which the ability was used.
     * @param player The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     */
    public abstract void onTick(ServerWorld world, ServerPlayerEntity player,
                                List<InfinityStoneType> activeStones);

    /**
     * The function that runs when this ability is toggled off.
     * Is dispatched from the server, so all logic contained within should
     * only be server-side.
     * @param world The world in which the ability was used.
     * @param player The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     */
    public abstract void onDisable(ServerWorld world, ServerPlayerEntity player,
                          List<InfinityStoneType> activeStones);
}
