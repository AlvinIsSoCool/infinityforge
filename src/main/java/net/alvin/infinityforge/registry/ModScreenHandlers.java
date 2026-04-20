package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.alvin.infinityforge.screen.GauntletScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<GauntletScreenHandler> GAUNTLET = Registry.register(
            Registries.SCREEN_HANDLER,
            new Identifier(InfinityForge.MOD_ID, "gauntlet"),
            new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> {
                Hand hand = buf.readEnumConstant(Hand.class);
                ItemStack stack = inventory.player.getStackInHand(hand);
                return new GauntletScreenHandler(syncId, inventory, stack);
            })
    );

    public static void initialize() {
        System.out.println("Registering Screen Handlers for: " + InfinityForge.MOD_ID);
    }
}
