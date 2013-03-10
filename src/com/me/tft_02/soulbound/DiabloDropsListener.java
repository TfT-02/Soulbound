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
    Soulbound plugin;

    public DiabloDropsListener(Soulbound instance) {
        plugin = instance;
    }

    /**
     * Check EntitySpawnWithItemEvent events.
     * 
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntitySpawnWithItem(EntitySpawnWithItemEvent event) {
        DropsAPI dropsAPI = new DropsAPI(DiabloDrops.getInstance());
        DiabloDropConfig diabloConfig = new DiabloDropConfig(plugin);
        for (ItemStack item : event.getItems()) {
            Tier tier = dropsAPI.getTier(item);
            String tierName = "None";
            if (tier != null) {
                tierName = tier.getName();
            }

            if (diabloConfig.getBindOnPickupTiers().contains(tierName)) {
                ItemUtils.bopItem(item);
            }
            else if (diabloConfig.getBindOnEquipTiers().contains(tierName)) {
                ItemUtils.boeItem(item);
            }
            else if (diabloConfig.getBindOnUseTiers().contains(tierName)) {
                ItemUtils.bouItem(item);
            }

        }
    }
}
