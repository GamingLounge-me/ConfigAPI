package me.gaminglounge.configapi;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Language {

    private static String getPrefix(Plugin plugin, String lang) {
        String pluginName = plugin.getName();

        String playerLang = getKey(lang, "prefix");
        if (playerLang != null)
            return playerLang;

        String defaultLang = getKey(pluginName + ":" + "en_US", "prefix");
        if (defaultLang != null)
            return defaultLang;

        ConfigAPI.INSTANCE.getLogger().log(Level.SEVERE,
                "Missing language key \"prefix\" in " + lang + " and the default language, please contact an admin.");
        return null;
    }

    private static String getLangCode(Plugin plugin, Player p) {
        String locale = plugin.getName() + ":" + p.locale().toString();
        if (!ConfigAPI.CONFIG.lang.containsKey(locale)) {
            locale = plugin.getName() + ":" + "en_US";
        }
        return locale;
    }

    private static String getValueI(Plugin plugin, String lang, String key) {
        String playerLang = getKey(lang, key);
        if (playerLang != null)
            return playerLang;

        lang = plugin.getName() + ":" + "en_US";
        if (ConfigAPI.CONFIG.lang.containsKey(lang)) {
            if (ConfigAPI.CONFIG.lang.get(lang).has(key)) {
                return ConfigAPI.CONFIG.lang.get(lang).get(key).getAsString();
            } else {
                ConfigAPI.INSTANCE.getLogger().log(Level.SEVERE,
                        "The default language \"" + lang + "\" doesn't have the tranlation for the key \"" + key
                                + "\".");
            }
        }
        return "Missing language key \"" + key + "\" for \"" + lang + "\", please contact an admin.";
    }

    private static String getKey(String lang, String key) {
        if (ConfigAPI.CONFIG.lang.containsKey(lang)) {
            if (ConfigAPI.CONFIG.lang.get(lang).has(key)) {
                return ConfigAPI.CONFIG.lang.get(lang).get(key).getAsString();
            } else {
                ConfigAPI.INSTANCE.getLogger().log(Level.WARNING,
                        "The language \"" + lang + "\" doesn't have the tranlation for the key \"" + key + "\".");
                return null;
            }
        } else
            return null;
    }

    public static String getValue(Plugin plugin, String lang, String key) {
        lang = plugin.getName() + ":" + lang;
        return getValueI(plugin, lang, key);
    }

    public static String getValue(Plugin plugin, String lang, String key, boolean prefix) {
        lang = plugin.getName() + ":" + lang;
        if (prefix) {
            return getValueI(plugin, lang, key);
        } else {
            return getPrefix(plugin, lang) + " " + getValueI(plugin, lang, key);
        }
    }

    public static String getValue(Plugin plugin, Player p, String key) {
        return getValueI(plugin, getLangCode(plugin, p), key);
    }

    public static String getValue(Plugin plugin, Player p, String key, boolean prefix) {
        String lang = getLangCode(plugin, p);
        if (prefix) {
            return getPrefix(plugin, lang) + " " + getValueI(plugin, lang, key);
        } else {
            return getValueI(plugin, lang, key);
        }
    }

}
