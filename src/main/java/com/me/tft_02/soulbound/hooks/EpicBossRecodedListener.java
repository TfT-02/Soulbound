package com.me.tft_02.soulbound.hooks;

//import me.ThaH3lper.com.Api.BossDeathEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.SoulboundConfig;
import com.me.tft_02.soulbound.util.ItemUtils;

public class EpicBossRecodedListener implements Listener {
    Soulbound plugin;

    public EpicBossRecodedListener(Soulbound instance) {
        plugin = instance;
    }

    /**
     * Check BossDeathEvent events.
     * 
     * @param event The event to check
     */
/*    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBossDeath(BossDeathEvent event) {
        if (event.getDrops().isEmpty()) {
            return;
        }

        for (ItemStack itemStack : event.getDrops()) {
            handleEpicBossItems(itemStack);
        }
    }
*/
    public void handleEpicBossItems(ItemStack itemStack) {
        SoulboundConfig config = new SoulboundConfig(Soulbound.getInstance());

        if (config.getEBRBindOnEquip() && ItemUtils.isEquipable(itemStack)) {
            ItemUtils.boeItem(itemStack);
        }
        else if (config.getEBRBindOnPickup()) {
            ItemUtils.bopItem(itemStack);
        }
        else if (config.getEBRBindOnUse()) {
            ItemUtils.bouItem(itemStack);
        }
    }
}
