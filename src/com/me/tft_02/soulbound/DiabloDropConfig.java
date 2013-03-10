package com.me.tft_02.soulbound;

import java.util.ArrayList;
import java.util.List;

public class DiabloDropConfig {
    Soulbound plugin;

    public DiabloDropConfig(Soulbound instance) {
        plugin = instance;
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
        String[] tiersString = plugin.getConfig().getString("DiabloDrops." + bindType).replaceAll(" ", "").split("[,]");
        List<String> tiers = new ArrayList<String>();

        for (String tier : tiersString) {
            tiers.add(tier);
        }
        return tiers;
    }
}
