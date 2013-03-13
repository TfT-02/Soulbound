package com.me.tft_02.soulbound.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;

public class SoulbindItemEvent extends PlayerEvent implements Cancellable {
    private ItemStack itemStack;
    private boolean cancelled;

    public SoulbindItemEvent(Player player, ItemStack itemStack) {
        super(player);

        this.setItemStack(itemStack);
        this.cancelled = false;
    }

    /**
     * @return The itemStack being soulbound
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * @return Set the itemStack being soulbound
     */
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
