package com.me.tft_02.soulbound;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class EntityListener implements Listener {
    Soulbound plugin;

    public EntityListener(Soulbound instance) {
        plugin = instance;
    }

    /**
     * Check BossDeathEvent events.
     * 
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        
    }

}
