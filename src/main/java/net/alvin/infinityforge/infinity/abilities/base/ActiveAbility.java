package net.alvin.infinityforge.infinity.abilities.base;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public abstract non-sealed class ActiveAbility implements GauntletAbility {
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
     * Controls the cooldown of the ability.
     * Use 0 for no cooldown.
     */
    private final int cooldownTicks;

    public ActiveAbility(Identifier id, Identifier icon, String key, int color, Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks) {
        this.id = id;
        this.icon = icon;
        this.key = key;
        this.color = color;
        this.requiredStones = requiredStones;
        this.cooldownTicks = cooldownTicks;
    }

    // Convenience Constructor for ability with no cooldown.
    public ActiveAbility(Identifier id, Identifier icon, String key, int color, Supplier<List<InfinityStoneType>> requiredStones) {
        this.id = id;
        this.icon = icon;
        this.key = key;
        this.color = color;
        this.requiredStones = requiredStones;
        this.cooldownTicks = 0;
    }

    // Convenience Constructor for ability with no cooldown and no stone requirements.
    public ActiveAbility(Identifier id, Identifier icon, String key, int color) {
        this.id = id;
        this.icon = icon;
        this.key = key;
        this.color = color;
        this.requiredStones = List::of;
        this.cooldownTicks = 0;
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

    public int getCooldownTicks() { return cooldownTicks; }

    /**
     * The function that runs on usage of this ability type.
     * Is dispatched from the server, so all logic contained within should
     * only be server-side.
     * @param world The world in which the ability was used.
     * @param player The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     * @return true, for starting the ability cooldown after usage, if cooldownTicks is non-zero.
     * false, for not starting the ability cooldown after usage.
     */
    public abstract boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones);
}
