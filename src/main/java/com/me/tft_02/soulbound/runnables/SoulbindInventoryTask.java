package com.me.tft_02.soulbound.runnables;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.me.tft_02.soulbound.config.ItemsConfig;
import com.me.tft_02.soulbound.datatypes.ActionType;
import com.me.tft_02.soulbound.util.ItemUtils;

public class SoulbindInventoryTask extends BukkitRunnable {
    private Player player;
    private ActionType actionType;

    public SoulbindInventoryTask(Player player, ActionType actionType) {
        this.player = player;
        this.actionType = actionType;
    }

    @Override
    public void run() {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && ItemsConfig.getInstance().isActionItem(itemStack, actionType)) {
                ItemUtils.soulbindItem(player, itemStack);
            }
        }
    }
}
