package com.me.tft_02.soulbound.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.events.SoulbindItemEvent;

public class ItemUtils {
    public enum ItemType {
        NORMAL,
        SOULBOUND,
        BIND_ON_PICKUP,
        BIND_ON_USE,
        BIND_ON_EQUIP;
    }

    Soulbound plugin;

    public ItemUtils(Soulbound instance) {
        plugin = instance;
    }

    public static ItemStack soulbindItem(Player player, ItemStack itemStack) {
        if (itemStack == null) {
            return itemStack;
        }

        if (isSoulbound(itemStack)) {
            return itemStack;
        }

        SoulbindItemEvent soulbindItemEvent = new SoulbindItemEvent(player, itemStack);
        Soulbound.p.getServer().getPluginManager().callEvent(soulbindItemEvent);
        itemStack = soulbindItemEvent.getItemStack();

        if (soulbindItemEvent.isCancelled()) {
            return itemStack;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        List<String> itemLore = new ArrayList<String>();

        if (itemMeta.hasLore()) {
            List<String> oldLore = itemMeta.getLore();
            oldLore.remove(ChatColor.DARK_RED + "Bind on Pickup");
            oldLore.remove(ChatColor.DARK_RED + "Bind on Equip");
            oldLore.remove(ChatColor.DARK_RED + "Bind on Use");
            itemLore.addAll(oldLore);
        }

        itemLore.add(ChatColor.GOLD + "Soulbound");
        itemLore.add(player.getName());
        itemLore.add(ChatColor.BLACK + player.getUniqueId().toString());
        itemMeta.setLore(itemLore);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static ItemStack bopItem(ItemStack itemStack) {
        if (itemStack == null) {
            return itemStack;
        }

        if (isBindOnPickup(itemStack)) {
            return itemStack;
        }

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
        if (itemStack == null) {
            return itemStack;
        }

        if (isBindOnEquip(itemStack)) {
            return itemStack;
        }

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
        if (itemStack == null) {
            return itemStack;
        }

        if (isBindOnUse(itemStack)) {
            return itemStack;
        }

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
        if (itemStack == null) {
            return null;
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta.hasLore() && isSoulbound(itemStack)) {
            List<String> oldLore = itemMeta.getLore();
            int loreSize = oldLore.size();

            if (loreSize <= 3) {
                itemMeta.setLore(null);
                itemStack.setItemMeta(itemMeta);
                return itemStack;
            }

            List<String> itemLore = new ArrayList<String>();
            itemLore.addAll(oldLore);
            int index = itemLore.indexOf(ChatColor.GOLD + "Soulbound");
            itemLore.remove(index);
            itemLore.remove(index + 1);

            if (loreSize > 3) {
                itemLore.remove(index + 2);
            }

            itemMeta.setLore(itemLore);
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
        List<String> itemLore = itemStack.getItemMeta().getLore();

        checkNameChange(player, itemStack);
        return itemLore.contains(player.getName()) || itemLore.contains(player.getUniqueId().toString());
    }

    private static void checkNameChange(Player player, ItemStack itemStack) {
        List<String> itemLore = itemStack.getItemMeta().getLore();
        if (!itemLore.contains(player.getName()) && itemLore.contains(player.getUniqueId().toString())) {
            return;
        }

        int index = itemLore.indexOf(ChatColor.GOLD + "Soulbound") + 1;
        itemLore.remove(index);
        itemLore.set(index, player.getName());
    }

    public static boolean isNormalItem(ItemStack itemStack) {
        return !itemStack.hasItemMeta() && !itemStack.getItemMeta().hasLore() || ItemUtils.getItemType(itemStack) == ItemType.NORMAL;
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
     * Checks to see if an item is an equipable item.
     *
     * @param is Item to check
     *
     * @return true if the item is equipable, false otherwise
     */
    public static boolean isEquipable(ItemStack is) {
        return isMinecraftArmor(is) || is.getType() == Material.SKULL_ITEM || is.getType() == Material.JACK_O_LANTERN;
    }

    /**
     * Checks to see if an item is a wearable armor piece.
     *
     * @param is Item to check
     *
     * @return true if the item is armor, false otherwise
     */
    public static boolean isMinecraftArmor(ItemStack is) {
        return isLeatherArmor(is) || isGoldArmor(is) || isIronArmor(is) || isDiamondArmor(is) || isChainmailArmor(is);
    }

    /**
     * Checks to see if an item is a leather armor piece.
     *
     * @param is Item to check
     *
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
     *
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
     *
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
     *
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

    /**
     * Checks to see if an item is a chainmail armor piece.
     *
     * @param is Item to check
     *
     * @return true if the item is chainmail armor, false otherwise
     */
    public static boolean isChainmailArmor(ItemStack is) {
        switch (is.getType()) {
            case CHAINMAIL_BOOTS:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_HELMET:
            case CHAINMAIL_LEGGINGS:
                return true;

            default:
                return false;
        }
    }
}
