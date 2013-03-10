package com.me.tft_02.soulbound;

import java.util.ArrayList;
import java.util.List;

public class DiabloDropConfig {
    Soulbound plugin;

    public DiabloDropConfig(Soulbound instance) {
        plugin = instance;
    }

    String tiersString = plugin.getConfig().getString("DiabloDrops.BindOnPickup");

    public List<String> getBindOnPickupTiers() {
        List<String> tiers = new ArrayList<String>();
        tiers.add(tiersString.replaceAll(" ", "").split("[,]").toString());
        return tiers;
    }
}
