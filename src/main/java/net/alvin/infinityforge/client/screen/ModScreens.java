package net.alvin.infinityforge.client.screen;

import net.alvin.infinityforge.registry.ModScreenHandlers;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ModScreens {
    public static void register() {
        HandledScreens.register(ModScreenHandlers.GAUNTLET_SCREEN_HANDLER, GauntletScreen::new);
        HandledScreens.register(ModScreenHandlers.BLUEPRINT_TABLE_SCREEN_HANDLER, BlueprintTableScreen::new);
    }
}
