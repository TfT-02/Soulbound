package com.me.tft_02.soulbound.runnables;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.me.tft_02.soulbound.config.ItemsConfig;
import com.me.tft_02.soulbound.datatypes.ActionType;
import com.me.tft_02.soulbound.util.ItemUtils;

public class SoulbindInventoryTask extends BukkitRunnable {
    private Player player;

    public SoulbindInventoryTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        for (ItemStack itemStack : player.getInventory().getContents()) {
            if (itemStack != null && ItemsConfig.getInstance().isActionItem(itemStack, ActionType.RESPAWN)) {
                ItemUtils.soulbindItem(player, itemStack);
            }
        }
    }
}
