package com.me.tft_02.soulbound;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventoryListener implements Listener {
    Soulbound plugin;

    public InventoryListener(Soulbound instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();

        if (itemStack == null || !ItemUtils.isSoulbound(itemStack)) {
            return;
        }

        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (!plugin.getConfig().getBoolean("Soulbound.Allow_Item_Storing")) {
                if (!(event.getInventory() instanceof PlayerInventory)) {
                    event.setCancelled(true);
                }
            }

            if (!ItemUtils.isBindedPlayer(player, itemStack)) {
                event.setCancelled(true);
            }
        }
    }
}
