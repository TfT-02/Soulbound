package com.me.tft_02.soulbound.hooks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.config.Config;
import com.me.tft_02.soulbound.util.ItemUtils;

import com.github.derwisch.loreLocks.KeyCreateEvent;

public class LoreLocksListener implements Listener {

    /**
     * Monitor KeyCreateEvent events.
     *
     * @param event The event to monitor
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onKeyCreate(KeyCreateEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getKey();

        if (itemStack != null && Config.getInstance().getLoreLocksBindKeys()) {
            event.setKey(ItemUtils.soulbindItem(player, itemStack));
        }
    }

}
