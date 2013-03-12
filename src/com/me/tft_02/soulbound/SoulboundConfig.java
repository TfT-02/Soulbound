package com.me.tft_02.soulbound;

import java.util.ArrayList;
import java.util.List;

public class SoulboundConfig {
    Soulbound plugin;

    public SoulboundConfig(Soulbound instance) {
        plugin = instance;
    }

    public boolean getEBRBindOnPickup() {
        return plugin.getConfig().getBoolean("Dependency_Plugins.EpicBossRecoded.BindOnPickup");
    }

    public boolean getEBRBindOnEquip() {
        return plugin.getConfig().getBoolean("Dependency_Plugins.EpicBossRecoded.BindOnEquip");
    }

    public boolean getEBRBindOnUse() {
        return plugin.getConfig().getBoolean("Dependency_Plugins.EpicBossRecoded.BindOnUse");
    }

    public List<String> getBindOnPickupTiers() {
        return getItemTiers("BindOnPickup");
    }

    public List<String> getBindOnUseTiers() {
        return getItemTiers("BindOnUse");
    }

    public List<String> getBindOnEquipTiers() {
        return getItemTiers("BindOnEquip");
    }

    public List<String> getItemTiers(String bindType) {
        String[] tiersString = plugin.getConfig().getString("Dependency_Plugins.DiabloDrops." + bindType).replaceAll(" ", "").split("[,]");
        List<String> tiers = new ArrayList<String>();

        for (String tier : tiersString) {
            tiers.add(tier);
        }
        return tiers;
    }

    public boolean getLoreLocksBindKeys() {
        return plugin.getConfig().getBoolean("Dependency_Plugins.LoreLocks.Bind_Keys");
    }
}
