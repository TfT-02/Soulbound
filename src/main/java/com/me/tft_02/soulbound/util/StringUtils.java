package com.me.tft_02.soulbound.util;

import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;

public class StringUtils {

    public static int getIndexOfSoulbound(List<String> itemLore) {
        int index = -1;

        for (String line : itemLore) {
            if (line.contains(Misc.SOULBOUND_TAG) && !line.equals(Misc.SOULBOUND_TAG)) {
                index = itemLore.indexOf(line);
            }
        }

        return index;
    }

    public static UUID readUUIDFromLore(List<String> itemLore) {
        int index = getIndexOfSoulbound(itemLore);

        if (index == -1) {
            return null;
        }

        String line = itemLore.get(index);
        line = line.substring(11);

        return readUUID(line);
    }

    public static String hideUUID(UUID uuid) {
        String string = uuid.toString();
        string = string.replaceAll("-", ChatColor.MAGIC.toString());
        StringBuilder formattedString = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            formattedString.append(ChatColor.COLOR_CHAR).append(string.charAt(i));
        }

        return formattedString.toString();
    }

    public static UUID readUUID(String string) {
        return UUID.fromString(string.replaceAll(ChatColor.MAGIC.toString(), "-").replaceAll("§", ""));
    }
}
