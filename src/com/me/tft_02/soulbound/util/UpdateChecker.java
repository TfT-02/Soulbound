package com.me.tft_02.soulbound.util;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.me.tft_02.soulbound.Soulbound;

public class UpdateChecker {
    static Soulbound plugin;

    private UpdateChecker() {}

    public static boolean updateAvailable() throws Exception {
        String version = Soulbound.getInstance().getDescription().getVersion();
        URL url = new URL("http://api.bukget.org/api2/bukkit/plugin/soulbound/latest");
        InputStreamReader isr;
        try {
            isr = new InputStreamReader(url.openStream());
        }
        catch (UnknownHostException e) {
            return false;
        }

        String newVersion;
        try {
            JSONParser jp = new JSONParser();
            Object o = jp.parse(isr);

            if (!(o instanceof JSONObject)) {
                isr.close();
                return false;
            }

            JSONObject jo = (JSONObject) o;
            jo = (JSONObject) jo.get("versions");
            newVersion = (String) jo.get("version");

            String[] oldTokens = version.replaceAll("(?i)(-)(.+?)(-)", "-").split("[.]");
            String[] newTokens = newVersion.replaceAll("(?i)(-)(.+?)(-)", "-").split("[.]");

            for (int i = 0; i < 3; i++) {
                Integer newVer = Integer.parseInt(newTokens[i]);
                Integer oldVer;
                try {
                    oldVer = Integer.parseInt(oldTokens[i]);
                }
                catch (NumberFormatException e) {
                    oldVer = 0;
                }
                if (oldVer < newVer) {
                    isr.close();
                    return true;
                }
            }
            return false;
        }
        catch (ParseException e) {
            isr.close();
            return false;
        }
    }
}
