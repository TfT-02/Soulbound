package com.me.tft_02.soulbound;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.modcrafting.diablodrops.DiabloDrops;
import com.modcrafting.diablodrops.drops.DropsAPI;
import com.modcrafting.diablodrops.events.EntitySpawnWithItemEvent;
import com.modcrafting.diablodrops.tier.Tier;

public class DiabloDropsListener implements Listener {

    /**
     * Monitor EntitySpawnWithItemEvent events.
     * 
     * @param event The event to monitor
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntitySpawnWithItem(EntitySpawnWithItemEvent event) {
        DropsAPI dropsAPI = new DropsAPI(DiabloDrops.getInstance());
        System.out.println("onEntitySpawnWithItem");
        for (ItemStack item : event.getItems()) {
            Tier tier = dropsAPI.getTier(item);
            String tierName = "None";
            if (tier != null) {
                tierName = tier.getName();
            }
            System.out.println("For item " + item + " with tier " + tierName);
        }
    }
}
