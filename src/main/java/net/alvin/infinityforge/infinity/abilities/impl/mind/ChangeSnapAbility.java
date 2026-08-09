package net.alvin.infinityforge.infinity.abilities.impl.mind;

import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.abilities.base.AbilityDynamicIcon;
import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.AbilityState;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.snap.SnapFunctions;
import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.server.state.GauntletAbilityStates;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;
import java.util.function.Supplier;

public class ChangeSnapAbility extends ActiveAbility
        implements AbilityState<SnapFunctions>, AbilityDynamicIcon<SnapFunctions> {
    public ChangeSnapAbility(Identifier id, AbilityIcon icon, Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks) {
        super(id, icon, color, requiredStones, cooldownTicks);
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        SnapFunctions snapFunction = getNextSnapFunction(player);
        player.sendMessage(
                Text.literal("Selected Function: ").append(Text.translatable(snapFunction.key)),
                true
        );
        return false;
    }

    private SnapFunctions getNextSnapFunction(PlayerEntity player) {
        SnapFunctions last = GauntletAbilityStates.get(player, this.getId(), SnapFunctions.class);
        SnapFunctions[] values = SnapFunctions.values();
        int nextOrdinal = (last == null ? -1 : last.ordinal()) + 1;
        if (nextOrdinal >= values.length) {
            nextOrdinal = 0;
        }
        SnapFunctions next = values[nextOrdinal];
        GauntletAbilityStates.set(player, this.getId(), next);
        return next;
    }

    @Override
    public Class<SnapFunctions> getType() {
        return SnapFunctions.class;
    }

    @Override
    public ItemStack getDynamicIcon(SnapFunctions state) {
        return ModItems.MIND_STONE.getDefaultStack();
    }
}