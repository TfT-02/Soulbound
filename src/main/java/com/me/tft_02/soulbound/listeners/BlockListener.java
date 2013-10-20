package com.me.tft_02.soulbound.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.util.DurabilityUtils;

public class BlockListener implements Listener {
    Soulbound plugin;

    public BlockListener(Soulbound instance) {
        plugin = instance;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        ItemStack itemStack = event.getPlayer().getItemInHand();

        DurabilityUtils.handleInfiniteDurability(itemStack);
    }
}
