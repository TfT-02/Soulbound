package com.me.tft_02.soulbound.listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.events.SoulbindItemEvent;
import com.me.tft_02.soulbound.util.ItemUtils;
import com.me.tft_02.soulbound.util.Permissions;

public class SelfListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onItemSoulbound(SoulbindItemEvent event) {
        Player player = event.getPlayer();
        Inventory inventory = player.getInventory();
        int maxAmount = Permissions.getSoulbindMaximum(player);

        if (maxAmount < 0) {
            return;
        }

        int count = 0;
        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && ItemUtils.isSoulbound(itemStack)) {
                count++;
            }
        }
        if (count >= maxAmount) {
            player.sendMessage(ChatColor.RED + "Cannot Soulbind any more items, maximum amount reached! " + ChatColor.GOLD + "(" + maxAmount + ")");
            event.setCancelled(true);
        }
    }
}
