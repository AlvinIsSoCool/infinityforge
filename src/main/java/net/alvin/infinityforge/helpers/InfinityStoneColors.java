package net.alvin.infinityforge.helpers;

public class InfinityStoneColors {
    public static final int POWER_STONE_BASE_COLOR = 0x8700D3;
    public static final int SPACE_STONE_BASE_COLOR = 0x0A3CFF;
    public static final int REALITY_STONE_BASE_COLOR = 0x5A0000;
    public static final int SOUL_STONE_BASE_COLOR = 0xE53900;
    public static final int MIND_STONE_BASE_COLOR = 0xE6C200;
    public static final int TIME_STONE_BASE_COLOR = 0x05A005;

    public static final int POWER_STONE_GLINT_COLOR = 0x6F00B8;
    public static final int SPACE_STONE_GLINT_COLOR = 0x0096FF;
    public static final int REALITY_STONE_GLINT_COLOR = 0xFF1E1E;
    public static final int SOUL_STONE_GLINT_COLOR = 0xFF5A00;
    public static final int MIND_STONE_GLINT_COLOR = 0xFFF200;
    public static final int TIME_STONE_GLINT_COLOR = 0x00E600;

    public static final int ALPHA_MASK = 0xFF000000;
    public static final int DEFAULT_ABILITY_COLOR = 0;
    public static final int POWER_STONE_ABILITY_COLOR = ALPHA_MASK | POWER_STONE_GLINT_COLOR;
    public static final int SPACE_STONE_ABILITY_COLOR = ALPHA_MASK | SPACE_STONE_GLINT_COLOR;
    public static final int REALITY_STONE_ABILITY_COLOR = ALPHA_MASK | REALITY_STONE_GLINT_COLOR;
    public static final int SOUL_STONE_ABILITY_COLOR = ALPHA_MASK | SOUL_STONE_BASE_COLOR;
    public static final int MIND_STONE_ABILITY_COLOR = ALPHA_MASK | MIND_STONE_GLINT_COLOR;
    public static final int TIME_STONE_ABILITY_COLOR = ALPHA_MASK | TIME_STONE_GLINT_COLOR;
}
