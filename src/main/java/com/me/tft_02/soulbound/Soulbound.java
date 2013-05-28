package com.me.tft_02.soulbound;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.me.tft_02.soulbound.hooks.DiabloDropsListener;
import com.me.tft_02.soulbound.hooks.EpicBossRecodedListener;
import com.me.tft_02.soulbound.hooks.LoreLocksListener;
import com.me.tft_02.soulbound.hooks.MythicDropsListener;
import com.me.tft_02.soulbound.listeners.BlockListener;
import com.me.tft_02.soulbound.listeners.EntityListener;
import com.me.tft_02.soulbound.listeners.InventoryListener;
import com.me.tft_02.soulbound.listeners.PlayerListener;
import com.me.tft_02.soulbound.util.Metrics;
import com.me.tft_02.soulbound.util.UpdateChecker;

public class Soulbound extends JavaPlugin {
    public static Soulbound instance;

    private PlayerListener playerListener = new PlayerListener(this);
    private InventoryListener inventoryListener = new InventoryListener(this);
    private EntityListener entityListener = new EntityListener(this);
    private BlockListener blockListener = new BlockListener(this);

    // Listeners for hooking into other plugins
    private DiabloDropsListener diabloDropsListener = new DiabloDropsListener(this);
    private EpicBossRecodedListener epicBossRecodedListener = new EpicBossRecodedListener(this);
    private LoreLocksListener loreLocksListener = new LoreLocksListener(this);
    private MythicDropsListener mythicDropsListener = new MythicDropsListener(this);

    // Checks for hooking into other plugins
    public static boolean diabloDropsEnabled = false;
    public static boolean epicBossRecodedEnabled = false;
    public static boolean loreLocksEnabled = false;
    public static boolean mythicDropsEnabled = false;

    // Update Check
    public boolean updateAvailable;

    public static Soulbound getInstance() {
        return instance;
    }

    /**
     * Run things on enable.
     */
    @Override
    public void onEnable() {
        instance = this;

        setupDiabloDrops();
        setupEpicBossRecoded();
        setupLoreLocks();
        setupMythicDrops();

        setupConfiguration();

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

    private void setupLoreLocks() {
        if (getServer().getPluginManager().isPluginEnabled("LoreLocks")) {
            loreLocksEnabled = true;
            getLogger().info("LoreLocks found!");
            getServer().getPluginManager().registerEvents(loreLocksListener, this);
        }
    }

    private void setupMythicDrops() {
        if (getServer().getPluginManager().isPluginEnabled("MythicDrops")) {
            mythicDropsEnabled = true;
            getLogger().info("MythicDrops found!");
            getServer().getPluginManager().registerEvents(mythicDropsListener, this);
        }
    }

    private void setupConfiguration() {
        final FileConfiguration config = this.getConfig();
        config.addDefault("General.stats_tracking_enabled", true);
        config.addDefault("General.update_check_enabled", true);

        config.addDefault("Soulbound.Feedback_Messages_Enabled", true);
        config.addDefault("Soulbound.Allow_Item_Drop", true);
        config.addDefault("Soulbound.Allow_Item_Storing", true);
        config.addDefault("Soulbound.Delete_On_Death", false);
        config.addDefault("Soulbound.Keep_On_Death", false);
        config.addDefault("Soulbound.Infinite_Durability", false);

        String[] defaultBlockedcmds = { "/blockedcommand" };
        config.addDefault("Soulbound.Blocked_Commands", Arrays.asList(defaultBlockedcmds));

        String[] defaultSoulbindcmds = { "/enchant" };
        config.addDefault("Soulbound.Commands_Bind_When_Used", Arrays.asList(defaultSoulbindcmds));

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

        if (loreLocksEnabled) {
            config.addDefault("Dependency_Plugins.LoreLocks.Bind_Keys", true);
        }

        if (mythicDropsEnabled) {
            config.addDefault("Dependency_Plugins.MythicDrops.BindOnPickup", "common, uncommon, rare");
            config.addDefault("Dependency_Plugins.MythicDrops.BindOnUse", "terric, netheric");
            config.addDefault("Dependency_Plugins.MythicDrops.BindOnEquip", "endric");
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
