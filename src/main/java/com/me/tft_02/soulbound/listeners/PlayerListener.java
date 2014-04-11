package com.me.tft_02.soulbound.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
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

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.config.Config;
import com.me.tft_02.soulbound.config.ItemsConfig;
import com.me.tft_02.soulbound.datatypes.ActionType;
import com.me.tft_02.soulbound.runnables.SoulbindInventoryTask;
import com.me.tft_02.soulbound.runnables.UpdateArmorTask;
import com.me.tft_02.soulbound.runnables.UpdateInventoryTask;
import com.me.tft_02.soulbound.util.DurabilityUtils;
import com.me.tft_02.soulbound.util.ItemUtils;
import com.me.tft_02.soulbound.util.Permissions;
import com.me.tft_02.soulbound.util.PlayerData;

public class PlayerListener implements Listener {
    Soulbound plugin;

    public PlayerListener(Soulbound instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    private void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (Soulbound.p.isUpdateAvailable() && Permissions.updateCheck(player)) {
            player.sendMessage(ChatColor.GOLD + "Soulbound is outdated!");
            player.sendMessage(ChatColor.AQUA + "http://dev.bukkit.org/server-mods/Soulbound/");
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onItemPickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();
        Item item = event.getItem();
        ItemStack itemStack = item.getItemStack();

        if (ItemUtils.isSoulbound(itemStack) && !ItemUtils.isBindedPlayer(player, itemStack)) {
            if (Permissions.pickupBypass(player)) {
                return;
            }

            event.setCancelled(true);
            return;
        }

        if (ItemUtils.isBindOnPickup(itemStack)) {
            ItemUtils.soulbindItem(player, itemStack);
            return;
        }

        if (ItemsConfig.getInstance().isActionItem(itemStack, ActionType.PICKUP_ITEM)) {
            ItemUtils.soulbindItem(player, itemStack);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onItemDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        Item item = event.getItemDrop();
        ItemStack itemStack = item.getItemStack();

        if (Config.getInstance().getPreventItemDrop()) {
            if (ItemUtils.isSoulbound(itemStack) && ItemUtils.isBindedPlayer(player, itemStack)) {
                item.setPickupDelay(2 * 20);
                event.setCancelled(true);
                new UpdateInventoryTask(player).runTask(Soulbound.p);
            }
            return;
        }

        if (Config.getInstance().getDeleteOnDrop()) {
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0F, 1.0F);
            event.getItemDrop().remove();
            return;
        }

        if (ItemsConfig.getInstance().isActionItem(itemStack, ActionType.DROP_ITEM)) {
            ItemUtils.soulbindItem(player, itemStack);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        boolean deleteOnDeath = Permissions.deleteOnDeath(player);
        boolean keepOnDeath = Permissions.keepOnDeath(player);

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

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    private void onPlayerRespawn(PlayerRespawnEvent event) {
        new SoulbindInventoryTask(event.getPlayer(), ActionType.RESPAWN).runTask(Soulbound.p);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onPlayerRespawnHighest(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        boolean keepOnDeath = Permissions.keepOnDeath(player);

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

        new UpdateInventoryTask(player).runTask(Soulbound.p);
    }

    /**
     * Watch PlayerInteract events.
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
                    new UpdateArmorTask(player).runTaskLater(Soulbound.p, 2);
                }
                else if (ItemUtils.isBindOnUse(inHand)) {
                    ItemUtils.soulbindItem(player, inHand);
                }
            default:
                break;
        }
    }

    /**
     * Monitor PlayerFishEvent events.
     *
     * @param event The event to monitor
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onPlayerFish(PlayerFishEvent event) {
        ItemStack itemInHand = event.getPlayer().getItemInHand();

        DurabilityUtils.handleInfiniteDurability(itemInHand);
    }

    /**
     * Monitor PlayerShearEntityEvent events.
     *
     * @param event The event to monitor
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onPlayerShearEntity(PlayerShearEntityEvent event) {
        ItemStack itemInHand = event.getPlayer().getItemInHand();

        DurabilityUtils.handleInfiniteDurability(itemInHand);
    }

    /**
     * Watch PlayerCommandPreprocessEvent events.
     *
     * @param event The event to watch
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();
        String command = event.getMessage();

        System.out.println("command " + command);

        if (ItemUtils.isSoulbound(itemStack) && Config.getInstance().getBlockedCommands().contains(command)) {
            player.sendMessage(ChatColor.RED + "You're not allowed to use " + ChatColor.GOLD + command + ChatColor.RED + " command while holding a Soulbound item.");
            event.setCancelled(true);
        }
        else if (command.contains("kit")) {
            new SoulbindInventoryTask(player, ActionType.KIT).runTask(Soulbound.p);
        }
    }

    /**
     * Monitor PlayerCommandPreprocessEvent events.
     *
     * @param event The event to monitor
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlayerCommandMonitor(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        ItemStack inHand = player.getItemInHand();
        String command = event.getMessage();

        if (!ItemUtils.isSoulbound(inHand) && Config.getInstance().getBindCommands().contains(command)) {
            ItemUtils.soulbindItem(player, inHand);
        }
    }
}
