package net.alvin.infinityforge.abilities.base;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

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

    public abstract boolean onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones);
}
