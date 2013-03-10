package com.me.tft_02.soulbound;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.ItemUtils.ItemType;

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

        ItemType itemType = ItemUtils.getItemType(itemStack);

        if (itemStack == null) {
            return;
        }

        if (entity instanceof Player) {
            Player player = (Player) entity;
            switch (itemType) {
                case BIND_ON_EQUIP:
                    switch (slotType) {
                        case ARMOR:
                            ItemUtils.soulbindItem(player, itemStack);
                            return;
                        default:
                            return;
                    }
                    /*
                    case BIND_ON_CLICK:
                    ItemUtils.soulbindItem(player, itemStack);
                    return;
                    */
                default:
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
                    if (!plugin.getConfig().getBoolean("Soulbound.Allow_Item_Storing") && !(inventoryType == InventoryType.CRAFTING)) {
                        event.setCancelled(true);
                    }

                    if (!ItemUtils.isBindedPlayer(player, itemStack)) {
                        event.setCancelled(true);
                    }
                    return;
                default:
                    return;
            }
        }
    }
}
