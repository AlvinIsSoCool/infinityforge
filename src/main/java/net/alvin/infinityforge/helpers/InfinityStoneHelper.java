package net.alvin.infinityforge.helpers;

import net.alvin.infinityforge.item.InfinityStoneItem;
import net.alvin.infinityforge.infinity.InfinityStoneType;
import net.alvin.infinityforge.registry.ModStones;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;

public class InfinityStoneHelper {
    public static boolean isHoldingInfinityStone(PlayerEntity player) {
        return getHoldingStoneType(player) != null;
    }

    public static boolean isHoldingStoneType(PlayerEntity player, InfinityStoneType stoneType) {
        return stoneType == getHoldingStoneType(player);
    }

    public static boolean isHoldingPowerStone(PlayerEntity player) {
        return isHoldingStoneType(player, ModStones.POWER);
    }

    public static boolean isHoldingSpaceStone(PlayerEntity player) {
        return isHoldingStoneType(player, ModStones.SPACE);
    }

    public static boolean isHoldingRealityStone(PlayerEntity player) {
        return isHoldingStoneType(player, ModStones.REALITY);
    }

    public static boolean isHoldingSoulStone(PlayerEntity player) {
        return isHoldingStoneType(player, ModStones.SOUL);
    }

    public static boolean isHoldingMindStone(PlayerEntity player) {
        return isHoldingStoneType(player, ModStones.MIND);
    }

    public static boolean isHoldingTimeStone(PlayerEntity player) {
        return isHoldingStoneType(player, ModStones.TIME);
    }

    public static InfinityStoneType getHoldingStoneType(PlayerEntity player) {
        if (player.getMainHandStack().getItem() instanceof InfinityStoneItem stone) {
            return stone.getStoneType();
        }
        else if (player.getOffHandStack().getItem() instanceof InfinityStoneItem stone) {
            return stone.getStoneType();
        }
        else return null;
    }

    public static Hand getStoneHoldingHand(PlayerEntity player) {
        if (player.getMainHandStack().getItem() instanceof InfinityStoneItem stone) {
            return Hand.MAIN_HAND;
        }
        if (player.getOffHandStack().getItem() instanceof InfinityStoneItem stone) {
            return Hand.OFF_HAND;
        }
        else return null;
    }
}
