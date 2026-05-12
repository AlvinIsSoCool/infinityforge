package net.alvin.infinityforge.infinity.abilities.base;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public abstract non-sealed class PassiveAbility implements GauntletAbility {
    private final Identifier id;
    private final Identifier icon;
    private final String key;
    private final int color;
    /**
     * Provides the list of stones needed for the ability.
     * Needs to include the stone that registers the ability.
     * e.g. A soul stone ability requiring the power stone would provide
     * required stones as so: {@code () -> List.of(ModStones.POWER, ModStones.SOUL)}
     * No requirements as so: {@code List::of}
     */
    private final Supplier<List<InfinityStoneType>> requiredStones;

    public PassiveAbility(Identifier id, Identifier icon, String key, int color, Supplier<List<InfinityStoneType>> requiredStones) {
        this.id = id;
        this.icon = icon;
        this.key = key;
        this.color = color;
        this.requiredStones = requiredStones;
    }

    // Convenience Constructor for ability with no stone requirements.
    public PassiveAbility(Identifier id, Identifier icon, String key, int color) {
        this.id = id;
        this.icon = icon;
        this.key = key;
        this.color = color;
        this.requiredStones = List::of;
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

    /**
     * The function that runs while this ability is active.
     * Is dispatched from the server, so all logic contained within should
     * only be server-side.
     * @param world The world in which the ability was used.
     * @param player The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     */
    public abstract void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones);
}
