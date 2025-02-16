package me.gaminglounge.configapi;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main file of the plguin
 */
public final class ConfigAPI extends JavaPlugin {

    /**
     * Instance of the plugin used in other methods to acces logger an other things.
     */
    public static ConfigAPI INSTANCE;
    /**
     * static field to acces LoadConfig.
     */
    public static LoadConfig CONFIG;

    @Override
    public void onLoad() {
        INSTANCE = this;
        CONFIG = new LoadConfig();
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}
