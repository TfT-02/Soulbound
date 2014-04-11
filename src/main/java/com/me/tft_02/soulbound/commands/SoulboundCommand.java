package com.me.tft_02.soulbound.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.me.tft_02.soulbound.Soulbound;

public class SoulboundCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch (args.length) {
            case 0:
                sender.sendMessage("Soulbound version " + Soulbound.p.getDescription().getVersion());
                return printUsage(sender);
            case 1:
                if (args[0].equalsIgnoreCase("reload")) {
                    return reloadConfiguration(sender);
                }
                else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                    return helpPages(sender, args);
                }
            default:
                return false;
        }
    }

    private boolean reloadConfiguration(CommandSender sender) {
        if (sender instanceof Player && !sender.hasPermission("soulbound.commands.reload")) {
            return false;
        }

        Soulbound.p.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");
        return false;
    }

    private boolean helpPages(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Can't use this from the console, sorry!");
            return false;
        }

        if (args.length >= 2 && Integer.parseInt(args[1]) > 1) {
            getHelpPage(Integer.parseInt(args[1]), sender);
            return true;
        }

        getHelpPage(1, sender);
        return true;
    }

    private void getHelpPage(int page, CommandSender sender) {
        int maxPages = 2;
        int nextPage = page + 1;
        if (page > maxPages) {
            sender.sendMessage(ChatColor.RED + "This page does not exist." + ChatColor.GOLD + " /help [0-" + maxPages + "]");
            return;
        }

        String dot = ChatColor.DARK_RED + "* ";
        sender.sendMessage(ChatColor.GRAY + "-----[ " + ChatColor.GOLD + "Soulbound Help" + ChatColor.GRAY + " ]----- Page " + page + "/" + maxPages);
        if (page == 1) {
            sender.sendMessage(ChatColor.GOLD + "How does it work?");
            sender.sendMessage(dot + ChatColor.GRAY + "Soulbound items are special items which are bound to a sender.");
            sender.sendMessage(dot + ChatColor.GRAY + "Players are prevented from doing certain actions with Soulbound items, such as:");
            sender.sendMessage(dot + ChatColor.GRAY + "dropping them on the ground, storing them in chests or giving them to other players.");
            sender.sendMessage(dot + ChatColor.GRAY + "Items marked as 'Bind on Pickup' will get Soulbound as soon as they get picked up.");
            sender.sendMessage(dot + ChatColor.GRAY + "Items marked as 'Bind on Use' will get Soulbound as soon as they get used.");
            sender.sendMessage(dot + ChatColor.GRAY + "Items marked as 'Bind on Equip' will get Soulbound as soon as they get equipped.");
        }
        if (page == 2) {
            sender.sendMessage(ChatColor.GOLD + "Commands:");
            if (sender.hasPermission("soulbound.commands.bindonpickup")) {
                sender.sendMessage(dot + ChatColor.GREEN + "/soulbound" + ChatColor.GRAY + " Check the status of the plugin.");
            }
            if (sender.hasPermission("soulbound.commands.bind")) {
                sender.sendMessage(dot + ChatColor.GREEN + "/bind <sender>" + ChatColor.GRAY + " Soulbound the item currently in hand.");
                sender.sendMessage(dot + ChatColor.GREEN + "/bind <sender> inventory" + ChatColor.GRAY + " Soulbound an entire inventory.");
            }
            if (sender.hasPermission("soulbound.commands.bindonpickup")) {
                sender.sendMessage(dot + ChatColor.GREEN + "/bindonpickup" + ChatColor.GRAY + " Mark the item in hand as 'Bind on Pickup'");
            }
            if (sender.hasPermission("soulbound.commands.bindonuse")) {
                sender.sendMessage(dot + ChatColor.GREEN + "/bindonuse" + ChatColor.GRAY + " Mark the item in hand as 'Bind on Use'");
            }
            if (sender.hasPermission("soulbound.commands.bindonequip")) {
                sender.sendMessage(dot + ChatColor.GREEN + "/bindonequip" + ChatColor.GRAY + " Mark the item in hand as 'Bind on Equip'");
            }
            if (sender.hasPermission("soulbound.commands.unbind")) {
                sender.sendMessage(dot + ChatColor.GREEN + "/unbind" + ChatColor.GRAY + " Unbind the item in hand.");
            }
        }
        if (nextPage <= maxPages) {
            sender.sendMessage(ChatColor.GOLD + "Type /soulbound help " + nextPage + " for more");
        }
    }

    private boolean printUsage(CommandSender sender) {
        sender.sendMessage("Usage: /soulbound [reload | help]");
        return false;
    }
}
