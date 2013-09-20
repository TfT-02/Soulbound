package com.me.tft_02.soulbound.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Material;

import com.me.tft_02.soulbound.datatypes.ActionType;

public class Config extends AutoUpdateConfigLoader {
    private static  Config instance;

    private Config() {
        super("config.yml");
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }

        return instance;
    }

    @Override
    protected void loadKeys() {}

    /* @formatter:off */

    /* GENERAL SETTINGS */
//    public String getLocale() { return config.getString("General.Locale", "en_us"); }
//    public int getSaveInterval() { return config.getInt("General.Save_Interval", 10); }
    public boolean getStatsTrackingEnabled() { return config.getBoolean("General.Stats_Tracking", true); }
    public boolean getUpdateCheckEnabled() { return config.getBoolean("General.Update_Check", true); }
    public boolean getPreferBeta() { return config.getBoolean("General.Prefer_Beta", false); }
    public boolean getVerboseLoggingEnabled() { return config.getBoolean("General.Verbose_Logging", false); }
    public boolean getConfigOverwriteEnabled() { return config.getBoolean("General.Config_Update_Overwrite", true); }

    /* @formatter:on */

    /* SOULBOUND SETTINGS */
    public boolean getFeedbackEnabled() { return config.getBoolean("Soulbound.Feedback_Messages_Enabled", true); }
    public boolean getPreventItemDrop() { return config.getBoolean("Soulbound.Prevent_Item_Drop", false); }
    public boolean getAllowItemStoring() { return config.getBoolean("Soulbound.Allow_Item_Storing", true); }
    public boolean getInfiniteDurability() { return config.getBoolean("Soulbound.Infinite_Durability", false); }

    public List<String> getBlockedCommands() { return config.getStringList("Soulbound.Blocked_Commands"); }
    public List<String> getBindCommands() { return config.getStringList("Soulbound.Commands_Bind_When_Used"); }

    // Binding certain items by material name
    public HashSet<Material> getAlwaysSoulboundItems(ActionType action) {
        HashSet<Material> items = new HashSet<Material>();

        for (String item : config.getStringList("Misc.Soulbound_On_Action." + action.toString().toLowerCase())) {
            Material material = Material.getMaterial(item.toUpperCase());

            if (material != null) {
                items.add(material);
            }
        }
        return items;
    }

    // EpicBossRecoded config settings

    public boolean getEBRBindOnPickup() { return config.getBoolean("Dependency_Plugins.EpicBossRecoded.BindOnPickup"); }
    public boolean getEBRBindOnEquip() { return config.getBoolean("Dependency_Plugins.EpicBossRecoded.BindOnEquip"); }
    public boolean getEBRBindOnUse() { return config.getBoolean("Dependency_Plugins.EpicBossRecoded.BindOnUse");}

    // DiabloDrops config settings

    public List<String> getDiabloDropsBindOnPickupTiers() { return getDiabloDropsItemTiers("BindOnPickup");}
    public List<String> getDiabloDropsBindOnUseTiers() { return getDiabloDropsItemTiers("BindOnUse");}
    public List<String> getDiabloDropsBindOnEquipTiers() { return getDiabloDropsItemTiers("BindOnEquip"); }

    public List<String> getDiabloDropsItemTiers(String bindType) {
        String[] tiersString = config.getString("Dependency_Plugins.DiabloDrops." + bindType).replaceAll(" ", "").split("[,]");
        List<String> tiers = new ArrayList<String>();

        for (String tier : tiersString) {
            tiers.add(tier);
        }
        return tiers;
    }

    // LoreLocks config settings

    public boolean getLoreLocksBindKeys() { return config.getBoolean("Dependency_Plugins.LoreLocks.Bind_Keys"); }

    // MythicDrops config settings

    public List<String> getMythicDropsBindOnPickupTiers() { return getMythicDropsItemTiers("BindOnPickup"); }
    public List<String> getMythicDropsBindOnUseTiers() { return getMythicDropsItemTiers("BindOnUse"); }
    public List<String> getMythicDropsBindOnEquipTiers() { return getMythicDropsItemTiers("BindOnEquip"); }

    public List<String> getMythicDropsItemTiers(String bindType) {
        String[] tiersString = config.getString("Dependency_Plugins.MythicDrops." + bindType).replaceAll(" ", "").split("[,]");
        List<String> tiers = new ArrayList<String>();

        for (String tier : tiersString) {
            tiers.add(tier);
        }
        return tiers;
    }
}
