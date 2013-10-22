package com.me.tft_02.soulbound.hooks;

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.config.Config;
import com.me.tft_02.soulbound.util.ItemUtils;
import net.nunnerycode.bukkit.mythicdrops.MythicDrops;
import net.nunnerycode.bukkit.mythicdrops.api.tiers.Tier;
import net.nunnerycode.bukkit.mythicdrops.events.CreatureEquippedWithItemStackEvent;
import net.nunnerycode.bukkit.mythicdrops.managers.TierManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class MythicDropsV2Listener implements Listener {
    Soulbound plugin;

    public MythicDropsV2Listener(Soulbound instance) {
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

    private ItemStack handleMythicDropsItems(ItemStack itemStack) {
        TierManager tierManager = MythicDrops.instance.getTierManager();
        Tier tier = tierManager.getTierFromItemStack(itemStack);
        String tierName = "Any";
        if (tier != null) {
            tierName = tier.getTierName();
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
