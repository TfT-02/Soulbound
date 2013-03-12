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
            sender.sendMessage("Soulbound version " + plugin.getDescription().getVersion());
            return printUsage(sender);
        }
        else if (cmd.getName().equalsIgnoreCase("bind") || cmd.getName().equalsIgnoreCase("bound")) {
            return soulbindCommand(sender, args);
        }
        else if (cmd.getName().equalsIgnoreCase("bindonpickup")) {
            return bindonpickupCommand(sender, args);
        }
        else if (cmd.getName().equalsIgnoreCase("bindonuse")) {
            return bindonuseCommand(sender, args);
        }
        else if (cmd.getName().equalsIgnoreCase("bindonequip")) {
            return bindonequipCommand(sender, args);
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

        if (!player.hasPermission("soulbound.commands.unbind")) {
            return false;
        }

        ItemStack itemInHand = player.getItemInHand();

        if ((itemInHand.getType() == Material.AIR) && !ItemUtils.isSoulbound(itemInHand)) {
            return false;
        }

        ItemUtils.unbindItem(itemInHand);
        player.sendMessage(ChatColor.GRAY + "Item no longer Soulbound.");
        return true;
    }

    private boolean helpPages(CommandSender sender) {
        sender.sendMessage(ChatColor.GRAY + "Bind items in hand with /bind [name]");
        return false;
    }

    private boolean printUsage(CommandSender sender) {
        sender.sendMessage("Usage: /soulbound [reload | help]");
        return false;
    }

    private boolean soulbindCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Can't use this from the console, sorry!");
            return false;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("soulbound.commands.bind")) {
            return false;
        }

        Player target;
        switch (args.length) {
            case 1:
                target = Bukkit.getPlayer(args[0]);
            default:
                target = player;
        }

        ItemStack itemInHand = player.getItemInHand();

        if ((itemInHand.getType() == Material.AIR) && ItemUtils.isSoulbound(itemInHand)) {
            return false;
        }
        ItemUtils.soulbindItem(target, itemInHand);
        player.sendMessage(ChatColor.GRAY + "Item is now " + ChatColor.GOLD + "Soulbound " + ChatColor.GRAY + "to " + ChatColor.DARK_AQUA + target.getName());

        return true;
    }

    private boolean bindonpickupCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Can't use this from the console, sorry!");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("soulbound.commands.bindonpickup")) {
            return false;
        }

        ItemStack itemInHand = player.getItemInHand();

        if ((itemInHand.getType() == Material.AIR) && ItemUtils.isSoulbound(itemInHand)) {
            return false;
        }
        ItemUtils.bopItem(itemInHand);
        player.sendMessage(ChatColor.GRAY + "Item is now " + ChatColor.DARK_RED + "Bind on pickup");

        return true;
    }

    private boolean bindonuseCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Can't use this from the console, sorry!");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("soulbound.commands.bindonuse")) {
            return false;
        }

        ItemStack itemInHand = player.getItemInHand();

        if ((itemInHand.getType() == Material.AIR) && ItemUtils.isSoulbound(itemInHand)) {
            return false;
        }
        ItemUtils.bouItem(itemInHand);
        player.sendMessage(ChatColor.GRAY + "Item is now " + ChatColor.DARK_RED + "Bind on Use");

        return true;
    }

    private boolean bindonequipCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Can't use this from the console, sorry!");
            return false;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("soulbound.commands.bindonequip")) {
            return false;
        }

        ItemStack itemInHand = player.getItemInHand();

        if ((itemInHand.getType() == Material.AIR) && ItemUtils.isSoulbound(itemInHand)) {
            return false;
        }
        ItemUtils.boeItem(itemInHand);
        player.sendMessage(ChatColor.GRAY + "Item is now " + ChatColor.DARK_RED + "Bind on Equip");

        return true;
    }

    private boolean reloadConfiguration(CommandSender sender) {
        if (sender instanceof Player && !((Player) sender).hasPermission("soulbound.commands.reload")) {
            return false;
        }

        plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Configuration reloaded.");
        return false;
    }
}
