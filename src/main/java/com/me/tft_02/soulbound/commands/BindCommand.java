package com.me.tft_02.soulbound.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.config.Config;
import com.me.tft_02.soulbound.util.CommandUtils;
import com.me.tft_02.soulbound.util.ItemUtils;

public class BindCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (CommandUtils.noConsoleUsage(sender)) {
            return true;
        }

        if (!sender.hasPermission("soulbound.commands.bind")) {
            return false;
        }

        boolean bindFullInventory = false;

        Player player = (Player) sender;
        Player target;
        switch (args.length) {
            case 1:
                target = Bukkit.getPlayerExact(args[0]);

                if (CommandUtils.isOffline(sender, target)) {
                    return true;
                }
            case 2:
                if (!args[1].equalsIgnoreCase("inventory")) {
                    player.sendMessage(ChatColor.RED + "Proper usage: " + ChatColor.GREEN + "/bind <player> inventory");
                    return true;
                }

                bindFullInventory = true;
                target = Bukkit.getPlayerExact(args[0]);

                if (CommandUtils.isOffline(sender, target)) {
                    return true;
                }
                break;
            default:
                target = player;
        }

        if (bindFullInventory) {
            return handleBindFullInventory(player, target);
        }

        ItemStack itemInHand = player.getItemInHand();

        if ((itemInHand.getType() == Material.AIR) || ItemUtils.isSoulbound(itemInHand)) {
            player.sendMessage(ChatColor.GRAY + "You can't " + ChatColor.GOLD + "Soulbound " + ChatColor.GRAY + "this item.");
            return false;
        }

        ItemUtils.soulbindItem(target, itemInHand);

        if (ItemUtils.isSoulbound(itemInHand) && Config.getInstance().getFeedbackEnabled()) {
            player.sendMessage(ChatColor.GRAY + "Item is now " + ChatColor.GOLD + "Soulbound " + ChatColor.GRAY + "to " + ChatColor.DARK_AQUA + target.getName());
        }
        return true;
    }

    private boolean handleBindFullInventory(Player player, Player target) {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && !(itemStack.getType() == Material.AIR) && !ItemUtils.isSoulbound(itemStack)) {
                ItemUtils.soulbindItem(target, itemStack);
            }
        }

        if (Config.getInstance().getFeedbackEnabled()) {
            player.sendMessage(ChatColor.GRAY + "All items in " + ChatColor.DARK_AQUA + target.getName() + ChatColor.GRAY + "'s inventory are now " + ChatColor.GOLD + "Soulbound " + ChatColor.GRAY + "to " + ChatColor.DARK_AQUA + target.getName());
        }
        return true;
    }
}
