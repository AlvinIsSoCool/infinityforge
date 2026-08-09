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
    /**
     * Provides the Identifier of the ability.
     */
    private final Identifier id;
    /**
     * Provides the default static icon for the ability.
     */
    private final AbilityIcon icon;
    /**
     * Provides the translation key for the ability.
     * The format is: abilities.modid.ability_name
     * (Ex: abilities.infinityforge.knockback_resistance)
     */
    private final String key;
    /**
     * Provides the color of the ability.
     * Use RGB format. ARGB conversion happens internally.
     */
    private final Supplier<Integer> color;
    /**
     * Provides the list of stones needed for the ability.
     * Needs to include the stone that registers the ability.
     * e.g. A soul stone ability requiring the power stone would provide
     * required stones as so: {@code () -> List.of(ModStones.POWER, ModStones.SOUL)}
     * No requirements as so: {@code List::of}
     */
    private final Supplier<List<InfinityStoneType>> requiredStones;

    public PassiveAbility(Identifier id, AbilityIcon icon, Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones) {
        this.id = id;
        this.key = "abilities." + id.getNamespace() + "." + id.getPath();
        this.icon = icon;
        this.color = color;
        this.requiredStones = requiredStones;
    }

    @Override
    public Identifier getId() { return id; }

    @Override
    public AbilityIcon getIcon() { return icon; }

    @Override
    public String getName() { return Text.translatable(key).getString(); }

    @Override
    public int getARGBColor() { return 0xFF000000 | getRGBColor(); }

    @Override
    public int getRGBColor() { return color.get(); }

    @Override
    public boolean meetsCondition(List<InfinityStoneType> activeStones) {
        return new HashSet<>(activeStones).containsAll(requiredStones.get());
    }

    /**
     * The function that runs while this ability is active.
     * Dispatched from the server, so all logic contained within should
     * be server-side.
     * @param world        The world in which the ability was used.
     * @param player       The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     */
    public void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {}
}
