package com.me.tft_02.soulbound.util;

import org.bukkit.entity.Player;

public class Permissions {

    public static int getSoulbindMaximum(Player player) {
        String permission = "soulbound.maximum_allowed.";
        int amount = -1;

        if (player.hasPermission(permission)) {
            amount = Integer.parseInt(permission.split(".")[2]);
        }
        return amount;
    }
}
