package net.alvin.infinityforge.config.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.alvin.infinityforge.InfinityForge;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Server-side config for InfinityForge. Read on both the dedicated server and the
 * integrated server (singleplayer). People can modify the file directly.
 */
public class InfinityForgeServerConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String HEADER = """
    // ======================================================================
    //  InfinityForge Server Configuration.
    //
    //  This file controls behavior consistent with server authority.
    //  Restart the server after editing for changes to take effect.
    //
    //  Note: any comments you add to this file will be removed
    //  the next time it is saved by the mod.
    // ======================================================================
    //
    // godMode (bool): Decides whether the fully-equipped infinity gauntlet
    //     should prevent all damage application to the player.
    //     Note: God Mode is absurdly powerful and will prevent every damage
    //     source from damaging the player, including /kill. Defaults to true.
    // ======================================================================
    """;
    public static InfinityForgeServerConfig INSTANCE;

    public boolean godMode = true;

    private static Path path() { return FabricLoader.getInstance().getConfigDir().resolve(
            InfinityForge.MOD_ID + "-server.json"); }
    public static void init() { INSTANCE = load(); }

    private static InfinityForgeServerConfig load() {
        Path path = path();
        if (Files.exists(path)) {
            try (var reader = Files.newBufferedReader(path)) {
                JsonObject raw = JsonParser.parseReader(reader).getAsJsonObject();
                InfinityForgeServerConfig loaded = GSON.fromJson(raw, InfinityForgeServerConfig.class);
                if (loaded == null) loaded = new InfinityForgeServerConfig();
                if (hasFieldMismatch(raw)) { loaded.save(); }
                return loaded;
            } catch (IOException | RuntimeException e) {
                InfinityForge.LOGGER.info("Server config load failed. Falling back to defaults!");
            }
        }
        InfinityForgeServerConfig defaults = new InfinityForgeServerConfig();
        defaults.save();
        return defaults;
    }


    public void save() {
        try (Writer w = Files.newBufferedWriter(path())) {
            w.write(HEADER);
            GSON.toJson(this, w);
        } catch (IOException e) {
            InfinityForge.LOGGER.info("Server config save failed.");
        }
    }

    private static boolean hasFieldMismatch(JsonObject raw) {
        JsonObject defaultKeys = GSON.toJsonTree(new InfinityForgeServerConfig()).getAsJsonObject();
        return !raw.keySet().equals(defaultKeys.keySet());
    }
}

