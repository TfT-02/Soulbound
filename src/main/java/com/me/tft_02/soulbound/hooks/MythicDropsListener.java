package com.me.tft_02.soulbound.hooks;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.config.Config;
import com.me.tft_02.soulbound.util.ItemUtils;

import org.nunnerycode.bukkit.mythicdropsapi.MythicDropsHook;
import org.nunnerycode.bukkit.mythicdropsapi.api.MythicDropsItemEvent;

public class MythicDropsListener implements Listener {
    Soulbound plugin;
    MythicDropsHook mythicDropsHook;

    public MythicDropsListener(Soulbound instance, MythicDropsHook mythicDropsHook) {
        plugin = instance;
        this.mythicDropsHook = mythicDropsHook;
    }

    /**
     * Check EntitySpawnWithItemEvent events.
     *
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onMythicDropsItemEvent(MythicDropsItemEvent event) {
        if (event.isCancelled()) {
            return;
        }

        ItemStack itemStack = event.getItemStack();

        ItemStack newItemStack = handleMythicDropsItems(itemStack.clone());

        if (newItemStack != null) {
            event.setItemStack(newItemStack);
        }
    }

    private ItemStack handleMythicDropsItems(ItemStack itemStack) {
        String tierName = mythicDropsHook.getWrapper().getTierFromItemStack(itemStack);

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
