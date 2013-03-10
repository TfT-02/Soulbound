package com.me.tft_02.soulbound;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {
    public enum ItemType {
        NORMAL,
        SOULBOUND,
        BIND_ON_PICKUP,
        BIND_ON_USE,
        BIND_ON_EQUIP;
    };

    Soulbound plugin;

    public ItemUtils(Soulbound instance) {
        plugin = instance;
    }

    public static ItemStack soulbindItem(Player player, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> itemLore = new ArrayList<String>();
        if (itemMeta.hasLore()) {
            List<String> oldLore =itemMeta.getLore();

            oldLore.remove(ChatColor.DARK_RED + "Bind on Pickup");
            oldLore.remove(ChatColor.DARK_RED + "Bind on Equip");
            oldLore.remove(ChatColor.DARK_RED + "Bind on Use");
            itemLore.addAll(oldLore);
        }
        itemLore.add(ChatColor.GOLD + "Soulbound");
        itemLore.add(player.getName());
        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack bopItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<String> itemLore = new ArrayList<String>();
        if (itemMeta.hasLore()) {
            itemLore.addAll(itemMeta.getLore());
        }
        itemLore.add(ChatColor.DARK_RED + "Bind on Pickup");
        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack boeItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<String> itemLore = new ArrayList<String>();
        if (itemMeta.hasLore()) {
            itemLore.addAll(itemMeta.getLore());
        }
        itemLore.add(ChatColor.DARK_RED + "Bind on Equip");
        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack bouItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        List<String> itemLore = new ArrayList<String>();
        if (itemMeta.hasLore()) {
            itemLore.addAll(itemMeta.getLore());
        }
        itemLore.add(ChatColor.DARK_RED + "Bind on Use");
        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack unbindItem(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasLore() && isSoulbound(itemStack)) {
            List<String> oldLore = itemMeta.getLore();
            int loreSize = oldLore.size();

            if (loreSize > 2) {
                List<String> itemLore = new ArrayList<String>();
                itemLore.addAll(oldLore);
                itemLore.remove(oldLore.get(loreSize - 1));
                itemLore.remove(oldLore.get(loreSize - 2));
                itemMeta.setLore(itemLore);
            }
            else {
                itemMeta.setLore(null);
            }
        }
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static boolean isSoulbound(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasLore()) {
            List<String> itemLore = itemMeta.getLore();
            if (itemLore.contains(ChatColor.GOLD + "Soulbound")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBindOnPickup(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasLore()) {
            List<String> itemLore = itemMeta.getLore();
            if (itemLore.contains(ChatColor.DARK_RED + "Bind on Pickup")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBindOnUse(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasLore()) {
            List<String> itemLore = itemMeta.getLore();
            if (itemLore.contains(ChatColor.DARK_RED + "Bind on Use")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBindOnEquip(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return false;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasLore()) {
            List<String> itemLore = itemMeta.getLore();
            if (itemLore.contains(ChatColor.DARK_RED + "Bind on Equip")) {
                return true;
            }
        }
        return false;
    }

    public static boolean isBindedPlayer(Player player, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> itemLore = itemMeta.getLore();
        if (itemLore.contains(player.getName())) {
            return true;
        }
        return false;
    }

    public static boolean isNormalItem(ItemStack itemStack) {
        if (!itemStack.hasItemMeta()) {
            return true;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!itemMeta.hasLore()) {
            return true;
        }

        ItemType itemType = ItemUtils.getItemType(itemStack);
        switch (itemType) {
            case NORMAL:
                return true;
            default:
                return false;
        }
    }

    public static ItemType getItemType(ItemStack itemStack) {
        if (itemStack == null) {
            return ItemType.NORMAL;
        }
        else if (isSoulbound(itemStack)) {
            return ItemType.SOULBOUND;
        }
        else if (isBindOnPickup(itemStack)) {
            return ItemType.BIND_ON_PICKUP;
        }
        else if (isBindOnUse(itemStack)) {
            return ItemType.BIND_ON_USE;
        }
        else if (isBindOnEquip(itemStack)) {
            return ItemType.BIND_ON_EQUIP;
        }
        else {
            return ItemType.NORMAL;
        }
    }

    /**
     * Checks to see if an item is a wearable armor piece.
     *
     * @param is Item to check
     * @return true if the item is armor, false otherwise
     */
    public static boolean isMinecraftArmor(ItemStack is) {
        return isLeatherArmor(is) || isGoldArmor(is) || isIronArmor(is) || isDiamondArmor(is);
    }

    /**
     * Checks to see if an item is a leather armor piece.
     *
     * @param is Item to check
     * @return true if the item is leather armor, false otherwise
     */
    public static boolean isLeatherArmor(ItemStack is) {
        switch (is.getType()) {
            case LEATHER_BOOTS:
            case LEATHER_CHESTPLATE:
            case LEATHER_HELMET:
            case LEATHER_LEGGINGS:
                return true;

            default:
                return false;
        }
    }

    /**
     * Checks to see if an item is a gold armor piece.
     *
     * @param is Item to check
     * @return true if the item is gold armor, false otherwise
     */
    public static boolean isGoldArmor(ItemStack is) {
        switch (is.getType()) {
            case GOLD_BOOTS:
            case GOLD_CHESTPLATE:
            case GOLD_HELMET:
            case GOLD_LEGGINGS:
                return true;

            default:
                return false;
        }
    }

    /**
     * Checks to see if an item is an iron armor piece.
     *
     * @param is Item to check
     * @return true if the item is iron armor, false otherwise
     */
    public static boolean isIronArmor(ItemStack is) {
        switch (is.getType()) {
            case IRON_BOOTS:
            case IRON_CHESTPLATE:
            case IRON_HELMET:
            case IRON_LEGGINGS:
                return true;

            default:
                return false;
        }
    }

    /**
     * Checks to see if an item is a diamond armor piece.
     *
     * @param is Item to check
     * @return true if the item is diamond armor, false otherwise
     */
    public static boolean isDiamondArmor(ItemStack is) {
        switch (is.getType()) {
            case DIAMOND_BOOTS:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_HELMET:
            case DIAMOND_LEGGINGS:
                return true;

            default:
                return false;
        }
    }
}
