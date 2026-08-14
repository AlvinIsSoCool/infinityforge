package net.alvin.infinityforge.infinity.abilities.base;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.icon.AbilityIcon;
import net.alvin.infinityforge.network.s2c.SyncHeldForceStopS2CPacket;
import net.alvin.infinityforge.server.state.GauntletHeldState;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

public abstract non-sealed class HeldAbility implements GauntletAbility {
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
     * (Ex: abilities.infinityforge.power_tendrils)
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
     * <p>e.g. A space stone ability requiring the power stone would provide
     * required stones as so: {@code () -> List.of(ModStones.POWER, ModStones.SPACE)}<br>
     * No requirements as so: {@code List::of}</p>
     */
    private final Supplier<List<InfinityStoneType>> requiredStones;
    /**
     * Controls maximum charge for the ability.
     * -1 indicates no charge. Infinite usage.
     */
    private final int maxChargeTicks;
    /**
     * Controls charge refill speed:
     * <p>Positive: ticks per +1 charge (e.g. 2 = one charge every 2 ticks)<br>
     * Zero: no refill<br>
     * Negative: charges per tick (e.g. -4 = four charges per tick)</p>
     */
    private final int refillRateTicks;

    public HeldAbility(Identifier id, AbilityIcon icon, Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones, int maxChargeTicks, int refillRateTicks) {
        this.id = id;
        this.key = "abilities." + id.getNamespace() + "." + id.getPath();
        this.icon = icon;
        this.color = color;
        this.requiredStones = requiredStones;
        this.maxChargeTicks = maxChargeTicks;
        this.refillRateTicks = refillRateTicks;
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

    public int getMaxChargeTicks() { return maxChargeTicks; }
    public int getRefillRateTicks() { return refillRateTicks; }

    /**
     * The function that runs on first holding this ability.
     * Dispatched from the server, so all logic contained within should
     * be server-side.
     *
     * @param world        The world in which the ability was used.
     * @param player       The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     * @return {@code true} - Allows the ability to be held active.<br>
     *         {@code false} - Prevents the ability from being held active.
     */
    public abstract boolean onStart(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones);

    /**
     * The function that runs while this ability is held.
     * Dispatched from the server, so all logic contained within should
     * be server-side.
     *
     * @param world        The world in which the ability was used.
     * @param player       The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     */
    public abstract void onTick(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones);

    /**
     * The function that runs after the ability is not held.
     * Dispatched from the server, so all logic contained within should
     * be server-side.
     *
     * @param world        The world in which the ability was used.
     * @param player       The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     */
    public abstract void onStop(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones);

    /**
     * Force stops the held ability that runs it.
     * @param world        The world in which the ability was used.
     * @param player       The player entity that used the ability.
     * @param activeStones A list of all the infinity stones present in the infinity gauntlet
     *                     of the user of this ability.
     */
    public final void forceStop(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        if (!GauntletHeldState.isHeld(player, getId())) return;
        GauntletHeldState.setHeld(player, getId(), false);
        onStop(world, player, activeStones);
        ServerPlayNetworking.send(player, new SyncHeldForceStopS2CPacket(getId()));
    }
}
