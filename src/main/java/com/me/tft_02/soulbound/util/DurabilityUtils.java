package com.me.tft_02.soulbound.util;

import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.config.Config;

public class DurabilityUtils {

    public static void handleInfiniteDurability(ItemStack itemStack) {
        if (Config.getInstance().getInfiniteDurability() && ItemUtils.isSoulbound(itemStack)) {
            itemStack.setDurability((short) 0);
            return;
        }
    }
}
