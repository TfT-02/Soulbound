package com.me.tft_02.soulbound.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.util.ItemUtils;

public final class ItemAPI {
    private ItemAPI() {}

    /**
     * Check if a Player is binded to an ItemStack.
     * </br>
     * This function is designed for API usage.
     *
     * @param player    The Player to check
     * @param itemStack The ItemStack to check
     *
     * @return true or false
     */
    public static boolean isSoulbindedPlayer(Player player, ItemStack itemStack) {
        return ItemUtils.isBindedPlayer(player, itemStack);
    }

    /**
     * Soulbind an ItemStack to a Player.
     * </br>
     * This function is designed for API usage.
     *
     * @param player    The Player to bind the item to
     * @param itemStack The ItemStack to bind
     *
     * @return the soulbound ItemStack
     */
    public static ItemStack soulbindItem(Player player, ItemStack itemStack) {
        return ItemUtils.soulbindItem(player, itemStack);
    }

    /**
     * Check if an itemstack is Soulbound.
     * </br>
     * This function is designed for API usage.
     *
     * @param itemStack The itemstack to check
     *
     * @return true or false
     */
    public static boolean isSoulbound(ItemStack itemStack) {
        return ItemUtils.isSoulbound(itemStack);
    }

    /**
     * Mark an itemstack as Bind on Pickup
     * </br>
     * This function is designed for API usage.
     *
     * @param itemStack The itemstack to mark as Bind on Pickup
     *
     * @return the marked itemstack
     */
    public static ItemStack bindOnPickupItem(ItemStack itemStack) {
        return ItemUtils.bopItem(itemStack);
    }

    /**
     * Check if an itemstack is Bind on Pickup.
     * </br>
     * This function is designed for API usage.
     *
     * @param itemStack The itemstack to check
     *
     * @return true or false
     */
    public static boolean isBindOnPickup(ItemStack itemStack) {
        return ItemUtils.isBindOnPickup(itemStack);
    }

    /**
     * Mark an itemstack as Bind on Use
     * </br>
     * This function is designed for API usage.
     *
     * @param itemStack The itemstack to mark as Bind on Use
     *
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
     *
     * @return the marked itemstack
     */
    public static ItemStack bindOnEquipItem(ItemStack itemStack) {
        return ItemUtils.boeItem(itemStack);
    }

    /**
     * Get the Soulbound type of an itemstack.
     * </br>
     * This function is designed for API usage.
     *
     * @param itemStack The itemstack to check
     *
     * @return the Bind type
     */
    public static String getItemType(ItemStack itemStack) {
        return ItemUtils.getItemType(itemStack).toString();
    }
}
