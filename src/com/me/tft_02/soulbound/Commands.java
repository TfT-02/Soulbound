package com.me.tft_02.soulbound;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Commands implements CommandExecutor {
    Soulbound plugin;

    public Commands(Soulbound instance) {
        plugin = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("soulbound")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    return reloadConfiguration(sender);
                }
                else if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                    return helpPages(sender);
                }
            }
            return printUsage(sender);
        }
        else if (cmd.getName().equalsIgnoreCase("bind") || cmd.getName().equalsIgnoreCase("bound")) {
            return soulbindCommand(sender, args);
        }
        else if (cmd.getName().equalsIgnoreCase("unbind") || cmd.getName().equalsIgnoreCase("unbound")) {
            return unbindCommand(sender, args);
        }
        return false;
    }

    private boolean unbindCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }

        Player player = (Player) sender;
        ItemStack itemInHand = player.getItemInHand();

        if ((itemInHand.getType() == Material.AIR) && !ItemUtils.isSoulbound(itemInHand)) {
            return false;
        }

        ItemUtils.unbindItem(itemInHand);
        player.sendMessage("Item unbinded.");
        return true;
    }

    private boolean helpPages(CommandSender sender) {
        // TODO Auto-generated method stub
        return false;
    }

    private boolean printUsage(CommandSender sender) {
        sender.sendMessage("Usage: /soulbound [reload]");
        return false;
    }

    private boolean soulbindCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Can't use this from the console, sorry!");
            return false;
        }
        Player player = (Player) sender;
        Player target;
        switch (args.length) {
            case 2:
                target = Bukkit.getPlayer(args[0]);
            default:
                target = player;
        }

        if (target == null) {
            return false;
        }

        ItemStack itemInHand = player.getItemInHand();

        if ((itemInHand.getType() == Material.AIR) && ItemUtils.isSoulbound(itemInHand)) {
            return false;
        }
        ItemUtils.soulbindItem(target, itemInHand);
        player.sendMessage("Item is not soulbound to " + target.getName());

        return true;
    }

    private boolean reloadConfiguration(CommandSender sender) {
        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");
        return false;
    }
}
