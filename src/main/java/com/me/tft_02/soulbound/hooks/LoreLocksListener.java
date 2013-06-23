package com.me.tft_02.soulbound.hooks;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.github.derwisch.loreLocks.KeyCreateEvent;
import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.SoulboundConfig;
import com.me.tft_02.soulbound.util.ItemUtils;

public class LoreLocksListener implements Listener {
    Soulbound plugin;

    public LoreLocksListener(Soulbound instance) {
        plugin = instance;
    }

    /**
     * Monitor KeyCreateEvent events.
     * 
     * @param event The event to monitor
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onKeyCreate(KeyCreateEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getKey();
        SoulboundConfig config = new SoulboundConfig();

        if (itemStack != null && config.getLoreLocksBindKeys()) {
            event.setKey(ItemUtils.soulbindItem(player, itemStack));
        }
    }

}
