package com.me.tft_02.soulbound.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.util.CommandUtils;
import com.me.tft_02.soulbound.util.ItemUtils;

public class BindOnEquipCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (CommandUtils.noConsoleUsage(sender)) {
            return true;
        }

        switch (args.length) {
            case 0:
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

                if (ItemUtils.isBindOnEquip(itemInHand)) {
                    player.sendMessage(ChatColor.GRAY + "Item is now " + ChatColor.DARK_RED + "Bind on Equip");
                }
                else {
                    player.sendMessage(ChatColor.RED + "Cannot mark this item as " + ChatColor.DARK_RED + "Bind on Equip");
                }
                return true;

            default:
                return false;
        }
    }
}
