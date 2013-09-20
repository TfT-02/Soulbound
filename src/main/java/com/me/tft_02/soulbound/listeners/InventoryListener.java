package com.me.tft_02.soulbound.listeners;

import java.util.HashSet;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.config.Config;
import com.me.tft_02.soulbound.datatypes.ActionType;
import com.me.tft_02.soulbound.runnables.UpdateArmorTask;
import com.me.tft_02.soulbound.util.ItemUtils;
import com.me.tft_02.soulbound.util.ItemUtils.ItemType;

public class InventoryListener implements Listener {
    Soulbound plugin;

    public InventoryListener(Soulbound instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    void onInventoryClick(InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();

        SlotType slotType = event.getSlotType();
        InventoryType inventoryType = event.getInventory().getType();

        if (inventoryType == null) {
            return;
        }

        if (itemStack == null) {
            return;
        }

        if (entity instanceof Player) {
            Player player = (Player) entity;
            switch (slotType) {
                case ARMOR:
                    new UpdateArmorTask(player).runTaskLater(Soulbound.p, 2);
                    return;
                case CONTAINER:
                    ItemType itemType = ItemUtils.getItemType(itemStack);
                    switch (itemType) {
                        case BIND_ON_PICKUP:
                            ItemUtils.soulbindItem(player, itemStack);
                            return;
                        default:
                            return;
                    }
                default:
                    if (ItemUtils.isEquipable(itemStack) && event.isShiftClick()) {
                        new UpdateArmorTask(player).runTaskLater(Soulbound.p, 2);
                        return;
                    }
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        InventoryType inventoryType = event.getInventory().getType();

        if (inventoryType == null) {
            return;
        }

        ItemType itemType = ItemUtils.getItemType(itemStack);

        if (itemStack == null) {
            return;
        }

        if (entity instanceof Player) {
            Player player = (Player) entity;
            switch (itemType) {
                case NORMAL:
                    return;
                case SOULBOUND:
                    if (!Config.getInstance().getAllowItemStoring() && !(inventoryType == InventoryType.CRAFTING)) {
                        event.setCancelled(true);
                    }

                    if (!ItemUtils.isBindedPlayer(player, itemStack)) {
                        if (player.hasPermission("soulbound.pickup.bypass")) {
                            return;
                        }

                        event.setCancelled(true);
                    }
                    return;
                default:
                    return;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryMoveEvent(InventoryMoveItemEvent event) {
        ItemStack itemStack = event.getItem();

        ItemType itemType = ItemUtils.getItemType(itemStack);

        if (itemStack == null) {
            return;
        }

        switch (itemType) {
            case NORMAL:
                return;
            case SOULBOUND:
                event.setCancelled(true);
                return;
            default:
                return;
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryPickupItemEvent(InventoryPickupItemEvent event) {
        ItemStack itemStack = event.getItem().getItemStack();

        ItemType itemType = ItemUtils.getItemType(itemStack);

        if (itemStack == null) {
            return;
        }

        switch (itemType) {
            case NORMAL:
                return;
            case SOULBOUND:
                event.setCancelled(true);
                return;
            default:
                return;
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        HumanEntity humanEntity = event.getPlayer();
        Inventory inventory = event.getInventory();
        
        if (humanEntity instanceof Player) {
            Player player = (Player) humanEntity;
            
            HashSet<Material> items = Config.getInstance().getAlwaysSoulboundItems(ActionType.OPEN_CHEST);
            if (items != null) {
                for (ItemStack itemStack : inventory.getContents()) {
                    if (itemStack != null && items.contains(itemStack.getType())) {
                        ItemUtils.soulbindItem(player, itemStack);
                    }
                }
            }
        }
    }
}
