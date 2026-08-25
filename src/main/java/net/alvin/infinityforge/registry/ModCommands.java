package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.server.command.LocateStonesCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {
    public static void register() {
        InfinityForge.LOGGER.info("Registering Commands for: {}", InfinityForge.MOD_ID);
        CommandRegistrationCallback.EVENT.register((dispatcher,
                                                    registryAccess,
                                                    environment) ->
                LocateStonesCommand.register(dispatcher));
    }
}
