package net.alvin.infinityforge;

import net.alvin.infinityforge.abilities.GauntletAbility;
import net.alvin.infinityforge.abilities.ModAbilities;
import net.alvin.infinityforge.client.screen.GauntletScreen;
import net.alvin.infinityforge.client.screen.ModScreenHandlers;
import net.alvin.infinityforge.events.InfinityForgeEvents;
import net.alvin.infinityforge.packets.GauntletAbilityC2SPacket;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

public class InfinityForgeClient implements ClientModInitializer {
    public static final Map<Identifier, KeyBinding> ABILITY_KEYS = new HashMap<>();

    @Override
    public void onInitializeClient() {
        InfinityForgeEvents.registerEventsClient();

        System.out.println("Client: Registering Ability KeyBindings for: " + InfinityForge.MOD_ID);
        registerAbilityKey(ModAbilities.TEST, GLFW.GLFW_KEY_J);
        registerAbilityKey(ModAbilities.TEST2, GLFW.GLFW_KEY_G);
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            ABILITY_KEYS.forEach(
                    (abilityId, key) -> {
                        while (key.wasPressed()) {
                            System.out.println("Client: Key binding pressed!");
                            ClientPlayNetworking.send(new GauntletAbilityC2SPacket(abilityId));
                        }
                    }
            );
        });

        System.out.println("Client: Registering Infinity Gauntlet Screen for: " + InfinityForge.MOD_ID);
        HandledScreens.register(ModScreenHandlers.GAUNTLET, GauntletScreen::new);
    }

    private static void registerAbilityKey(GauntletAbility ability, int glfwKey) {
        KeyBinding key = KeyBindingHelper.registerKeyBinding(
                new KeyBinding(
                        "key." + InfinityForge.MOD_ID + "." + ability.getId().getPath(),
                        InputUtil.Type.KEYSYM,
                        glfwKey,
                        "category." + InfinityForge.MOD_ID
                )
        );
        ABILITY_KEYS.put(ability.getId(), key);
    }
}
