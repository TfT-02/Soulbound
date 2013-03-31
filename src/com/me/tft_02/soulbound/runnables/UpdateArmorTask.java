package com.me.tft_02.soulbound.runnables;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.me.tft_02.soulbound.util.ItemUtils;
import com.me.tft_02.soulbound.util.ItemUtils.ItemType;

public class UpdateArmorTask extends BukkitRunnable {
    private Player player;

    public UpdateArmorTask(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        handleBindOnEquip(player);
    }

    public void handleBindOnEquip(Player player) {
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && ItemUtils.getItemType(armor) == ItemType.BIND_ON_EQUIP) {
                ItemUtils.soulbindItem(player, armor);
            }
        }

        player.getInventory().setArmorContents(player.getInventory().getArmorContents());
    }
}
