package net.alvin.infinityforge;

import net.alvin.infinityforge.abilities.*;
import net.alvin.infinityforge.client.hud.GauntletHudRenderer;
import net.alvin.infinityforge.client.screen.gauntlet.GauntletScreen;
import net.alvin.infinityforge.client.screen.ModScreenHandlers;
import net.alvin.infinityforge.client.state.GauntletClientState;
import net.alvin.infinityforge.events.InfinityForgeEvents;
import net.alvin.infinityforge.infinity.InfinityGauntletItem;
import net.alvin.infinityforge.packets.GauntletAbilityC2SPacket;
import net.alvin.infinityforge.packets.GauntletHeldC2SPacket;
import net.alvin.infinityforge.packets.GauntletToggleC2SPacket;
import net.alvin.infinityforge.packets.SyncToggleStateS2CPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class InfinityForgeClient implements ClientModInitializer {
    private static final KeyBinding[] SLOT_KEYS = new KeyBinding[6];
    private static final int[] SLOT_GLFW_KEYS = {
            GLFW.GLFW_KEY_G, GLFW.GLFW_KEY_H, GLFW.GLFW_KEY_J,
            GLFW.GLFW_KEY_K, GLFW.GLFW_KEY_L, GLFW.GLFW_KEY_M
    };

    @Override
    public void onInitializeClient() {
        InfinityForgeEvents.registerEventsClient();

        System.out.println("Client: Registering Ability KeyBindings for: " + InfinityForge.MOD_ID);
        registerKeyBindings();

        ClientPlayNetworking.registerGlobalReceiver(SyncToggleStateS2CPacket.TYPE,
                (packet, player, responseSender) -> {
                    if (packet.active()) GauntletClientState.activeToggles.add(packet.abilityId());
                    else GauntletClientState.activeToggles.remove(packet.abilityId());
                }
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
                if (client.player == null) return;

                ItemStack gauntlet = InfinityGauntletItem.findGauntlet(client.player);
                InfinityGauntletItem gauntletItem = gauntlet != null ? (InfinityGauntletItem) gauntlet.getItem() : null;

                List<GauntletAbility> abilities = gauntletItem != null
                        ? gauntletItem.getVisibleAbilities(gauntlet)
                        : List.of();

                if (abilities.isEmpty()) {
                    GauntletClientState.scrollOffset = 0;
                } else {
                    int max = Math.max(0, abilities.size() - 6);
                    GauntletClientState.scrollOffset = Math.min(
                            Math.max(GauntletClientState.scrollOffset, 0),
                            max
                    );
                }

                for (int slot = 0; slot < 6; slot++) {
                    int index = GauntletClientState.scrollOffset + slot;
                    if (index >= abilities.size()) continue;

                    GauntletAbility ability = abilities.get(index);

                    if (ability instanceof ActiveAbility a) {
                        while (SLOT_KEYS[slot].wasPressed())
                            ClientPlayNetworking.send(new GauntletAbilityC2SPacket(a.getId()));
                    } else if (ability instanceof ToggleAbility t) {
                        while (SLOT_KEYS[slot].wasPressed())
                            ClientPlayNetworking.send(new GauntletToggleC2SPacket(t.getId()));
                    } else if (ability instanceof HeldAbility h) {
                        boolean pressing = SLOT_KEYS[slot].isPressed();
                        boolean wasHeld = GauntletClientState.heldActive.contains(h.getId());

                        if (pressing && !wasHeld) {
                            GauntletClientState.heldActive.add(h.getId());
                            ClientPlayNetworking.send(new GauntletHeldC2SPacket(h.getId(), true));
                        } else if (!pressing && wasHeld) {
                            GauntletClientState.heldActive.remove(h.getId());
                            ClientPlayNetworking.send(new GauntletHeldC2SPacket(h.getId(), false));
                        }
                    }
                }
            }
        );

        System.out.println("Client: Registering Infinity Gauntlet Screen for: " + InfinityForge.MOD_ID);
        HandledScreens.register(ModScreenHandlers.GAUNTLET, GauntletScreen::new);

        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
            GauntletHudRenderer.render(drawContext);
        });
    }

    private static void registerKeyBindings() {
        for (int i = 0; i < 6; i++) {
            SLOT_KEYS[i] = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.infinityforge.slot_" + (i + 1),
                    InputUtil.Type.KEYSYM,
                    SLOT_GLFW_KEYS[i],
                    "category.infinityforge"
            ));
        }
    }
}
