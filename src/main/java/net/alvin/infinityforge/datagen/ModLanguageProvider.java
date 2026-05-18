package net.alvin.infinityforge.datagen;

import net.alvin.infinityforge.registry.ModBlocks;
import net.alvin.infinityforge.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class ModLanguageProvider extends FabricLanguageProvider {
    public ModLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.POWER_STONE, "Power Stone");
        translationBuilder.add(ModItems.SPACE_STONE, "Space Stone");
        translationBuilder.add(ModItems.REALITY_STONE, "Reality Stone");
        translationBuilder.add(ModItems.SOUL_STONE, "Soul Stone");
        translationBuilder.add(ModItems.MIND_STONE, "Mind Stone");
        translationBuilder.add(ModItems.TIME_STONE, "Time Stone");
        translationBuilder.add(ModItems.POWER_TESSERACT, "Power Tesseract");
        translationBuilder.add(ModItems.SPACE_TESSERACT, "Space Tesseract");
        translationBuilder.add(ModItems.REALITY_TESSERACT, "Reality Tesseract");
        translationBuilder.add(ModItems.SOUL_TESSERACT, "Soul Tesseract");
        translationBuilder.add(ModItems.MIND_TESSERACT, "Mind Tesseract");
        translationBuilder.add(ModItems.TIME_TESSERACT, "Time Tesseract");
        translationBuilder.add(ModItems.INFINITY_GAUNTLET, "Infinity Gauntlet");
        translationBuilder.add(ModItems.INFINITY_GAUNTLET_BLUEPRINT, "Blueprint");
        translationBuilder.add(ModItems.RAW_TITANIUM, "Raw Titanium");
        translationBuilder.add(ModItems.TITANIUM_INGOT, "Titanium Ingot");
        translationBuilder.add(ModItems.GOLD_TITANIUM_ALLOY_INGOT, "Gold-Titanium Alloy Ingot");

        translationBuilder.add(ModBlocks.TITANIUM_ORE, "Titanium Ore");
        translationBuilder.add(ModBlocks.DEEPSLATE_TITANIUM_ORE, "Deepslate Titanium Ore");
        //translationBuilder.add(ModBlocks.BLUEPRINT_TABLE, "Blueprint Table");

        translationBuilder.add("itemgroup.infinity", "Infinity Forge");
        translationBuilder.add("category.infinityforge", "Ability Keybinds");

        translationBuilder.add("key.infinityforge.slot_1", "Ability 1");
        translationBuilder.add("key.infinityforge.slot_2", "Ability 2");
        translationBuilder.add("key.infinityforge.slot_3", "Ability 3");
        translationBuilder.add("key.infinityforge.slot_4", "Ability 4");
        translationBuilder.add("key.infinityforge.slot_5", "Ability 5");
        translationBuilder.add("key.infinityforge.slot_6", "Ability 6");

        translationBuilder.add("abilities.infinityforge.teleport", "Teleportation");
        translationBuilder.add("abilities.infinityforge.forcefield", "Forcefield");
        translationBuilder.add("abilities.infinityforge.weather", "Set Weather");
        translationBuilder.add("abilities.infinityforge.spawn_real_block", "Spawn Block");
        translationBuilder.add("abilities.infinityforge.kill", "Kill");
        translationBuilder.add("abilities.infinityforge.healing", "Healing");
        translationBuilder.add("abilities.infinityforge.health", "Health");
        translationBuilder.add("abilities.infinityforge.saturation", "Soul Sustenance");
        translationBuilder.add("abilities.infinityforge.water_breathing", "Water Breathing");
        translationBuilder.add("abilities.infinityforge.flight", "Flight");
        translationBuilder.add("abilities.infinityforge.snap", "Snap");
        translationBuilder.add("abilities.infinityforge.advance_time", "Advance Time");
        translationBuilder.add("abilities.infinityforge.rewind_time", "Rewind Time");
        translationBuilder.add("abilities.infinityforge.stop_time", "Stop Time");
        translationBuilder.add("abilities.infinityforge.randomise_time", "Randomise Time");
    }
}
