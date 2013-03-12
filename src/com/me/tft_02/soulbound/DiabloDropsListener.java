package com.me.tft_02.soulbound;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
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
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRuinGenerate(RuinGenerateEvent event) {
        Block block = event.getChest();
        BlockState blockState =  block.getState();

        if (block.getType() == Material.CHEST) {
            Chest chest = (Chest)blockState;
            Inventory inventory = chest.getBlockInventory();

            for (ItemStack itemStack : inventory.getContents()) {
                if (itemStack != null) {
                    System.out.println("Editing chest at X:" + block.getLocation().getX() + " Z: " + block.getLocation().getZ());
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

        if (config.getBindOnEquipTiers().contains(tierName) && ItemUtils.isMinecraftArmor(itemStack)) {
            ItemUtils.boeItem(itemStack);
        }
        else if (config.getBindOnPickupTiers().contains(tierName)) {
            ItemUtils.bopItem(itemStack);
        }
        else if (config.getBindOnUseTiers().contains(tierName)) {
            ItemUtils.bouItem(itemStack);
        }

    }
}
