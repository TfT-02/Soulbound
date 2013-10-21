package com.me.tft_02.soulbound;

import java.io.File;
import java.io.IOException;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.me.tft_02.soulbound.commands.Commands;
import com.me.tft_02.soulbound.config.Config;
import com.me.tft_02.soulbound.hooks.EpicBossRecodedListener;
import com.me.tft_02.soulbound.hooks.LoreLocksListener;
import com.me.tft_02.soulbound.hooks.MythicDropsListener;
import com.me.tft_02.soulbound.listeners.BlockListener;
import com.me.tft_02.soulbound.listeners.EntityListener;
import com.me.tft_02.soulbound.listeners.InventoryListener;
import com.me.tft_02.soulbound.listeners.PlayerListener;
import com.me.tft_02.soulbound.util.LogFilter;

import net.gravitydevelopment.updater.soulbound.Updater;
import org.mcstats.Metrics;

public class Soulbound extends JavaPlugin {
    /* File Paths */
    private static String mainDirectory;

    public static Soulbound p;

    // Jar Stuff
    public static File soulbound;

    private PlayerListener playerListener = new PlayerListener(this);
    private InventoryListener inventoryListener = new InventoryListener(this);
    private EntityListener entityListener = new EntityListener(this);
    private BlockListener blockListener = new BlockListener(this);

    // Listeners for hooking into other plugins
    private EpicBossRecodedListener epicBossRecodedListener = new EpicBossRecodedListener(this);
    private LoreLocksListener loreLocksListener = new LoreLocksListener(this);
    private MythicDropsListener mythicDropsListener = new MythicDropsListener(this);

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

        setupEpicBossRecoded();
        setupLoreLocks();
        setupMythicDrops();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        pm.registerEvents(inventoryListener, this);
        pm.registerEvents(entityListener, this);
        pm.registerEvents(blockListener, this);

        getCommand("soulbound").setExecutor(new Commands(this));
        getCommand("bind").setExecutor(new Commands(this));
        getCommand("bindonpickup").setExecutor(new Commands(this));
        getCommand("bindonuse").setExecutor(new Commands(this));
        getCommand("bindonequip").setExecutor(new Commands(this));
        getCommand("unbind").setExecutor(new Commands(this));

        checkForUpdates();

        if (Config.getInstance().getStatsTrackingEnabled()) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            }
            catch (IOException e) {}
        }
    }

    private void setupEpicBossRecoded() {
        if (getServer().getPluginManager().isPluginEnabled("EpicBossRecoded")) {
            epicBossRecodedEnabled = true;
            debug("EpicBossRecoded found!");
            getServer().getPluginManager().registerEvents(epicBossRecodedListener, this);
        }
    }

    private void setupLoreLocks() {
        if (getServer().getPluginManager().isPluginEnabled("LoreLocks")) {
            loreLocksEnabled = true;
            debug("LoreLocks found!");
            getServer().getPluginManager().registerEvents(loreLocksListener, this);
        }
    }

    private void setupMythicDrops() {
        if (getServer().getPluginManager().isPluginEnabled("MythicDrops")) {
            mythicDropsEnabled = true;
            debug("MythicDrops found!");
            getServer().getPluginManager().registerEvents(mythicDropsListener, this);
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
