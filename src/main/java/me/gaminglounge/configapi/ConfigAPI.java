package me.gaminglounge.configapi;

import org.bukkit.plugin.java.JavaPlugin; 

public final class ConfigAPI extends JavaPlugin {
 
    public static ConfigAPI INSTANCE;
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
