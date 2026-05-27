package net.alvin.infinityforge.world.event;

import net.alvin.infinityforge.world.gen.CrystalHypercubeProcessor;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;

public class WorldGenEventHandler {
    public static void register() {
        ServerLifecycleEvents.SERVER_STOPPING
                .register(server -> CrystalHypercubeProcessor.clearState());
    }
}
