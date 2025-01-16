package me.gaminglounge.configapi;

import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Language {

    private static String getPrefix(Plugin plugin, String lang) {
        lang = plugin.getName() + ":" + lang;
        if (ConfigAPI.CONFIG.lang.get(lang).has("prefix")) {
            return ConfigAPI.CONFIG.lang.get(lang).get("prefix").getAsString(); 
        } else if (ConfigAPI.CONFIG.lang.get("en_us").has("prefix")) {
            return ConfigAPI.CONFIG.lang.get("en_us").get("prefix").getAsString(); 
        } else {
            throw new IllegalArgumentException("The translation key \"prefix\" is not set.");
        }
    }
    
    private static String getLangCode(Player p) {
        String locale = p.locale().toString();
        if (!ConfigAPI.CONFIG.lang.containsKey(locale)) {
            locale = "en_us";
        }
        ConfigAPI.INSTANCE.getLogger().log(Level.WARNING, locale);
        return locale;
    }

    private static String getValueI(Plugin plugin, String lang, String key) {
        lang = plugin.getName() + ":" + lang;
        if (ConfigAPI.CONFIG.lang.containsKey(lang)) {
            if (ConfigAPI.CONFIG.lang.get(lang).has(key)) {
                return ConfigAPI.CONFIG.lang.get(lang).get(key).getAsString(); 
            } else {
                ConfigAPI.INSTANCE.getLogger().log(Level.WARNING, "The language \"{0}\" doesn''t have the tranlation for the key \"{1}\".", new Object[]{lang, key});
            }
        }
        lang = "en_us";
        if (ConfigAPI.CONFIG.lang.containsKey(lang)) {
            if (ConfigAPI.CONFIG.lang.get(lang).has(key)) {
                return ConfigAPI.CONFIG.lang.get(lang).get(key).getAsString(); 
            } else {
                ConfigAPI.INSTANCE.getLogger().log(Level.SEVERE, "The default language \"{0}\" doesn''t have the tranlation for the key \"{1}\".", new Object[]{lang, key});
            }
        }
        return "Missing language key \"" + key + "\", please contact an admin.";
    }

    public static String getValue(Plugin plugin, String lang, String key) {
        return getValueI(plugin, lang, key);
    }

    public static String getValue(Plugin plugin, String lang, String key, boolean prefix) {
        if (prefix) {
            return getValueI(plugin, lang, key);
        } else {
            return getPrefix(plugin, lang) + " " + getValueI(plugin, lang, key);
        }
    }

    public static String getValue(Plugin plugin, Player p, String key) {
        return getValueI(plugin, getLangCode(p), key);
    }

    public static String getValue(Plugin plugin, Player p, String key, boolean prefix) {
        String lang = getLangCode(p);
        if (prefix) {
            return getPrefix(plugin, lang) + " " + getValueI(plugin, lang, key);
        } else {
            return getValueI(plugin, lang, key);
        }
    }

}
