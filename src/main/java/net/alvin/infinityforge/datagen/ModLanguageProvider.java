package net.alvin.infinityforge.datagen;

import net.alvin.infinityforge.block.ModBlocks;
import net.alvin.infinityforge.entity.ModEntities;
import net.alvin.infinityforge.item.ModItems;
import net.alvin.infinityforge.entity.effect.ModStatusEffects;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class ModLanguageProvider extends FabricLanguageProvider {
    public ModLanguageProvider(FabricDataOutput dataOutput) { super(dataOutput, "en_us"); }

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
        translationBuilder.add(ModItems.BLUEPRINT, "Blueprint");
        translationBuilder.add(ModItems.RAW_TITANIUM, "Raw Titanium");
        translationBuilder.add(ModItems.TITANIUM_INGOT, "Titanium Ingot");
        translationBuilder.add(ModItems.GOLD_TITANIUM_ALLOY_INGOT, "Gold-Titanium Alloy Ingot");

        translationBuilder.add(ModBlocks.TITANIUM_ORE, "Titanium Ore");
        translationBuilder.add(ModBlocks.DEEPSLATE_TITANIUM_ORE, "Deepslate Titanium Ore");
        translationBuilder.add(ModBlocks.BLUEPRINT_TABLE, "Blueprint Table");
        translationBuilder.add(ModBlocks.FAKE_BLOCK, "Fake Block");

        translationBuilder.add(ModStatusEffects.SNAP_EFFECT, "I don't feel so good...");
        translationBuilder.add(ModStatusEffects.HEALTH_DRAIN_EFFECT, "Health Drain");
        translationBuilder.add(ModStatusEffects.MOVEMENT_LOCKED_EFFECT, "Movement Locked");
        translationBuilder.add(ModStatusEffects.SCROLL_LOCKED_EFFECT, "Scroll Locked");

        translationBuilder.add(ModEntities.BLACKHOLE_ENTITY, "Black Hole");
        translationBuilder.add(ModEntities.PORTAL_ENTITY, "Portal");

        translationBuilder.add("death.attack.infinityforge.power_stone", "%1$s was obliterated by the power stone");
        translationBuilder.add("death.attack.infinityforge.black_hole", "%1$s was spaghettified by a black hole");
        translationBuilder.add("death.attack.infinityforge.health_drain", "%1$s ran out of hearts!");

        translationBuilder.add("gui.infinityforge.blueprint_table", "Blueprint Table");
        translationBuilder.add("itemgroup.infinity", "Infinity Forge");
        translationBuilder.add("category.infinityforge", "Ability Keybinds");

        translationBuilder.add("key.infinityforge.slot_1", "Ability 1");
        translationBuilder.add("key.infinityforge.slot_2", "Ability 2");
        translationBuilder.add("key.infinityforge.slot_3", "Ability 3");
        translationBuilder.add("key.infinityforge.slot_4", "Ability 4");
        translationBuilder.add("key.infinityforge.slot_5", "Ability 5");
        translationBuilder.add("key.infinityforge.slot_6", "Ability 6");

        translationBuilder.add("abilities.infinityforge.knockback_resistance", "Knockback Resistance");
        translationBuilder.add("abilities.infinityforge.speed", "Speed");
        translationBuilder.add("abilities.infinityforge.attack_speed", "Attack Speed");
        translationBuilder.add("abilities.infinityforge.step_height", "Step Height");
        translationBuilder.add("abilities.infinityforge.teleport", "Teleportation");
        translationBuilder.add("abilities.infinityforge.forcefield", "Forcefield");
        translationBuilder.add("abilities.infinityforge.portal", "Portal");
        translationBuilder.add("abilities.infinityforge.blackhole", "Black Hole");
        translationBuilder.add("abilities.infinityforge.phasing", "Phasing");
        translationBuilder.add("abilities.infinityforge.weather", "Set Weather");
        translationBuilder.add("abilities.infinityforge.turn_into_bubbles", "Turn Into Bubbles");
        translationBuilder.add("abilities.infinityforge.spawn_real_block", "Spawn Block");
        translationBuilder.add("abilities.infinityforge.spawn_real_item", "Spawn Item");
        translationBuilder.add("abilities.infinityforge.spawn_fake_block", "Spawn Fake Block");
        translationBuilder.add("abilities.infinityforge.spawn_fake_item", "Spawn Fake Item");
        translationBuilder.add("abilities.infinityforge.invisibility", "Invisibility");
        translationBuilder.add("abilities.infinityforge.size_change_small", "Shrink");
        translationBuilder.add("abilities.infinityforge.size_change_big", "Enlarge");
        translationBuilder.add("abilities.infinityforge.kill", "Kill");
        translationBuilder.add("abilities.infinityforge.healing", "Healing");
        translationBuilder.add("abilities.infinityforge.health", "Health");
        translationBuilder.add("abilities.infinityforge.saturation", "Self-Sustenance");
        translationBuilder.add("abilities.infinityforge.water_breathing", "Water Breathing");
        translationBuilder.add("abilities.infinityforge.flight", "Flight");
        translationBuilder.add("abilities.infinityforge.snap", "Snap");
        translationBuilder.add("abilities.infinityforge.change_snap", "Change Snap Function");
        translationBuilder.add("abilities.infinityforge.advance_time", "Advance Time");
        translationBuilder.add("abilities.infinityforge.rewind_time", "Rewind Time");
        translationBuilder.add("abilities.infinityforge.stop_time", "Stop Time");
        translationBuilder.add("abilities.infinityforge.randomise_time", "Randomise Time");

        translationBuilder.add("snapfunctions.infinityforge.kill_half", "Kill Half");
        translationBuilder.add("snapfunctions.infinityforge.kill_all", "Kill All");
        translationBuilder.add("snapfunctions.infinityforge.kill_hostiles", "Kill Hostile Mobs");
        translationBuilder.add("snapfunctions.infinityforge.revert_kills", "Bring Back The Dead");
        translationBuilder.add("snapfunctions.infinityforge.recreate_world", "Recreate the world (N/A)");
        translationBuilder.add("snapfunctions.infinityforge.destroy_stones", "Destroy The Stones");
        translationBuilder.add("snapfunctions.infinityforge.creative_mode", "Switch To Creative Mode");

        translationBuilder.add("snapmessages.infinityforge.missing", "No Snap Function Selected!");
        translationBuilder.add("snapmessages.infinityforge.kill_half", "%s: You should have gone for the head.");
        translationBuilder.add("snapmessages.infinityforge.kill_all", "%s: I AM... INEVITABLE!");
        translationBuilder.add("snapmessages.infinityforge.kill_hostiles", "%s: And I am... Iron Man...!");
        translationBuilder.add("snapmessages.infinityforge.destroy_stones", "%s: I used the stones... to destroy the stones!");

        translationBuilder.add("text.autoconfig.infinityforge.title", "Infinity Forge Options");
        translationBuilder.add("text.autoconfig.infinityforge.category.color_options", "Color Options");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.stoneBaseColors", "Infinity Stone Base Colors");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.stoneGlintColors", "Infinity Stone Glint Colors");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.abilityOutlineColors", "Ability Outline Colors");

        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.stoneBaseColors.powerStone", "Power Stone Base Color");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.stoneBaseColors.spaceStone", "Space Stone Base Color");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.stoneBaseColors.realityStone", "Reality Stone Base Color");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.stoneBaseColors.soulStone", "Soul Stone Base Color");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.stoneBaseColors.mindStone", "Mind Stone Base Color");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.stoneBaseColors.timeStone", "Time Stone Base Color");

        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.stoneGlintColors.powerStone", "Power Stone Glint Color");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.stoneGlintColors.spaceStone", "Space Stone Glint Color");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.stoneGlintColors.realityStone", "Reality Stone Glint Color");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.stoneGlintColors.soulStone", "Soul Stone Glint Color");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.stoneGlintColors.mindStone", "Mind Stone Glint Color");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.stoneGlintColors.timeStone", "Time Stone Glint Color");

        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.abilityOutlineColors.powerStone", "Power Stone Ability Color");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.abilityOutlineColors.spaceStone", "Space Stone Ability Color");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.abilityOutlineColors.realityStone", "Reality Stone Ability Color");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.abilityOutlineColors.soulStone", "Soul Stone Ability Color");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.abilityOutlineColors.mindStone", "Mind Stone Ability Color");
        translationBuilder.add("text.autoconfig.infinityforge.option.colorOptions.abilityOutlineColors.timeStone", "Time Stone Ability Color");
    }
}
