package com.me.tft_02.soulbound.hooks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.conventnunnery.plugins.MythicDrops.MythicDrops;
import com.conventnunnery.plugins.MythicDrops.api.TierAPI;
import com.conventnunnery.plugins.MythicDrops.events.CreatureEquippedWithItemStackEvent;
import com.conventnunnery.plugins.MythicDrops.objects.Tier;
import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.SoulboundConfig;
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
        handleMythicDropsItems(itemStack);
    }

    private void handleMythicDropsItems(ItemStack itemStack) {
        SoulboundConfig config = new SoulboundConfig(Soulbound.getInstance());
        TierAPI tierAPI = Soulbound.getInstance().getMythicDrops().getTierAPI();
        Tier tier = tierAPI.getTierFromItemStack(itemStack);
        String tierName = "Any";
        if (tier != null) {
            tierName = tier.getName();
        }

        if (config.getMythicDropsBindOnEquipTiers().contains(tierName) && ItemUtils.isEquipable(itemStack)) {
            ItemUtils.boeItem(itemStack);
        }
        else if (config.getMythicDropsBindOnPickupTiers().contains(tierName)) {
            ItemUtils.bopItem(itemStack);
        }
        else if (config.getMythicDropsBindOnUseTiers().contains(tierName)) {
            ItemUtils.bouItem(itemStack);
        }
    }
}