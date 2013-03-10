package com.me.tft_02.soulbound;

import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.modcrafting.diablodrops.DiabloDrops;
import com.modcrafting.diablodrops.drops.DropsAPI;
import com.modcrafting.diablodrops.events.EntitySpawnWithItemEvent;
import com.modcrafting.diablodrops.events.RuinGenerateEvent;
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
        for (ItemStack itemStack : event.getItems()) {
            handleDiabloDropsItems(itemStack);
        }
    }

    /**
     * Check RuinGenerateEvent events.
     * 
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onRuinGenerate(RuinGenerateEvent event) {
        if (event.getChest().getType() == Material.CHEST) {
            Chest chest = (Chest) event.getChest();

            for (ItemStack itemStack : chest.getInventory().getContents()) {
                if (itemStack != null) {
                    handleDiabloDropsItems(itemStack);
                }
            }
        }
    }

    public void handleDiabloDropsItems(ItemStack itemStack) {
        DropsAPI dropsAPI = new DropsAPI(DiabloDrops.getInstance());
        SoulboundConfig config = new SoulboundConfig(plugin);
        Tier tier = dropsAPI.getTier(itemStack);
        String tierName = "None";
        if (tier != null) {
            tierName = tier.getName();
        }

        if (config.getBindOnPickupTiers().contains(tierName)) {
            ItemUtils.bopItem(itemStack);
        }
        else if (config.getBindOnEquipTiers().contains(tierName)) {
            ItemUtils.boeItem(itemStack);
        }
        else if (config.getBindOnUseTiers().contains(tierName)) {
            ItemUtils.bouItem(itemStack);
        }

    }
}
