package com.me.tft_02.soulbound.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import com.me.tft_02.soulbound.datatypes.ActionType;
import com.me.tft_02.soulbound.datatypes.SoulbindItem;

public class ItemsConfig extends ConfigLoader {
    private static ItemsConfig instance;

    private List<SoulbindItem> soulbindOnCraft  = new ArrayList<SoulbindItem>();
    private List<SoulbindItem> soulbindOnOpenChest  = new ArrayList<SoulbindItem>();
    private List<SoulbindItem> soulbindOnPickupItem  = new ArrayList<SoulbindItem>();
    private List<SoulbindItem> soulbindOnDrop  = new ArrayList<SoulbindItem>();
    private List<SoulbindItem> soulbindOnRespawn  = new ArrayList<SoulbindItem>();

    public ItemsConfig() {
        super("items.yml");
        loadKeys();
    }

    public static ItemsConfig getInstance() {
        if (instance == null) {
            instance = new ItemsConfig();
        }

        return instance;
    }

    @Override
    protected void loadKeys() {
        ConfigurationSection configurationSection = config.getConfigurationSection("Items");

        if (configurationSection == null) {
            return;
        }

        Set<String> itemConfigSet = configurationSection.getKeys(false);

        for (String itemName : itemConfigSet) {
            String[] itemInfo = itemName.split("[|]");

            Material itemMaterial = Material.matchMaterial(itemInfo[0]);

            if (itemMaterial == null) {
                plugin.getLogger().warning("Invalid material name. This item will be skipped. - " + itemInfo[0]);
                continue;
            }

            byte itemData = (itemInfo.length == 2) ? Byte.valueOf(itemInfo[1]) : 0;
            MaterialData itemMaterialData = new MaterialData(itemMaterial, itemData);

            List<String> lore = null;
            if (config.contains("Items." + itemName + ".Lore")) {
                lore = config.getStringList("Items." + itemName + ".Lore");
            }

            String name = null;
            if (config.contains("Items." + itemName + ".Name")) {
                name = config.getString("Items." + itemName + ".Name");
            }

            SoulbindItem soulbindItem = new SoulbindItem(itemMaterialData, name, lore);

            List<String> actions = config.getStringList("Items." + itemName + ".Actions");

            for (ActionType actionType : ActionType.values()) {
                if (actions.contains(actionType.toString())) {
                    addSoulbindItem(actionType, soulbindItem);
                }
            }
        }
    }

    private void addSoulbindItem(ActionType actionType, SoulbindItem soulbindItem) {
        switch (actionType) {
            case CRAFT:
                soulbindOnCraft.add(soulbindItem);
                return;
            case OPEN_CHEST:
                soulbindOnOpenChest.add(soulbindItem);
                return;
            case PICKUP_ITEM:
                soulbindOnPickupItem.add(soulbindItem);
                return;
            case DROP_ITEM:
                soulbindOnDrop.add(soulbindItem);
                return;
            case RESPAWN:
                soulbindOnRespawn.add(soulbindItem);
                return;
        }
    }

    public List<SoulbindItem> getSoulbindItems(ActionType actionType) {
        switch (actionType) {
            case CRAFT:
                return soulbindOnCraft;
            case OPEN_CHEST:
                return soulbindOnOpenChest;
            case PICKUP_ITEM:
                return soulbindOnPickupItem;
            case DROP_ITEM:
                return soulbindOnDrop;
            case RESPAWN:
                return soulbindOnRespawn;
            default:
                return null;
        }
    }

    public boolean isActionItem(ItemStack itemStack, ActionType actionType) {
        for (SoulbindItem soulbindItem : getSoulbindItems(actionType)) {
            if (itemStack.getData().equals(soulbindItem.getMaterialData())) {
                if (itemStack.hasItemMeta()) {
                    ItemMeta itemMeta = itemStack.getItemMeta();

                    if (soulbindItem.getName() != null) {
                        if (!itemMeta.getDisplayName().contains(soulbindItem.getName())) {
                            return false;
                        }
                    }

                    if (soulbindItem.getLore() != null && !soulbindItem.getLore().isEmpty()) {
                        if (!itemMeta.getLore().contains(soulbindItem.getLore())) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }

        return false;
    }
}
