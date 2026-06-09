package net.alvin.infinityforge.server.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.world.data.InfinityStoneWorldGenState;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LocateStonesCommand {
    public static final String[] stoneNames = {"power", "space", "reality", "soul", "time", "mind"};

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        var command = CommandManager.literal("locatestones")
                .requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                .executes(LocateStonesCommand::getNearbyStoneLocation);
        for (String stoneName : stoneNames) {
            command.then(CommandManager.literal(stoneName)
                    .executes(context ->
                            getSpecifiedStoneLocation(context, stoneName, false)));
        }
        command.then(CommandManager.literal("all").executes(LocateStonesCommand::getAllStoneLocations));
        dispatcher.register(command);
    }

    public static int getAllStoneLocations(CommandContext<ServerCommandSource> context) {
        for (String stoneName : stoneNames) {
            getSpecifiedStoneLocation(context, stoneName, true);
        }
        return 1;
    }

    public static int getNearbyStoneLocation(CommandContext<ServerCommandSource> context) {
        ServerWorld world = context.getSource().getServer().getOverworld();
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            context.getSource().sendError(Text.literal("This command can only be used by a player."));
            return 0;
        }
        InfinityStoneWorldGenState state = InfinityStoneWorldGenState.get(world);
        BlockPos playerPos = player.getBlockPos();
        Identifier currentDimension = player.getServerWorld().getRegistryKey().getValue();

        List<Map.Entry<BlockPos, Identifier>> positions = state.getAllSpawnedPositionsWithDimId();
        if (positions.isEmpty()) {
            context.getSource().sendError(Text.literal("No Stones have spawned in this world."));
            return 0;
        }

        Map.Entry<BlockPos, Identifier> nearest = positions.stream()
                .filter(e -> e.getValue().equals(currentDimension))
                .min(Comparator.comparingDouble(e -> e.getKey().getSquaredDistance(playerPos.toCenterPos())))
                .orElse(null);

        if (nearest == null) {
            context.getSource().sendError(Text.literal("No Stones have spawned in this world."));
            return 0;
        }

        BlockPos stonePos = nearest.getKey();
        String format = "Nearest Stone is at: [%d, %d, %d]";
        context.getSource().sendFeedback(() ->
                Text.literal(String.format(format, stonePos.getX(),
                        stonePos.getY(), stonePos.getZ())), false);
        return 1;
    }

    public static int getSpecifiedStoneLocation(CommandContext<ServerCommandSource> context, String stoneName, boolean includeDimId) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        if (player == null) {
            context.getSource().sendError(Text.literal("This command can only be used by a player."));
            return 0;
        }

        ServerWorld overworld = context.getSource().getServer().getOverworld();
        InfinityStoneWorldGenState state = InfinityStoneWorldGenState.get(overworld);
        BlockPos playerPos = player.getBlockPos();
        Identifier stoneId = new Identifier(InfinityForge.MOD_ID, stoneName);
        String stoneNameUpper = stoneName.toUpperCase();

        List<Map.Entry<BlockPos, Identifier>> positions = state.getSpawnedPositionsWithDimId(stoneId);
        if (positions.isEmpty()) {
            context.getSource().sendError(Text.literal("No " + stoneNameUpper + " Stone has spawned in this world."));
            return 0;
        }

        Map.Entry<BlockPos, Identifier> nearest = positions.stream()
                .min(Comparator.comparingDouble(e -> e.getKey().getSquaredDistance(playerPos.toCenterPos())))
                .get();
        BlockPos stonePos = nearest.getKey();

        boolean inSameDimension = player.getWorld().getRegistryKey().getValue().equals(nearest.getValue());
        if (includeDimId || !inSameDimension) {
            String format = "Nearest %s Stone is at: [%d, %d, %d] in dimension: [%s]";
            context.getSource().sendFeedback(() ->
                    Text.literal(String.format(format, stoneNameUpper, stonePos.getX(),
                            stonePos.getY(), stonePos.getZ(), nearest.getValue().toString())), false);
        } else {
            String format = "Nearest %s Stone is at: [%d, %d, %d]";
            context.getSource().sendFeedback(() ->
                    Text.literal(String.format(format, stoneNameUpper, stonePos.getX(),
                            stonePos.getY(), stonePos.getZ())), false);
        }

        return 1;
    }
}
