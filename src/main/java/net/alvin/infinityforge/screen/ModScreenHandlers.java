package net.alvin.infinityforge.screen;

import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static final ScreenHandlerType<GauntletScreenHandler> GAUNTLET_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER,
                    new Identifier(InfinityForge.MOD_ID, "gauntlet_sh"),
                    new ExtendedScreenHandlerType<>((syncId, inventory, buf) -> {
                        Hand hand = buf.readEnumConstant(Hand.class);
                        ItemStack stack = inventory.player.getStackInHand(hand);
                        return new GauntletScreenHandler(syncId, inventory, stack);
                    })
            );
    public static final ScreenHandlerType<BlueprintTableScreenHandler> BLUEPRINT_TABLE_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER,
                    new Identifier(InfinityForge.MOD_ID, "blueprint_table_sh"),
                    new ExtendedScreenHandlerType<>(BlueprintTableScreenHandler::new)
            );

    public static final ScreenHandlerType<ItemSelectionScreenHandler> ITEM_SELECTION_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER,
                    new Identifier(InfinityForge.MOD_ID, "item_selection_sh"),
                    new ExtendedScreenHandlerType<>(ItemSelectionScreenHandler::new)
            );

    public static void initialize() {
        InfinityForge.LOGGER.info("Registering Screen Handlers for: {}", InfinityForge.MOD_ID);
    }
}
