package net.alvin.infinityforge.config.client;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class InfinityForgeConfigScreen {
    public static Screen build(Screen parent) {
        InfinityForgeClientConfig config = InfinityForgeClientConfig.get();
        InfinityForgeClientConfig defaults = new InfinityForgeClientConfig();

        return YetAnotherConfigLib.createBuilder()
                .title(Text.of("InfinityForge"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("General"))
                        .group(stoneBaseColorsGroup(config, defaults))
                        .group(stoneGlintColorsGroup(config, defaults))
                        .group(abilityOutlineColorsGroup(config, defaults))
                        .build())
                .save(InfinityForgeClientConfig.HANDLER::save)
                .build()
                .generateScreen(parent);
    }

    private static OptionGroup stoneBaseColorsGroup(InfinityForgeClientConfig config, InfinityForgeClientConfig defaults) {
        InfinityForgeClientConfig.StoneBaseColors c = config.stoneBaseColors;
        InfinityForgeClientConfig.StoneBaseColors d = defaults.stoneBaseColors;
        return OptionGroup.createBuilder()
                .name(Text.of("Stone Base Colors"))
                .collapsed(false)
                .option(colorOption("Power Stone", "The base color of the power stone.",
                        d.powerStone, () -> c.powerStone, v -> c.powerStone = v))
                .option(colorOption("Space Stone", "The base color of the space stone.",
                        d.spaceStone, () -> c.spaceStone, v -> c.spaceStone = v))
                .option(colorOption("Reality Stone", "The base color of the reality stone.",
                        d.realityStone, () -> c.realityStone, v -> c.realityStone = v))
                .option(colorOption("Soul Stone", "The base color of the soul stone.",
                        d.soulStone, () -> c.soulStone, v -> c.soulStone = v))
                .option(colorOption("Mind Stone", "The base color of the mind stone.",
                        d.mindStone, () -> c.mindStone, v -> c.mindStone = v))
                .option(colorOption("Time Stone", "The base color of the time stone.",
                        d.timeStone, () -> c.timeStone, v -> c.timeStone = v))
                .build();
    }

    private static OptionGroup stoneGlintColorsGroup(InfinityForgeClientConfig config, InfinityForgeClientConfig defaults) {
        InfinityForgeClientConfig.StoneGlintColors c = config.stoneGlintColors;
        InfinityForgeClientConfig.StoneGlintColors d = defaults.stoneGlintColors;
        String suffix = " The tesseract uses this color as well.";
        return OptionGroup.createBuilder()
                .name(Text.of("Stone Glint Colors"))
                .collapsed(false)
                .option(colorOption("Power Stone", "The glint color of the power stone." + suffix,
                        d.powerStone, () -> c.powerStone, v -> c.powerStone = v))
                .option(colorOption("Space Stone", "The glint color of the space stone." + suffix,
                        d.spaceStone, () -> c.spaceStone, v -> c.spaceStone = v))
                .option(colorOption("Reality Stone", "The glint color of the reality stone." + suffix,
                        d.realityStone, () -> c.realityStone, v -> c.realityStone = v))
                .option(colorOption("Soul Stone", "The glint color of the soul stone." + suffix,
                        d.soulStone, () -> c.soulStone, v -> c.soulStone = v))
                .option(colorOption("Mind Stone", "The glint color of the mind stone." + suffix,
                        d.mindStone, () -> c.mindStone, v -> c.mindStone = v))
                .option(colorOption("Time Stone", "The glint color of the time stone." + suffix,
                        d.timeStone, () -> c.timeStone, v -> c.timeStone = v))
                .build();
    }

    private static OptionGroup abilityOutlineColorsGroup(InfinityForgeClientConfig config, InfinityForgeClientConfig defaults) {
        InfinityForgeClientConfig.AbilityOutlineColors c = config.abilityOutlineColors;
        InfinityForgeClientConfig.AbilityOutlineColors d = defaults.abilityOutlineColors;
        return OptionGroup.createBuilder()
                .name(Text.of("Ability Outline Colors"))
                .collapsed(false)
                .option(colorOption("Power Stone", "The color of the outline of the power stone abilities.",
                        d.powerStone, () -> c.powerStone, v -> c.powerStone = v))
                .option(colorOption("Space Stone", "The color of the outline of the space stone abilities.",
                        d.spaceStone, () -> c.spaceStone, v -> c.spaceStone = v))
                .option(colorOption("Reality Stone", "The color of the outline of the reality stone abilities.",
                        d.realityStone, () -> c.realityStone, v -> c.realityStone = v))
                .option(colorOption("Soul Stone", "The color of the outline of the soul stone abilities.",
                        d.soulStone, () -> c.soulStone, v -> c.soulStone = v))
                .option(colorOption("Mind Stone", "The color of the outline of the mind stone abilities.",
                        d.mindStone, () -> c.mindStone, v -> c.mindStone = v))
                .option(colorOption("Time Stone", "The color of the outline of the time stone abilities.",
                        d.timeStone, () -> c.timeStone, v -> c.timeStone = v))
                .build();
    }

    private static Option<Color> colorOption(String name, String description, int defaultValue,
                                             Supplier<Integer> getter, Consumer<Integer> setter) {
        return Option.<Color>createBuilder()
                .name(Text.of(name))
                .description(OptionDescription.of(Text.of(description)))
                .binding(new Color(defaultValue),
                        () -> new Color(getter.get()),
                        color -> setter.accept(color.getRGB() & 0xFFFFFF))
                .controller(ColorControllerBuilder::create)
                .build();
    }
}