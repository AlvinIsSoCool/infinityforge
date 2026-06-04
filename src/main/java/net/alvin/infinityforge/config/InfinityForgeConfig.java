package net.alvin.infinityforge.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
import net.alvin.infinityforge.InfinityForge;

@Config(name = InfinityForge.MOD_ID)
public class InfinityForgeConfig implements ConfigData {
    @Comment("Allows full infinity stone power and prevents death.")
    public boolean godMode = true;

    @ConfigEntry.Category("color_options")
    @ConfigEntry.Gui.TransitiveObject
    public ColorOptions colorOptions = new ColorOptions();

    public static class ColorOptions {
        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        public StoneBaseColors stoneBaseColors = new StoneBaseColors();

        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        public StoneGlintColors stoneGlintColors = new StoneGlintColors();

        @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
        public AbilityOutlineColors abilityOutlineColors = new AbilityOutlineColors();
    }

    public static class StoneBaseColors {
        @Comment("The base color of the power stone.")
        @ConfigEntry.ColorPicker
        public int powerStone = 0x8700D3;

        @Comment("The base color of the space stone.")
        @ConfigEntry.ColorPicker
        public int spaceStone = 0x0A3CFF;

        @Comment("The base color of the reality stone.")
        @ConfigEntry.ColorPicker
        public int realityStone = 0x5A0000;

        @Comment("The base color of the soul stone.")
        @ConfigEntry.ColorPicker
        public int soulStone = 0xE53900;

        @Comment("The base color of the mind stone.")
        @ConfigEntry.ColorPicker
        public int mindStone = 0xE6C200;

        @Comment("The base color of the time stone.")
        @ConfigEntry.ColorPicker
        public int timeStone = 0x05A005;
    }

    public static class StoneGlintColors {
        @Comment("The glint color of the power stone. The tesseract uses this color as well.")
        @ConfigEntry.ColorPicker
        public int powerStone = 0x6F00B8;

        @Comment("The glint color of the space stone. The tesseract uses this color as well.")
        @ConfigEntry.ColorPicker
        public int spaceStone = 0x0096FF;

        @Comment("The glint color of the reality stone. The tesseract uses this color as well.")
        @ConfigEntry.ColorPicker
        public int realityStone = 0xFF1E1E;

        @Comment("The glint color of the soul stone. The tesseract uses this color as well.")
        @ConfigEntry.ColorPicker
        public int soulStone = 0xFF6E00;

        @Comment("The glint color of the mind stone. The tesseract uses this color as well.")
        @ConfigEntry.ColorPicker
        public int mindStone = 0xFFF200;

        @Comment("The glint color of the time stone. The tesseract uses this color as well.")
        @ConfigEntry.ColorPicker
        public int timeStone = 0x00E600;
    }

    public static class AbilityOutlineColors {
        @Comment("The color of the outline of the power stone abilities.")
        @ConfigEntry.ColorPicker
        public int powerStone = 0x6F00B8;

        @Comment("The color of the outline of the space stone abilities.")
        @ConfigEntry.ColorPicker
        public int spaceStone = 0x0096FF;

        @Comment("The color of the outline of the reality stone abilities.")
        @ConfigEntry.ColorPicker
        public int realityStone = 0xFF1E1E;

        @Comment("The color of the outline of the soul stone abilities.")
        @ConfigEntry.ColorPicker
        public int soulStone = 0xFF6E00;

        @Comment("The color of the outline of the mind stone abilities.")
        @ConfigEntry.ColorPicker
        public int mindStone = 0xFFF200;

        @Comment("The color of the outline of the time stone abilities.")
        @ConfigEntry.ColorPicker
        public int timeStone = 0x00E600;
    }

    public static InfinityForgeConfig get() {
        return AutoConfig.getConfigHolder(InfinityForgeConfig.class).getConfig();
    }
}
