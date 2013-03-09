package com.me.tft_02.soulbound;

import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {
    Soulbound plugin;

    public PlayerListener(Soulbound instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.updateAvailable && player.hasPermission("soulbound.updatecheck")) {
            player.sendMessage(ChatColor.GOLD + "Soulbound is outdated!");
            player.sendMessage(ChatColor.AQUA + "http://dev.bukkit.org/server-mods/Soulbound/");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onItemPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Item item = event.getItem();
        ItemStack itemStack = item.getItemStack();

        if (ItemUtils.isSoulbound(itemStack) && !ItemUtils.isBindedPlayer(player, itemStack)) {
            event.setCancelled(true);
        }
        else if (ItemUtils.isBindOnPickup(itemStack)) {
            ItemUtils.soulbindItem(player, itemStack);
            player.sendMessage("This item is now bounded to you!");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Item item = event.getItemDrop();
        ItemStack itemStack = item.getItemStack();

        if (plugin.getConfig().getBoolean("Soulbound.Allow_Item_Drop")) {
            return;
        }

        if (ItemUtils.isSoulbound(itemStack) && ItemUtils.isBindedPlayer(player, itemStack)) {
            event.setCancelled(true);
        }
    }
}
