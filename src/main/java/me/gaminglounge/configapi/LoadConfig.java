package me.gaminglounge.configapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class LoadConfig {
    private Gson gson;
    private Map<String, JsonObject> configs;
    private File configDir;
    
    public LoadConfig() {
        gson = new Gson();
        configs = new HashMap<>();
        configDir = ConfigAPI.INSTANCE.getDataFolder();

        loadConfig();
    }

    public void loadConfig() {
        Map<String, JsonObject> tmp = new HashMap<>();

        // creating plugin folder
        if (!configDir.exists()) configDir.mkdir();

        for (var plugin:configDir.listFiles()) {
            if (plugin.isFile()) continue;

            // Loading lang files
            {
                File langDir = new File(plugin, "lang");
                if (!langDir.exists()) langDir.mkdir();

                for (var file:langDir.listFiles()) {
                    if (!file.isFile()) continue;

                    ConfigAPI.INSTANCE.getLogger().log(Level.WARNING, file.getName());

                    try (InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8")) {
                        tmp.put(plugin.getName()  + ":" + file.getName().replace(".json", ""), gson.fromJson(isr, JsonObject.class));
                    } catch (JsonSyntaxException syntaxException) {
                        ConfigAPI.INSTANCE.getLogger().log(Level.SEVERE, "Synxtax error while loading the language file \"" + file.getName() + "\".", syntaxException);
                    } catch (IOException ioe) {
                        ConfigAPI.INSTANCE.getLogger().log(Level.SEVERE, "Coud not load the language \"" + file.getName() + "\".", ioe);
                    }
                }
            }
        }

        this.configs = tmp;
        ConfigAPI.INSTANCE.getLogger().log(Level.INFO, this.configs.toString());

    }
}
