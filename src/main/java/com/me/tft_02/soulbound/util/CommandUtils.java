package com.me.tft_02.soulbound.util;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class CommandUtils {

    public static String[] extractArgs(String command) {
        String[] args = {""};
        String[] split = command.split(" ", 2);

        if (split.length > 1) {
            args = split[1].split(" ");
        }

        return args;
    }

    public static boolean noConsoleUsage(CommandSender sender) {
        if (sender instanceof Player) {
            return false;
        }

        sender.sendMessage("This command does not support console usage.");
        return true;
    }

    public static boolean isOffline(CommandSender sender, OfflinePlayer player) {
        if (player != null && player.isOnline()) {
            return false;
        }

        sender.sendMessage(ChatColor.RED + "This command does not work for offline players.");
        return true;
    }
}
