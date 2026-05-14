package net.alvin.infinityforge.client.screen;

import net.alvin.infinityforge.registry.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ModScreens {
    public static void register() {
        HandledScreens.register(ModScreenHandlers.GAUNTLET, GauntletScreen::new);
    }
}
