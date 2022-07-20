package dev.coffeese.hopperfilter;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

public class Plugin extends JavaPlugin {

    private Logger logger;

    @Override
    public void onLoad() {
        logger = getLogger();
        logger.info("onLoad");
    }

    @Override
    public void onEnable() {
        logger.info("onEnable");

        this.getServer().getPluginManager().registerEvents(new HopperListener(logger), this);
    }

    @Override
    public void onDisable() {
        logger.info("onDisable");
    }
}
