package net.alvin.infinityforge.infinity.abilities.impl.soul;

import net.alvin.infinityforge.infinity.abilities.icon.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.infinity.snap.SnapFunctions;
import net.alvin.infinityforge.infinity.snap.SnapFunctionsHelper;
import net.alvin.infinityforge.infinity.abilities.impl.ModGauntletAbilities;
import net.alvin.infinityforge.registry.ModSounds;
import net.alvin.infinityforge.server.state.GauntletAbilityStates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;

import java.util.List;
import java.util.function.Supplier;

public class SnapAbility extends ActiveAbility {
    public SnapAbility(Identifier id, AbilityIcon icon,
                       Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones,
                       int cooldownTicks) {
        super(id, icon, color, requiredStones, cooldownTicks);
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        SnapFunctions snapFunction = GauntletAbilityStates.get(player,
                ModGauntletAbilities.CHANGE_SNAP.getId(), SnapFunctions.class);
        if (snapFunction != null) executeSnap(world, player, snapFunction);
        else player.sendMessage(
                Text.translatable("snapmessages.infinityforge.missing")
                        .formatted(Formatting.RED), true);
        return true;
    }

    public void executeSnap(ServerWorld world, ServerPlayerEntity player, SnapFunctions function) {
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.USE_GAUNTLET, SoundCategory.PLAYERS, 1f, 1f);
        switch (function) {
            case KILL_HALF -> SnapFunctionsHelper.killHalf(world, player);
            case KILL_ALL -> SnapFunctionsHelper.killAll(world, player);
            case KILL_HOSTILES -> SnapFunctionsHelper.killHostiles(world, player);
            case REVERT_KILLS -> SnapFunctionsHelper.revertKills(world, player);
            case RECREATE_WORLD -> {}
            case DESTROY_STONES -> SnapFunctionsHelper.destroyStones(player);
            case CREATIVE_MODE -> {
                if (player.interactionManager.getGameMode() != GameMode.CREATIVE)
                    player.changeGameMode(GameMode.CREATIVE);
                else player.changeGameMode(GameMode.SURVIVAL);
            }
        }
    }
}
