package net.alvin.infinityforge.infinity.abilities.impl.reality;

import net.alvin.infinityforge.block.entity.FakeBlockEntity;
import net.alvin.infinityforge.infinity.abilities.base.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.AbilityState;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.item.FakeItem;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;

import java.util.List;
import java.util.function.Supplier;

public class SpawnBlockAbility extends ActiveAbility implements AbilityState<Block> {
    private final boolean spawnFake;

    public SpawnBlockAbility(Identifier id, AbilityIcon icon,
                             String key, Supplier<Integer> color,
                             Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks,
                             boolean spawnFake) {
        super(id, icon, key, color, requiredStones, cooldownTicks);
        this.spawnFake = spawnFake;
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        Block selectedBlock = getState(player);
        BlockHitResult hit = (BlockHitResult) player.raycast(5.0, 1.0f, false);
        boolean missed = (hit.getType() != HitResult.Type.BLOCK);

        if (player.isSneaking()) {
            if (missed) {
                // Sneaking and looking away.
                if (selectedBlock != null) {
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

            String format = "Selected: %s (%s)";
            Text message = Text.literal(String.format(format,
                    block.getName().getString(), Registries.BLOCK.getId(block)));
            player.sendMessage(message, true);
            return false;
        } else {
            if (selectedBlock == null) {
                player.sendMessage(Text.literal("No blocks selected! Try sneaking and selecting a block."), true);
                return false;
            }
            if (spawnFake) {
                FakeBlockEntity.place(world, hit.getBlockPos(), selectedBlock);
            } else {
                world.setBlockState(hit.getBlockPos(), selectedBlock.getDefaultState());
            }
            return true;
        }
    }

    @Override
    public Class<Block> getType() { return Block.class; }

    @Override
    public ItemStack getDynamicIconFromState(Block state) {
        return spawnFake ? FakeItem.create(state.asItem(), 1) : new ItemStack(state);
    }
}
