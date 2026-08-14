package net.alvin.infinityforge.compat.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.alvin.infinityforge.config.client.InfinityForgeConfigScreen;

public class InfinityForgeModMenuPlugin implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return InfinityForgeConfigScreen::build;
    }
}
