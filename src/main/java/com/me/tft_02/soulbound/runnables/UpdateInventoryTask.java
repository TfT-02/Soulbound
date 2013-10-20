package com.me.tft_02.soulbound.runnables;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateInventoryTask extends BukkitRunnable {
    private Player player;

    public UpdateInventoryTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        if (player.isValid()) {
            player.updateInventory();
        }
    }
}
