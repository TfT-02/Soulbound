package com.me.tft_02.soulbound.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.config.Config;
import com.me.tft_02.soulbound.config.ItemsConfig;
import com.me.tft_02.soulbound.datatypes.ActionType;
import com.me.tft_02.soulbound.runnables.UpdateArmorTask;
import com.me.tft_02.soulbound.util.ItemUtils;
import com.me.tft_02.soulbound.util.ItemUtils.ItemType;
import com.me.tft_02.soulbound.util.Permissions;

public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    void onInventoryClick(InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();

        SlotType slotType = event.getSlotType();
        InventoryType inventoryType = event.getInventory().getType();

        if (inventoryType == null) {
            return;
        }

        if (itemStack == null) {
            return;
        }

        if (entity instanceof Player) {
            Player player = (Player) entity;
            switch (slotType) {
                case ARMOR:
                    new UpdateArmorTask(player).runTaskLater(Soulbound.p, 2);
                    return;
                case CONTAINER:
                    ItemType itemType = ItemUtils.getItemType(itemStack);
                    switch (itemType) {
                        case BIND_ON_PICKUP:
                            ItemUtils.soulbindItem(player, itemStack);
                            return;
                        default:
                            return;
                    }
                default:
                    if (ItemUtils.isEquipable(itemStack) && event.isShiftClick()) {
                        new UpdateArmorTask(player).runTaskLater(Soulbound.p, 2);
                        return;
                    }
                    break;
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClickEvent(InventoryClickEvent event) {
        HumanEntity entity = event.getWhoClicked();

        if (!(entity instanceof Player)) {
            return;
        }

        Player player = (Player) entity;
        ItemStack itemStack = event.getCurrentItem();
        InventoryType inventoryType = event.getInventory().getType();

        if (inventoryType == null) {
            return;
        }

        ItemType itemType = ItemUtils.getItemType(itemStack);

        if (itemType != ItemType.SOULBOUND) {
            return;
        }

        if (!Config.getInstance().getAllowItemStoring() && !(inventoryType == InventoryType.CRAFTING)) {
            event.setCancelled(true);
        }

        if (ItemUtils.isBindedPlayer(player, itemStack) || Permissions.pickupBypass(player)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryMoveEvent(InventoryMoveItemEvent event) {
        ItemStack itemStack = event.getItem();
        ItemType itemType = ItemUtils.getItemType(itemStack);

        if (itemType == ItemType.SOULBOUND) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryPickupItemEvent(InventoryPickupItemEvent event) {
        ItemStack itemStack = event.getItem().getItemStack();
        ItemType itemType = ItemUtils.getItemType(itemStack);

        if (itemType == ItemType.SOULBOUND) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        HumanEntity humanEntity = event.getPlayer();
        Inventory inventory = event.getInventory();

        if (!(humanEntity instanceof Player)) {
            return;
        }

        Player player = (Player) humanEntity;

        for (ItemStack itemStack : inventory.getContents()) {
            if (itemStack != null && ItemsConfig.getInstance().isActionItem(itemStack, ActionType.OPEN_CHEST)) {
                ItemUtils.soulbindItem(player, itemStack);
            }
        }
    }

    /**
     * Check CraftItemEvent events.
     *
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onCraftItem(CraftItemEvent event) {
        HumanEntity humanEntity = event.getWhoClicked();

        if (!(humanEntity instanceof Player)) {
            return;
        }

        Player player = (Player) humanEntity;

        ItemStack itemStack = event.getRecipe().getResult();

        if (ItemsConfig.getInstance().isActionItem(itemStack, ActionType.CRAFT)) {
            event.getInventory().setResult(ItemUtils.soulbindItem(player, itemStack));
        }
    }

    /**
     * Check EnchantItemEvent events.
     *
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    private void onEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        ItemStack itemStack = event.getItem();

        if (ItemUtils.isBindOnUse(itemStack)) {
            ItemUtils.soulbindItem(player, itemStack);
            return;
        }
    }
}
