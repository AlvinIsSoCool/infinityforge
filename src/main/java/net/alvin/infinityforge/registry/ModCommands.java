package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.server.command.LocateStonesCommand;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ModCommands {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher,
                                                    registryAccess,
                                                    environment) ->
                LocateStonesCommand.register(dispatcher));
    }
}
