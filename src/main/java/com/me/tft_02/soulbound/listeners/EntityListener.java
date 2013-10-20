package com.me.tft_02.soulbound.listeners;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.util.DurabilityUtils;

public class EntityListener implements Listener {
    Soulbound plugin;

    public EntityListener(Soulbound instance) {
        plugin = instance;
    }

    /**
     * Check EntityDamageByEntityEvent events.
     * 
     * @param event The event to check
     */
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onEntityDeath(EntityDamageByEntityEvent event) {
        if (event.getDamage() == 0 || event.getEntity().isDead()) {
            return;
        }

        if (event.getEntity() instanceof LivingEntity) {
            combatChecks(event);
        }
    }

    /**
     * Apply combat modifiers
     * 
     * @param event The event to run the combat checks on.
     */
    void combatChecks(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        EntityType damagerType = damager.getType();

        switch (damagerType) {
            case PLAYER:
                Player attacker = (Player) event.getDamager();
                ItemStack itemInHand = attacker.getItemInHand();

                DurabilityUtils.handleInfiniteDurability(itemInHand);
            default:
                return;
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onEntityShootBow(EntityShootBowEvent event) {
        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            DurabilityUtils.handleInfiniteDurability(((Player) entity).getItemInHand());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    private void onEntityDamage(EntityDamageEvent event) {
        if (event.getDamage() == 0 || event.getEntity().isDead()) {
            return;
        }

        Entity entity = event.getEntity();

        if (entity instanceof Player) {
            Player player = (Player) entity;

            for (ItemStack itemStack : player.getInventory().getArmorContents()) {
                DurabilityUtils.handleInfiniteDurability(itemStack);
            }
        }
    }
}
