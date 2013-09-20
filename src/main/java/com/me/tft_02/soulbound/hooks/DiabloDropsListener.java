package com.me.tft_02.soulbound.hooks;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.config.Config;
import com.me.tft_02.soulbound.util.ItemUtils;
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
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRuinGenerate(RuinGenerateEvent event) {
        final Block block = event.getChest();
        Bukkit.getScheduler().scheduleSyncDelayedTask(Soulbound.p, new Runnable() {
            @Override
            public void run() {
                if (block.getType() == Material.CHEST) {
                    handleDiabloDropsChests(block);
                }
            }
        }, 1);
    }

    public void handleDiabloDropsItems(ItemStack itemStack) {
        DropsAPI dropsAPI = new DropsAPI(DiabloDrops.getInstance());
        Tier tier = dropsAPI.getTier(itemStack);
        String tierName = "Any";
        if (tier != null) {
            tierName = tier.getName();
        }

        if (Config.getInstance().getDiabloDropsBindOnEquipTiers().contains(tierName) && ItemUtils.isEquipable(itemStack)) {
            ItemUtils.boeItem(itemStack);
        }
        else if (Config.getInstance().getDiabloDropsBindOnPickupTiers().contains(tierName)) {
            ItemUtils.bopItem(itemStack);
        }
        else if (Config.getInstance().getDiabloDropsBindOnUseTiers().contains(tierName)) {
            ItemUtils.bouItem(itemStack);
        }
    }

    public void handleDiabloDropsChests(Block block) {
        BlockState blockState = block.getState();
        Chest chest = (Chest) blockState;
        Inventory inventory = chest.getBlockInventory();

        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null) {
                handleDiabloDropsItems(itemStack);
            }
        }
    }
}
