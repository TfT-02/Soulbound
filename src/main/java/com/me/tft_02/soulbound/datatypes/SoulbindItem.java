package com.me.tft_02.soulbound.datatypes;

import java.util.List;

import org.bukkit.material.MaterialData;

public class SoulbindItem {
    private MaterialData materialData;
    private String name;
    private List<String> lore;

    public SoulbindItem(MaterialData materialData, String name, List<String> lore) {
        this.materialData = materialData;
        this.name = name;
        this.lore = lore;
    }

    public MaterialData getMaterialData() {
        return materialData;
    }

    public List<String> getLore() {
        return lore;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return materialData.toString() + " " + name.toString() + " " + lore.toString();
    }
}
