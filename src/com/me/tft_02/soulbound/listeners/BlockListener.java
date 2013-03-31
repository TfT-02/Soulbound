package com.me.tft_02.soulbound.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.util.ItemUtils;

public class BlockListener implements Listener {
    Soulbound plugin;

    public BlockListener(Soulbound instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getItemInHand();

        if (ItemUtils.isSoulbound(itemStack) && Soulbound.getInstance().getConfig().getBoolean("Soulbound.Infinite_Durability")) {
            itemStack.setDurability((short) 0);
            return;
        }
    }
}
