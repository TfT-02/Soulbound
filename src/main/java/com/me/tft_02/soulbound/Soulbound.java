package com.me.tft_02.soulbound;

import com.me.tft_02.soulbound.commands.*;
import com.me.tft_02.soulbound.config.Config;
import com.me.tft_02.soulbound.config.ItemsConfig;
import com.me.tft_02.soulbound.hooks.EpicBossRecodedListener;
import com.me.tft_02.soulbound.hooks.LoreLocksListener;
import com.me.tft_02.soulbound.hooks.MythicDropsListener;
import com.me.tft_02.soulbound.listeners.BlockListener;
import com.me.tft_02.soulbound.listeners.EntityListener;
import com.me.tft_02.soulbound.listeners.InventoryListener;
import com.me.tft_02.soulbound.listeners.PlayerListener;
import com.me.tft_02.soulbound.util.LogFilter;
import net.gravitydevelopment.updater.soulbound.Updater;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcstats.Metrics;
import org.nunnerycode.bukkit.mythicdropsapi.MythicDropsHook;

import java.io.File;
import java.io.IOException;

public class Soulbound extends JavaPlugin {
    /* File Paths */
    private static String mainDirectory;

    public static Soulbound p;

    // Jar Stuff
    public static File soulbound;

    // Checks for hooking into other plugins
    public static boolean epicBossRecodedEnabled = false;
    public static boolean loreLocksEnabled = false;
    public static boolean mythicDropsEnabled = false;

    // Update Check
    private boolean updateAvailable;

    /**
     * Run things on enable.
     */
    @Override
    public void onEnable() {
        p = this;
        getLogger().setFilter(new LogFilter(this));

        setupFilePaths();

        loadConfigFiles();

        setupEpicBossRecoded();
        setupLoreLocks();
        setupMythicDrops();

        registerEvents();

        getCommand("soulbound").setExecutor(new SoulboundCommand());
        getCommand("bind").setExecutor(new BindCommand());
        getCommand("bindonpickup").setExecutor(new BindOnPickupCommand());
        getCommand("bindonuse").setExecutor(new BindOnUseCommand());
        getCommand("bindonequip").setExecutor(new BindOnEquipCommand());
        getCommand("unbind").setExecutor(new UnbindCommand());

        checkForUpdates();

        if (Config.getInstance().getStatsTrackingEnabled()) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            }
            catch (IOException e) {
            }
        }
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerListener(this), this);
        pm.registerEvents(new InventoryListener(this), this);
        pm.registerEvents(new EntityListener(this), this);
        pm.registerEvents(new BlockListener(this), this);
    }

    private void setupEpicBossRecoded() {
        if (getServer().getPluginManager().isPluginEnabled("EpicBossRecoded")) {
            epicBossRecodedEnabled = true;
            debug("EpicBossRecoded found!");
            getServer().getPluginManager().registerEvents(new EpicBossRecodedListener(this), this);
        }
    }

    private void setupLoreLocks() {
        if (getServer().getPluginManager().isPluginEnabled("LoreLocks")) {
            loreLocksEnabled = true;
            debug("LoreLocks found!");
            getServer().getPluginManager().registerEvents(new LoreLocksListener(this), this);
        }
    }

    private void setupMythicDrops() {
        MythicDropsHook mythicDropsHook = new MythicDropsHook(this);
        if (mythicDropsHook.hasHook()) {
            mythicDropsEnabled = true;
            debug("MythicDrops found!");
            getServer().getPluginManager().registerEvents(new MythicDropsListener(this), this);
        }
    }

    /**
     * Run things on disable.
     */
    @Override
    public void onDisable() {}

    public static String getMainDirectory() {
        return mainDirectory;
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }

    public void debug(String message) {
        getLogger().info("[Debug] " + message);
    }

    /**
     * Setup the various storage file paths
     */
    private void setupFilePaths() {
        soulbound = getFile();
        mainDirectory = getDataFolder().getPath() + File.separator;
    }

    private void loadConfigFiles() {
        Config.getInstance();
        ItemsConfig.getInstance();
    }

    private void checkForUpdates() {
        if (!Config.getInstance().getUpdateCheckEnabled()) {
            return;
        }

        Updater updater = new Updater(this, 53483, soulbound, Updater.UpdateType.NO_DOWNLOAD, false);

        if (updater.getResult() != Updater.UpdateResult.UPDATE_AVAILABLE) {
            this.updateAvailable = false;
            return;
        }

        if (updater.getLatestType().equals("beta") && !Config.getInstance().getPreferBeta()) {
            this.updateAvailable = false;
            return;
        }

        this.updateAvailable = true;
        getLogger().info("Soulbound is outdated!");
        getLogger().info("http://dev.bukkit.org/server-mods/soulbound/");
    }
}
