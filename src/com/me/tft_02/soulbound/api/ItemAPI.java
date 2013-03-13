package com.me.tft_02.soulbound.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.util.ItemUtils;

public final class ItemAPI {
    private ItemAPI() {}

    /**
     * Soulbind an itemstack to a player.
     * </br>
     * This function is designed for API usage.
     *
     * @param player The player to bind the item to
     * @param itemStack The itemstack to bind
     * @return the soulbound itemstack
     */
    public static ItemStack soulbindItem(Player player, ItemStack itemStack) {
        return ItemUtils.soulbindItem(player, itemStack);
    }

    /**
     * Mark an itemstack as Bind on Pickup
     * </br>
     * This function is designed for API usage.
     *
     * @param itemStack The itemstack to mark as Bind on Pickup
     * @return the marked itemstack
     */
    public static ItemStack bindOnPickupItem(ItemStack itemStack) {
        return ItemUtils.bopItem(itemStack);
    }

    /**
     * Mark an itemstack as Bind on Use
     * </br>
     * This function is designed for API usage.
     *
     * @param itemStack The itemstack to mark as Bind on Use
     * @return the marked itemstack
     */
    public static ItemStack bindOnUseItem(ItemStack itemStack) {
        return ItemUtils.bouItem(itemStack);
    }

    /**
     * Mark an itemstack as Bind on Equip
     * </br>
     * This function is designed for API usage.
     *
     * @param itemStack The itemstack to mark as Bind on Equip
     * @return the marked itemstack
     */
    public static ItemStack bindOnEquipItem(ItemStack itemStack) {
        return ItemUtils.boeItem(itemStack);
    }
}
