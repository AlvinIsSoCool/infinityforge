package net.alvin.infinityforge.infinity.abilities.impl.reality;

import net.alvin.infinityforge.infinity.abilities.ext.StatefulAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.List;
import java.util.function.Supplier;

public class SpawnBlockAbility extends StatefulAbility<Block> {
    private final boolean spawnFake;

    public SpawnBlockAbility(Identifier id, Identifier icon,
                             String key, int color,
                             Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks,
                             boolean spawnFake) {
        super(id, icon, key, color, requiredStones, cooldownTicks);
        this.spawnFake = spawnFake;
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        Block selected = getState(player);
        BlockHitResult hit = (BlockHitResult) player.raycast(5.0, 1.0f, false);
        boolean missed = hit.getType() != HitResult.Type.BLOCK;

        if (player.isSneaking()) {
            if (missed) {
                // Sneaking and looking away.
                if (selected != null) {
                    // Has a selection already.
                    player.sendMessage(Text.literal("You will get all the blocks!"), true);
                    return true;
                } else {
                    // Has no selection.
                    player.sendMessage(Text.literal("No blocks selected! Try sneaking and selecting a block."), true);
                    return false;
                }
            }

            Block block = world.getBlockState(hit.getBlockPos()).getBlock();
            setState(player, block);
            player.sendMessage(Text.literal("Selected: " +
                    block.getName().getString() + " (" + Registries.BLOCK.getId(block) + ")"), true);
            return false;
        } else {
            if (selected == null) {
                player.sendMessage(Text.literal("No blocks selected! Try sneaking and selecting a block."), true);
                return false;
            }
            world.setBlockState(hit.getBlockPos(), selected.getDefaultState());
            return true;
        }
    }
}
