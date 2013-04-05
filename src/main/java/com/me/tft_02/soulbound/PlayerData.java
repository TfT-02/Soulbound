package com.me.tft_02.soulbound;

import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerData {
    Soulbound plugin;

    public PlayerData(Soulbound instance) {
        plugin = instance;
    }

    public static HashMap<String, List<ItemStack>> playerSoulboundItems = new HashMap<String, List<ItemStack>>();

    public static void storeItemsDeath(Player player, List<ItemStack> items) {
        String playerName = player.getName();

        if (playerSoulboundItems.containsKey(playerName)) {
            playerSoulboundItems.put(playerName, null);
        }

        playerSoulboundItems.put(playerName, items);
    }

    public static List<ItemStack> retrieveItemsDeath(Player player) {
        String playerName = player.getName();

        if (playerSoulboundItems.containsKey(playerName)) {
            return playerSoulboundItems.get(playerName);
        }
        else {
            return null;
        }
    }
}
