package com.me.tft_02.soulbound.hooks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.conventnunnery.plugins.mythicdrops.MythicDrops;
import com.conventnunnery.plugins.mythicdrops.events.CreatureEquippedWithItemStackEvent;
import com.conventnunnery.plugins.mythicdrops.events.ItemIdentifiedEvent;
import com.conventnunnery.plugins.mythicdrops.managers.TierManager;
import com.conventnunnery.plugins.mythicdrops.objects.Tier;
import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.config.Config;
import com.me.tft_02.soulbound.util.ItemUtils;

public class MythicDropsListener implements Listener {
    Soulbound plugin;

    public MythicDropsListener(Soulbound instance) {
        plugin = instance;
    }

    /**
     * Check EntitySpawnWithItemEvent events.
     * 
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMythicDropsEntitySpawn(CreatureEquippedWithItemStackEvent event) {
        ItemStack itemStack = event.getItemStack();

        ItemStack newItemStack = handleMythicDropsItems(itemStack.clone());

        if (newItemStack != null) {
            event.setItemStack(newItemStack);
        }
    }

    /**
     * Check ItemIdentifiedEvent events.
     * 
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemIdentifiedEvent(ItemIdentifiedEvent event) {
        ItemStack itemStack = event.getItemStack();

        handleMythicDropsItems(itemStack);
    }

    private ItemStack handleMythicDropsItems(ItemStack itemStack) {
        TierManager tierManager = MythicDrops.getInstance().getTierManager();
        Tier tier = tierManager.getTierFromItemStack(itemStack);
        String tierName = "Any";
        if (tier != null) {
            tierName = tier.getName();
        }

        if (Config.getInstance().getMythicDropsBindOnEquipTiers().contains(tierName) && ItemUtils.isEquipable(itemStack)) {
            return ItemUtils.boeItem(itemStack);
        }
        else if (Config.getInstance().getMythicDropsBindOnPickupTiers().contains(tierName.toLowerCase())) {
            return ItemUtils.bopItem(itemStack);
        }
        else if (Config.getInstance().getMythicDropsBindOnUseTiers().contains(tierName)) {
            return ItemUtils.bouItem(itemStack);
        }
        return null;
    }
}
