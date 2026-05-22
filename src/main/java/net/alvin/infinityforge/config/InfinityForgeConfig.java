package net.alvin.infinityforge.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.alvin.infinityforge.InfinityForge;

@Config(name = InfinityForge.MOD_ID)
public class InfinityForgeConfig implements ConfigData {



    public static InfinityForgeConfig get() {
        return AutoConfig.getConfigHolder(InfinityForgeConfig.class).getConfig();
    }
}
