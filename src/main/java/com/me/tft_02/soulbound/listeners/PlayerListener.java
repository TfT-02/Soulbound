package com.me.tft_02.soulbound.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.PlayerData;
import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.runnables.UpdateArmorTask;
import com.me.tft_02.soulbound.util.DurabilityUtils;
import com.me.tft_02.soulbound.util.ItemUtils;

public class PlayerListener implements Listener {
    Soulbound plugin;

    public PlayerListener(Soulbound instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (Soulbound.getInstance().updateAvailable && player.hasPermission("soulbound.updatecheck")) {
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
            if (player.hasPermission("soulbound.pickup.bypass")) {
                return;
            }

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

        if (Soulbound.getInstance().getConfig().getBoolean("Soulbound.Allow_Item_Drop")) {
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
        boolean keepOnDeath = Soulbound.getInstance().getConfig().getBoolean("Soulbound.Keep_On_Death");

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

    /**
     * Monitor PlayerInteract events.
     * 
     * @param event The event to watch
     */
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack inHand = player.getItemInHand();

        switch (action) {
            case RIGHT_CLICK_BLOCK:
            case RIGHT_CLICK_AIR:
            case LEFT_CLICK_AIR:
            case LEFT_CLICK_BLOCK:
                if (ItemUtils.isEquipable(inHand)) {
                    new UpdateArmorTask(player).runTaskLater(Soulbound.getInstance(), 2);
                }
                else if (ItemUtils.isBindOnUse(inHand)) {
                    ItemUtils.soulbindItem(player, inHand);
                }
            default:
                break;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onPlayerFish(PlayerFishEvent event) {
        ItemStack itemInHand = event.getPlayer().getItemInHand();

        DurabilityUtils.handleInfiniteDurability(itemInHand);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onPlayerShearEntity(PlayerShearEntityEvent event) {
        ItemStack itemInHand = event.getPlayer().getItemInHand();

        DurabilityUtils.handleInfiniteDurability(itemInHand);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        ItemStack inHand = player.getItemInHand();
        String command = event.getMessage();

        List<String> blockedCmds = Soulbound.getInstance().getConfig().getStringList("Soulbound.Blocked_Commands");
        if (ItemUtils.isSoulbound(inHand) && blockedCmds.contains(command)) {
            player.sendMessage(ChatColor.RED + "You're not allowed to use " + ChatColor.GOLD + command + ChatColor.RED + " command while holding a Soulbound item.");
            event.setCancelled(true);
        }

        List<String> bindCmds = Soulbound.getInstance().getConfig().getStringList("Soulbound.Commands_Bind_When_Used");
        if (!ItemUtils.isSoulbound(inHand) && bindCmds.contains(command)) {
            ItemUtils.soulbindItem(player, inHand);
        }
    }
}
