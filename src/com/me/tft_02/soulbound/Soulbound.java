package com.me.tft_02.soulbound;

import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Soulbound extends JavaPlugin {

    private PlayerListener playerListener = new PlayerListener(this);
    private InventoryListener inventoryListener = new InventoryListener(this);

    // Listeners for hooking into other plugins
    private DiabloDropsListener diabloDropsListener = new DiabloDropsListener(this);
    private EpicBossRecodedListener epicBossRecodedListener = new EpicBossRecodedListener(this);

    // Checks for hooking into other plugins
    public static boolean diabloDropsEnabled = false;
    public static boolean epicBossRecodedEnabled = false;

    // Update Check
    public boolean updateAvailable;

    /**
     * Run things on enable.
     */
    @Override
    public void onEnable() {
        setupConfiguration();

        setupDiabloDrops();
        setupEpicBossRecoded();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(playerListener, this);
        pm.registerEvents(inventoryListener, this);

        getCommand("soulbound").setExecutor(new Commands(this));
        getCommand("bind").setExecutor(new Commands(this));
        getCommand("bindonpickup").setExecutor(new Commands(this));
        getCommand("unbind").setExecutor(new Commands(this));

        checkForUpdates();

        if (getConfig().getBoolean("General.stats_tracking_enabled")) {
            try {
                Metrics metrics = new Metrics(this);
                metrics.start();
            }
            catch (IOException e) {
                System.out.println("Failed to submit stats.");
            }
        }
    }

    private void setupDiabloDrops() {
        if (getServer().getPluginManager().isPluginEnabled("DiabloDrops")) {
            diabloDropsEnabled = true;
            getLogger().info("DiabloDrops found!");
            getServer().getPluginManager().registerEvents(diabloDropsListener, this);
        }
    }

    private void setupEpicBossRecoded() {
        if (getServer().getPluginManager().isPluginEnabled("EpicBossRecoded")) {
            epicBossRecodedEnabled = true;
            getLogger().info("EpicBossRecoded found!");
            getServer().getPluginManager().registerEvents(epicBossRecodedListener, this);
        }
    }

    private void setupConfiguration() {
        final FileConfiguration config = this.getConfig();
        config.addDefault("General.stats_tracking_enabled", true);
        config.addDefault("General.update_check_enabled", true);

        config.addDefault("Soulbound.Allow_Item_Drop", true);
        config.addDefault("Soulbound.Allow_Item_Storing", true);
        config.addDefault("Soulbound.Delete_On_Death", false);
        config.addDefault("Soulbound.Keep_On_Death", false);

        if (diabloDropsEnabled) {
            config.addDefault("Dependency_Plugins.DiabloDrops.BindOnPickup", "Legendary, Rare, Unidentified");
            config.addDefault("Dependency_Plugins.DiabloDrops.BindOnUse", "Magical");
            config.addDefault("Dependency_Plugins.DiabloDrops.BindOnEquip", "Set");
        }

        if (epicBossRecodedEnabled) {
            config.addDefault("Dependency_Plugins.EpicBossRecoded.BindOnPickup", true);
            config.addDefault("Dependency_Plugins.EpicBossRecoded.BindOnEquip", false);
            config.addDefault("Dependency_Plugins.EpicBossRecoded.BindOnUse", false);
        }

        config.options().copyDefaults(true);
        saveConfig();
    }

    /**
     * Run things on disable.
     */
    @Override
    public void onDisable() {}

    private void checkForUpdates() {
        if (getConfig().getBoolean("General.update_check_enabled")) {
            try {
                updateAvailable = UpdateChecker.updateAvailable();
            }
            catch (Exception e) {
                updateAvailable = false;
            }

            if (updateAvailable) {
                this.getLogger().log(Level.INFO, ChatColor.GOLD + "Soulbound is outdated!");
                this.getLogger().log(Level.INFO, ChatColor.AQUA + "http://dev.bukkit.org/server-mods/soulbound/");
            }
        }
    }
}
