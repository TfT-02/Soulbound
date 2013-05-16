package com.me.tft_02.soulbound.util;

import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.Soulbound;

public class DurabilityUtils {

    public static void handleInfiniteDurability(ItemStack itemStack) {
        if (Soulbound.getInstance().getConfig().getBoolean("Soulbound.Infinite_Durability")) {
            itemStack.setDurability((short) 0);
            return;
        }
    }
}
