package com.me.tft_02.soulbound.hooks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.config.Config;
import com.me.tft_02.soulbound.util.ItemUtils;

import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.events.RandomItemGenerationEvent;

public class MythicDropsV3Listener implements Listener {
    Soulbound plugin;

    public MythicDropsV3Listener(Soulbound instance) {
        plugin = instance;
    }

    /**
     * Check RandomItemGenerationEvent events.
     *
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMythicDropsEntitySpawn(RandomItemGenerationEvent event) {
        ItemStack itemStack = event.getItemStack();

        ItemStack newItemStack = handleMythicDropsItems(itemStack.clone());

        if (newItemStack != null) {
            event.setItemStack(newItemStack);
        }
    }

    private ItemStack handleMythicDropsItems(ItemStack itemStack) {
        Tier tier = TierUtil.getTierFromItemStack(itemStack);
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
