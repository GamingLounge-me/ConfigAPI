package me.gaminglounge.configapi;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.plugin.Plugin;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

/**
 * Loading configuration
 * atm: langfiles
 */
public final class LoadConfig {
    /**
     * Map which hold the language configuration
     * Map -> key:String value:JsonObject
     * String: "plugin_namespace:lang_code" (langocde example: en_US, de_DE)
     * JsonObeject: Value of file
     */
    public Map<String, JsonObject> lang;
    private final File configDir;

    /**
     * init and load the configs
     */
    public LoadConfig() {
        lang = new HashMap<>();
        configDir = ConfigAPI.INSTANCE.getDataFolder();

        loadConfig();
    }

    /**
     * If you only need one language in your project use this if you need more pls
     * use the version
     * {@code registerLanguage(Plugin plugin, Map<String, InputStream> langs)}
     * 
     * @param plugin the plugin mainly used for namespaces
     * @param file   name of the stored configuration file
     * @param is     input of the original lang file
     * 
     */
    public static void registerLanguage(Plugin plugin, String file, InputStream is) {
        Map<String, InputStream> lang = new HashMap<>();
        lang.put(file, is);
        registerConfig(plugin, "lang", lang);
    }

    /**
     * Used for registration of multiple languages.
     * 
     * @param plugin the plugin mainly used for namespaces
     * @param langs  map of name of the stored file and the input if the original
     *               file
     * 
     */
    public static void registerLanguage(Plugin plugin, Map<String, InputStream> langs) {
        registerConfig(plugin, "lang", langs);
    }

    private static void registerConfig(Plugin plugin, String subFolder, Map<String, InputStream> langs) {
        File folder = new File(ConfigAPI.CONFIG.configDir, plugin.getName());
        if (!folder.exists())
            folder.mkdir();

        File subfolder = new File(folder, subFolder);
        if (!subfolder.exists())
            subfolder.mkdir();

        for (Map.Entry<String, InputStream> entry : langs.entrySet()) {
            File file = new File(subfolder, entry.getKey());
            if (!file.exists()) {
                writeConf(file, entry.getValue());
            } else {
                JsonObject json = loadFileToJsonObject(file);
                JsonObject n = loadJsonObjectFrominputStream(entry.getValue());

                if (json == null || n == null || json == n)
                    return;
                if (json.keySet().equals(n.keySet()))
                    return;

                // add new keys to the old JSonObject
                for (String a : n.keySet()) {
                    if (json.has(a))
                        continue;
                    json.add(a, n.get(a));
                }

                writeConf(file, new ByteArrayInputStream(json.toString().getBytes()));

            }
        }
        ConfigAPI.CONFIG.loadConfig();
    }

    private static void writeConf(File file, InputStream is) {
        try (
                FileOutputStream fos = new FileOutputStream(file);) {
            is.transferTo(fos);
            fos.flush();
        } catch (IOException ioe) {
            ConfigAPI.INSTANCE.getLogger().log(Level.WARNING, "Restart to try to write file again", ioe);
        }
    }

    /**
     * loading all configs
     */
    public void loadConfig() {
        // creating temporary map for config
        Map<String, JsonObject> tmpLang;
        tmpLang = new HashMap<>();

        // creating plugin folder
        if (!configDir.exists())
            configDir.mkdir();

        for (var plugin : configDir.listFiles()) {
            if (plugin.isFile())
                continue;

            // Loading lang files
            {
                File langDir = new File(plugin, "lang");
                if (!langDir.exists())
                    langDir.mkdir();

                for (var file : langDir.listFiles()) {
                    if (!file.isFile())
                        continue;

                    Map<String, JsonObject> a = loadFile(file, plugin.getName());
                    if (a != null)
                        tmpLang.putAll(a);
                }
            }
        }

        // saving loaded config
        this.lang = tmpLang;

    }

    private static JsonObject loadJsonObjectFrominputStream(InputStream is) {
        Gson gson = new Gson();
        try (InputStreamReader isr = new InputStreamReader(is, "utf-8")) {
            return gson.fromJson(isr, JsonObject.class);
        } catch (JsonSyntaxException syntaxException) {
            ConfigAPI.INSTANCE.getLogger().log(Level.SEVERE, "Synxtax error while loading config file",
                    syntaxException);
            return null;
        } catch (IOException ioe) {
            ConfigAPI.INSTANCE.getLogger().log(Level.SEVERE, "Coud not load config file", ioe);
            return null;
        }
    }

    private static JsonObject loadFileToJsonObject(File file) {
        try {
            return loadJsonObjectFrominputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            ConfigAPI.INSTANCE.getLogger().log(Level.SEVERE, "File \"" + file.getName() + "\" was not found.", e);
            return null;
        }
    }

    private static Map<String, JsonObject> loadFile(File file, String namecpace) {
        Map<String, JsonObject> tmp = new HashMap<>();
        if (namecpace != null)
            namecpace += ":";

        JsonObject json = loadFileToJsonObject(file);
        if (json == null)
            return null;

        tmp.put(namecpace + file.getName().replace(".json", ""), json);
        return tmp;
    }

}
