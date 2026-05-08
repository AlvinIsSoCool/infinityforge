package net.alvin.infinityforge.abilities.impl.soul;

import net.alvin.infinityforge.abilities.base.ActiveAbility;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

public class SnapAbility extends ActiveAbility {
    public SnapAbility(Identifier id, Identifier icon, String key, int color, Supplier<List<InfinityStoneType>> requiredStones, int cooldownTicks) {
        super(id, icon, key, color, requiredStones, cooldownTicks);
    }

    @Override
    public boolean onActivate(World world, PlayerEntity player, List<InfinityStoneType> activeStones) {
        player.sendMessage(Text.literal("I AM INEVITABLE!"), true);
        return true;
    }
}
