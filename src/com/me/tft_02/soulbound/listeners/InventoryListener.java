package com.me.tft_02.soulbound.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.util.ItemUtils;
import com.me.tft_02.soulbound.util.ItemUtils.ItemType;

public class InventoryListener implements Listener {
    Soulbound plugin;

    public InventoryListener(Soulbound instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() < 0) {
            return;
        }
        HumanEntity entity = event.getWhoClicked();
        ItemStack cursor = event.getCursor();
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
                    handleBindOnEquip(player, itemStack);
                    handleBindOnEquip(player, cursor);
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
                    break;
            }
        }
    }

    public void handleBindOnEquip(Player player, ItemStack itemStack) {
        ItemType itemType = ItemUtils.getItemType(itemStack);
        switch (itemType) {
            case BIND_ON_EQUIP:
                ItemUtils.soulbindItem(player, itemStack);
                break;
            default:
                break;
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
                    if (!Soulbound.getInstance().getConfig().getBoolean("Soulbound.Allow_Item_Storing") && !(inventoryType == InventoryType.CRAFTING)) {
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
}
