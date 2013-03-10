package com.me.tft_02.soulbound;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
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
            player.updateInventory();
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onPlayerDeath(PlayerDeathEvent event) {
        boolean deleteOnDeath = plugin.getConfig().getBoolean("Soulbound.Delete_On_Death");
        boolean keepOnDeath = plugin.getConfig().getBoolean("Soulbound.Keep_On_Death");

        Player player = event.getEntity();
        List<ItemStack> items = new ArrayList<ItemStack>();

        if (!keepOnDeath && !deleteOnDeath) {
            return;
        }

        for (ItemStack item : new ArrayList<ItemStack>(event.getDrops())) {
            if (ItemUtils.isSoulbound(item) && ItemUtils.isBindedPlayer(player, item)) {
                if (keepOnDeath) {
                    items.add(item);
                    event.getDrops().remove(item);
                }
                else if (deleteOnDeath) {
                    event.getDrops().remove(item);
                }
            }
        }

        PlayerData.storeItemsDeath(player, items);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        boolean keepOnDeath = plugin.getConfig().getBoolean("Soulbound.Keep_On_Death");

        if (!keepOnDeath) {
            return;
        }

        List<ItemStack> items = new ArrayList<ItemStack>();
        items = PlayerData.retrieveItemsDeath(player);
        if (items != null) {
            for (ItemStack item : items) {
                player.getInventory().addItem(item);
            }
        }

        player.updateInventory();
    }
}
