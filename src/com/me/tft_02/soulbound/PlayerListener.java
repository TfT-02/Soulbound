package com.me.tft_02.soulbound;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener {
    Soulbound plugin;

    public PlayerListener(Soulbound instance) {
        plugin = instance;
    }


    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.updateAvailable && player.hasPermission("everlastingweather.updatecheck")) {
            player.sendMessage(ChatColor.GOLD + "EverlastingWeather is outdated!");
            player.sendMessage(ChatColor.AQUA + "http://dev.bukkit.org/server-mods/EverlastingWeather/");
        }
    }
}