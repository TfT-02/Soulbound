package com.me.tft_02.soulbound.hooks;

//import me.ThaH3lper.com.Api.BossDeathEvent;

import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.config.Config;
import com.me.tft_02.soulbound.util.ItemUtils;

public class EpicBossRecodedListener implements Listener {

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
        if (Config.getInstance().getEBRBindOnEquip() && ItemUtils.isEquipable(itemStack)) {
            ItemUtils.boeItem(itemStack);
        }
        else if (Config.getInstance().getEBRBindOnPickup()) {
            ItemUtils.bopItem(itemStack);
        }
        else if (Config.getInstance().getEBRBindOnUse()) {
            ItemUtils.bouItem(itemStack);
        }
    }
}
