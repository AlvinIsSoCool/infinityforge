package net.alvin.infinityforge.infinity.abilities.impl.reality;

import net.alvin.infinityforge.block.entity.FakeBlockEntity;
import net.alvin.infinityforge.config.client.InfinityForgeClientConfig;
import net.alvin.infinityforge.infinity.abilities.base.AbilityDynamicIcon;
import net.alvin.infinityforge.infinity.abilities.icon.AbilityIcon;
import net.alvin.infinityforge.infinity.abilities.base.AbilityState;
import net.alvin.infinityforge.infinity.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.item.FakeItem;
import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.particle.InfinityDustParticleEffect;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public class SpawnBlockAbility extends ActiveAbility
        implements AbilityState<Block>, AbilityDynamicIcon<Block> {
    private final boolean spawnFake;

    public SpawnBlockAbility(Identifier id, AbilityIcon icon,
                             Supplier<Integer> color, Supplier<List<InfinityStoneType>> requiredStones,
                             int cooldownTicks, boolean spawnFake) {
        super(id, icon, color, requiredStones, cooldownTicks);
        this.spawnFake = spawnFake;
    }

    @Override
    public boolean onActivate(ServerWorld world, ServerPlayerEntity player, List<InfinityStoneType> activeStones) {
        Block selectedBlock = getState(player);
        BlockHitResult hit = (BlockHitResult) player.raycast(5.0, 1.0f, false);
        boolean missed = (hit.getType() != HitResult.Type.BLOCK);
        BlockPos hitPos = hit.getBlockPos();

        if (player.isSneaking()) {
            if (missed) {
                // Sneaking and looking away.
                if (selectedBlock != null) {
                    // Has a selection already.
                    BlockPos origin = player.getBlockPos();
                    World playerWorld = player.getWorld();
                    int r = 5;
                    ParticleEffect effect = new InfinityDustParticleEffect(Vec3d.unpackRgb(
                            InfinityForgeClientConfig.get().stoneGlintColors.realityStone).toVector3f(),
                            1.0f, true, false);

                    for (int x = -r; x <= r; x++) {
                        for (int z = -r; z <= r; z++) {
                            if (x*x + z*z > r*r) continue;
                            BlockPos surface = new BlockPos(origin.getX() + x, origin.getY() - 1, origin.getZ() + z);
                            if (spawnFake) FakeBlockEntity.place(playerWorld, surface, selectedBlock);
                            else playerWorld.setBlockState(surface, selectedBlock.getDefaultState());

                            double cx = surface.getX() + 0.5;
                            double cy = surface.getY() + 0.5;
                            double cz = surface.getZ() + 0.5;

                            world.spawnParticles(effect, cx, cy, cz, 50, 0.3, 0.4, 0.3, 0.005);
                            world.spawnParticles(effect, cx, cy, cz, 50, 0.5, 0.6, 0.5, 0.025);
                        }
                    }
                    return true;
                } else {
                    // Has no selection.
                    player.sendMessage(Text.literal("No blocks selected! Try sneaking and selecting a block."), true);
                    return false;
                }
            }

            Block block = world.getBlockState(hitPos).getBlock();
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
            if (spawnFake) FakeBlockEntity.place(world, hitPos, selectedBlock);
            else world.setBlockState(hitPos, selectedBlock.getDefaultState());
            double cx = hitPos.getX() + 0.5;
            double cy = hitPos.getY() + 0.5;
            double cz = hitPos.getZ() + 0.5;
            ParticleEffect effect = new InfinityDustParticleEffect(Vec3d.unpackRgb(
                    InfinityForgeClientConfig.get().stoneGlintColors.realityStone).toVector3f(),
                    1.0f, true, false);
            world.spawnParticles(effect, cx, cy, cz, 50, 0.3, 0.4, 0.3, 0.005);
            world.spawnParticles(effect, cx, cy, cz, 50, 0.5, 0.6, 0.5, 0.025);
            return true;
        }
    }

    @Override
    public Class<Block> getType() { return Block.class; }

    @Override
    public ItemStack getDynamicIcon(Block state) {
        return state == null ? ModItems.REALITY_STONE.getDefaultStack()
                : spawnFake ? FakeItem.create(state.asItem(), 1) : new ItemStack(state);
    }
}
