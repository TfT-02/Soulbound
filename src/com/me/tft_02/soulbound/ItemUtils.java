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
        if (itemMeta.hasLore() && isBindOnPickup(itemStack)) {
            itemMeta.getLore().remove(ChatColor.DARK_RED + "Bind on pickup");
        }

        List<String> itemLore = new ArrayList<String>();
        if (itemMeta.hasLore()) {
            itemLore.addAll(itemMeta.getLore());
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
        itemLore.addAll(itemMeta.getLore());
        itemLore.add(ChatColor.DARK_RED + "Bind on pickup");
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
            if (itemLore.contains(ChatColor.DARK_RED + "Bind on pickup")) {
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
            if (itemLore.contains(ChatColor.DARK_RED + "Bind on use")) {
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
            if (itemLore.contains(ChatColor.DARK_RED + "Bind on equip")) {
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
}
