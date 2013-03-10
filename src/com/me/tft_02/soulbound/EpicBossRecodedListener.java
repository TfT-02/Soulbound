package com.me.tft_02.soulbound;

import me.ThaH3lper.com.Api.BossDeathEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class EpicBossRecodedListener implements Listener {
    Soulbound plugin;

    public EpicBossRecodedListener(Soulbound instance) {
        plugin = instance;
    }

    @EventHandler(ignoreCancelled = true)
    public void onBossDeath(BossDeathEvent event) {
        for (ItemStack itemStack : event.getDrops()) {
            handleEpicBossItems(itemStack);
        }
    }

    public void handleEpicBossItems(ItemStack itemStack) {
        SoulboundConfig config = new SoulboundConfig(plugin);

        if (config.getEBRBindOnEquip() && ItemUtils.isMinecraftArmor(itemStack)) {
            ItemUtils.boeItem(itemStack);
        }
        else if (config.getEBRBindOnPickUp()) {
            ItemUtils.bopItem(itemStack);
        }
        else if (config.getEBRBindOnUse()) {
            ItemUtils.bouItem(itemStack);
        }
    }
}
