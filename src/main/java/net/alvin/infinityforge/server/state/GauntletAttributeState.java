package net.alvin.infinityforge.server.state;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.HashSet;
import java.util.Set;

public class GauntletAttributeState {
    private static final Set<String> ACTIVE_MODIFIERS = new HashSet<>();

    private static String key(PlayerEntity player, Identifier abilityId) {
        return player.getUuidAsString() + ":" + abilityId;
    }

    public static void markActive(PlayerEntity player, Identifier abilityId) {
        ACTIVE_MODIFIERS.add(key(player, abilityId));
    }

    public static void markInactive(PlayerEntity player, Identifier abilityId) {
        ACTIVE_MODIFIERS.remove(key(player, abilityId));
    }

    public static boolean isActive(PlayerEntity player, Identifier abilityId) {
        return ACTIVE_MODIFIERS.contains(key(player, abilityId));
    }

    public static Set<Identifier> getActive(PlayerEntity player) {
        Set<Identifier> result = new HashSet<>();
        String prefix = player.getUuidAsString() + ":";
        for (String key : ACTIVE_MODIFIERS) {
            if (key.startsWith(prefix)) {
                result.add(new Identifier(key.substring(prefix.length())));
            }
        }
        return result;
    }

    public static void clear(PlayerEntity player) {
        ACTIVE_MODIFIERS.removeIf(key -> key.startsWith(player.getUuidAsString() + ":"));
    }
}
