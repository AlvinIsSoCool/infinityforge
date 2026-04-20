package net.alvin.infinityforge.client.input;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

// GauntletKeybinds.java — keybind registration, holds SLOT_KEYS
public class GauntletKeybinds {
    public static final KeyBinding[] SLOT_KEYS = new KeyBinding[6];
    private static final int[] SLOT_GLFW_KEYS = {
            GLFW.GLFW_KEY_Z, GLFW.GLFW_KEY_X, GLFW.GLFW_KEY_C,
            GLFW.GLFW_KEY_V, GLFW.GLFW_KEY_B, GLFW.GLFW_KEY_N
    };

    public static void initialize() {
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
