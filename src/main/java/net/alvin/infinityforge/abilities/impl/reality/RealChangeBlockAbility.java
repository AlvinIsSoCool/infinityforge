package net.alvin.infinityforge.abilities.impl.reality;

import net.alvin.infinityforge.abilities.ext.StatefulAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public class RealChangeBlockAbility extends StatefulAbility<Block> {

    public RealChangeBlockAbility(Identifier id, Identifier icon,
                                  String key, int color,
                                  Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks) {
        super(id, icon, key, color, requiredStones, cooldownTicks);
    }

    @Override
    public void onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones) {
        Block selected = getState(player);
        BlockHitResult hit = (BlockHitResult) player.raycast(5.0, 1.0f, false);
        boolean missed = hit.getType() != HitResult.Type.BLOCK;

        if (player.isSneaking()) {
            if (missed) {
                // sneaking, looking away
                if (selected != null) {
                    // has a selection already, just notify
                    player.sendMessage(Text.literal("You will get all the blocks!"), true);
                } else {
                    // Has no selection.
                    player.sendMessage(Text.literal("No blocks selected! Try sneaking and selecting a block."), true);
                }
                return;
            }

            Block block = world.getBlockState(hit.getBlockPos()).getBlock();
            setState(player, block);
            player.sendMessage(Text.literal("Selected: " +
                    block.getName().getString() + " (" + Registries.BLOCK.getId(block) + ")"), true);
        } else {
            if (selected == null) {
                player.sendMessage(Text.literal("No blocks selected! Try sneaking and selecting a block."), true);
                return;
            }
            if (missed) return;
            world.setBlockState(hit.getBlockPos(), selected.getDefaultState());
        }
    }
}
