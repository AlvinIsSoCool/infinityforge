package net.alvin.infinityforge.config.client;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;

public class InfinityForgeClientConfig {
    public static final ConfigClassHandler<InfinityForgeClientConfig> HANDLER =
            ConfigClassHandler.createBuilder(InfinityForgeClientConfig.class)
                    .id(new Identifier(InfinityForge.MOD_ID, "config"))
                    .serializer(config -> GsonConfigSerializerBuilder.create(config)
                            .setPath(FabricLoader.getInstance().getConfigDir().resolve(InfinityForge.MOD_ID + ".json5"))
                            .setJson5(true)
                            .build())
                    .build();
    @SerialEntry
    public StoneBaseColors stoneBaseColors = new StoneBaseColors();
    @SerialEntry
    public StoneGlintColors stoneGlintColors = new StoneGlintColors();
    @SerialEntry
    public AbilityOutlineColors abilityOutlineColors = new AbilityOutlineColors();

    public static class StoneBaseColors {
        @SerialEntry(comment = "The base color of the power stone.")
        public int powerStone = 0x6F00B8;
        @SerialEntry(comment = "The base color of the space stone.")
        public int spaceStone = 0x0A3CFF;
        @SerialEntry(comment = "The base color of the reality stone.")
        public int realityStone = 0x5A0000;
        @SerialEntry(comment = "The base color of the soul stone.")
        public int soulStone = 0xE53900;
        @SerialEntry(comment = "The base color of the mind stone.")
        public int mindStone = 0xE6C200;
        @SerialEntry(comment = "The base color of the time stone.")
        public int timeStone = 0x05A005;
    }

    public static class StoneGlintColors {
        @SerialEntry(comment = "The glint color of the power stone. The tesseract uses this color as well.")
        public int powerStone = 0x8700D3;
        @SerialEntry(comment = "The glint color of the space stone. The tesseract uses this color as well.")
        public int spaceStone = 0x0472FF;
        @SerialEntry(comment = "The glint color of the reality stone. The tesseract uses this color as well.")
        public int realityStone = 0xFF1E1E;
        @SerialEntry(comment = "The glint color of the soul stone. The tesseract uses this color as well.")
        public int soulStone = 0xFF6E00;
        @SerialEntry(comment = "The glint color of the mind stone. The tesseract uses this color as well.")
        public int mindStone = 0xFFF200;
        @SerialEntry(comment = "The glint color of the time stone. The tesseract uses this color as well.")
        public int timeStone = 0x00E600;
    }

    public static class AbilityOutlineColors {
        @SerialEntry(comment = "The color of the outline of the power stone abilities.")
        public int powerStone = 0x6F00B8;
        @SerialEntry(comment = "The color of the outline of the space stone abilities.")
        public int spaceStone = 0x0096FF;
        @SerialEntry(comment = "The color of the outline of the reality stone abilities.")
        public int realityStone = 0xFF1E1E;
        @SerialEntry(comment = "The color of the outline of the soul stone abilities.")
        public int soulStone = 0xFF6E00;
        @SerialEntry(comment = "The color of the outline of the mind stone abilities.")
        public int mindStone = 0xFFF200;
        @SerialEntry(comment = "The color of the outline of the time stone abilities.")
        public int timeStone = 0x00E600;
    }

    public static InfinityForgeClientConfig get() {
        return HANDLER.instance();
    }
}
