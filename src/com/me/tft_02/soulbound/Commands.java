package com.me.tft_02.soulbound;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.util.ItemUtils;

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
                    return helpPages(sender, args);
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
            sender.sendMessage("Can't use this from the console, sorry!");
            return false;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("soulbound.commands.unbind")) {
            return false;
        }

        ItemStack itemInHand = player.getItemInHand();

        if ((itemInHand.getType() == Material.AIR) || !ItemUtils.isSoulbound(itemInHand)) {
            player.sendMessage(ChatColor.GRAY + "You can't " + ChatColor.GOLD + "Unbind " + ChatColor.GRAY + "this item.");
            return false;
        }

        ItemUtils.unbindItem(itemInHand);
        player.sendMessage(ChatColor.GRAY + "Item no longer Soulbound.");
        return true;
    }

    private boolean helpPages(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Can't use this from the console, sorry!");
            return false;
        }
        Player player = (Player) sender;

        if (args.length == 2) {
            if (Integer.parseInt(args[1]) > 1) {
                getHelpPage(Integer.parseInt(args[1]), player);
                return true;
            }
            else {
                getHelpPage(1, player);
                return true;
            }
        }
        else {
            getHelpPage(1, player);
            return true;
        }
    }

    private void getHelpPage(int page, Player player) {
        int maxPages = 2;
        int nextPage = page + 1;
        if (page > maxPages) {
            player.sendMessage(ChatColor.RED + "This page does not exist." + ChatColor.GOLD + " /help [0-" + maxPages + "]");
        }
        else {
            String dot = ChatColor.DARK_RED + "* ";
            player.sendMessage(ChatColor.GRAY + "-----[ " + ChatColor.GOLD + "Soulbound Help" + ChatColor.GRAY + " ]----- Page " + page + "/" + maxPages);
            if (page == 1) {
                player.sendMessage(ChatColor.GOLD + "How does it work?");
                player.sendMessage(dot + ChatColor.GRAY + "Soulbound items are special items which are bound to a player.");
                player.sendMessage(dot + ChatColor.GRAY + "Players are prevented from doing certain actions with Soulbound items, such as:");
                player.sendMessage(dot + ChatColor.GRAY + "dropping them on the ground, storing them in chests or giving them to other players.");
                player.sendMessage(dot + ChatColor.GRAY + "Items marked as 'Bind on Pickup' will get Soulbound as soon as they get picked up.");
                player.sendMessage(dot + ChatColor.GRAY + "Items marked as 'Bind on Use' will get Soulbound as soon as they get used.");
                player.sendMessage(dot + ChatColor.GRAY + "Items marked as 'Bind on Equip' will get Soulbound as soon as they get equipped.");
            }
            if (page == 2) {
                player.sendMessage(ChatColor.GOLD + "Commands:");
                if (player.hasPermission("soulbound.commands.bindonpickup")) {
                    player.sendMessage(dot + ChatColor.GREEN + "/soulbound" + ChatColor.GRAY + " Check the status of the plugin.");
                }
                if (player.hasPermission("soulbound.commands.bind")) {
                    player.sendMessage(dot + ChatColor.GREEN + "/bind <player>" + ChatColor.GRAY + " Soulbound the item currently in hand.");
                }
                if (player.hasPermission("soulbound.commands.bindonpickup")) {
                    player.sendMessage(dot + ChatColor.GREEN + "/bindonpickup" + ChatColor.GRAY + " Mark the item in hand as 'Bind on Pickup'");
                }
                if (player.hasPermission("soulbound.commands.bindonuse")) {
                    player.sendMessage(dot + ChatColor.GREEN + "/bindonuse" + ChatColor.GRAY + " Mark the item in hand as 'Bind on Use'");
                }
                if (player.hasPermission("soulbound.commands.bindonequip")) {
                    player.sendMessage(dot + ChatColor.GREEN + "/bindonequip" + ChatColor.GRAY + " Mark the item in hand as 'Bind on Equip'");
                }
                if (player.hasPermission("soulbound.commands.unbind")) {
                    player.sendMessage(dot + ChatColor.GREEN + "/unbind" + ChatColor.GRAY + " Unbind the item in hand.");
                }
            }
            if (nextPage <= maxPages)
                player.sendMessage(ChatColor.GOLD + "Type /soulbound help " + nextPage + " for more");
        }
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

        if ((itemInHand.getType() == Material.AIR) || ItemUtils.isSoulbound(itemInHand)) {
            player.sendMessage(ChatColor.GRAY + "You can't " + ChatColor.GOLD + "Soulbound " + ChatColor.GRAY + "this item.");
            return false;
        }
        ItemUtils.soulbindItem(target, itemInHand);
        if (ItemUtils.isSoulbound(itemInHand)) {
            player.sendMessage(ChatColor.GRAY + "Item is now " + ChatColor.GOLD + "Soulbound " + ChatColor.GRAY + "to " + ChatColor.DARK_AQUA + target.getName());
        }
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

        if ((itemInHand.getType() == Material.AIR) || ItemUtils.isSoulbound(itemInHand)) {
            player.sendMessage(ChatColor.GRAY + "You can't " + ChatColor.GOLD + "Soulbound " + ChatColor.GRAY + "this item.");
            return false;
        }

        ItemUtils.unbindItem(itemInHand);
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

        if ((itemInHand.getType() == Material.AIR) || ItemUtils.isSoulbound(itemInHand)) {
            player.sendMessage(ChatColor.GRAY + "You can't " + ChatColor.GOLD + "Soulbound " + ChatColor.GRAY + "this item.");
            return false;
        }

        ItemUtils.unbindItem(itemInHand);
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

        if ((itemInHand.getType() == Material.AIR) || ItemUtils.isSoulbound(itemInHand)) {
            player.sendMessage(ChatColor.GRAY + "You can't " + ChatColor.GOLD + "Soulbound " + ChatColor.GRAY + "this item.");
            return false;
        }

        ItemUtils.unbindItem(itemInHand);
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
