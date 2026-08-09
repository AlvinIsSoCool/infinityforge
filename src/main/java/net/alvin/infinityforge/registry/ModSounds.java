package net.alvin.infinityforge.registry;

import net.alvin.infinityforge.InfinityForge;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent ENERGY_BLAST = register("energy_blast");
    public static final SoundEvent EQUIP_STONE = register("equip_stone");
    public static final SoundEvent USE_GAUNTLET = register("use_gauntlet");

    private static SoundEvent register(String name) {
        Identifier id = new Identifier(InfinityForge.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void initialize() {
        InfinityForge.LOGGER.info("Registering sounds for: {}", InfinityForge.MOD_ID);
    }
}