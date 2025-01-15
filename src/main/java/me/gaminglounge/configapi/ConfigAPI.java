package me.gaminglounge.configapi;

import org.bukkit.plugin.java.JavaPlugin; 

public final class ConfigAPI extends JavaPlugin {
 
    public static ConfigAPI INSTANCE;
 
    @Override
    public void onLoad() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
    }

    @Override
    public void onDisable() {
    }
}
