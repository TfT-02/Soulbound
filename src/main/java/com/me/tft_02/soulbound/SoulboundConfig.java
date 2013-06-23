package com.me.tft_02.soulbound;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class SoulboundConfig {

    static FileConfiguration config = Soulbound.getInstance().getConfig();

    public static boolean getFeedbackMessagesEnabled() {
        return config.getBoolean("Soulbound.Feedback_Messages_Enabled", true);
    }
    
    // EpicBossRecoded config settings

    public boolean getEBRBindOnPickup() {
        return config.getBoolean("Dependency_Plugins.EpicBossRecoded.BindOnPickup");
    }

    public boolean getEBRBindOnEquip() {
        return config.getBoolean("Dependency_Plugins.EpicBossRecoded.BindOnEquip");
    }

    public boolean getEBRBindOnUse() {
        return config.getBoolean("Dependency_Plugins.EpicBossRecoded.BindOnUse");
    }

    // DiabloDrops config settings

    public List<String> getDiabloDropsBindOnPickupTiers() {
        return getDiabloDropsItemTiers("BindOnPickup");
    }

    public List<String> getDiabloDropsBindOnUseTiers() {
        return getDiabloDropsItemTiers("BindOnUse");
    }

    public List<String> getDiabloDropsBindOnEquipTiers() {
        return getDiabloDropsItemTiers("BindOnEquip");
    }

    public List<String> getDiabloDropsItemTiers(String bindType) {
        String[] tiersString = config.getString("Dependency_Plugins.DiabloDrops." + bindType).replaceAll(" ", "").split("[,]");
        List<String> tiers = new ArrayList<String>();

        for (String tier : tiersString) {
            tiers.add(tier);
        }
        return tiers;
    }

    // LoreLocks config settings

    public boolean getLoreLocksBindKeys() {
        return config.getBoolean("Dependency_Plugins.LoreLocks.Bind_Keys");
    }

    // MythicDrops config settings

    public List<String> getMythicDropsBindOnPickupTiers() {
        return getMythicDropsItemTiers("BindOnPickup");
    }

    public List<String> getMythicDropsBindOnUseTiers() {
        return getMythicDropsItemTiers("BindOnUse");
    }

    public List<String> getMythicDropsBindOnEquipTiers() {
        return getMythicDropsItemTiers("BindOnEquip");
    }

    public List<String> getMythicDropsItemTiers(String bindType) {
        String[] tiersString = config.getString("Dependency_Plugins.MythicDrops." + bindType).replaceAll(" ", "").split("[,]");
        List<String> tiers = new ArrayList<String>();

        for (String tier : tiersString) {
            tiers.add(tier);
        }
        return tiers;
    }

}
