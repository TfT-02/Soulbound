package com.me.tft_02.soulbound;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.modcrafting.diablodrops.DiabloDrops;
import com.modcrafting.diablodrops.drops.DropsAPI;
import com.modcrafting.diablodrops.events.EntitySpawnWithItemEvent;
import com.modcrafting.diablodrops.tier.Tier;

public class DiabloDropsListener implements Listener {
    DiabloDrops plugin;

    private DropsAPI dropsAPI = new DropsAPI(plugin);

    public void onEntitySpawnWithItem(EntitySpawnWithItemEvent event) {
        System.out.println("onEntitySpawnWithItem");
        for (ItemStack item : event.getItems()) {
            Tier tier = dropsAPI.getTier(item);
            System.out.println("For item " + item + " tier = " + tier);
        }
    }
}
