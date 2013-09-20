package com.me.tft_02.soulbound.util;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;

import com.me.tft_02.soulbound.Soulbound;
import com.me.tft_02.soulbound.config.Config;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class UpdateChecker {
    private UpdateChecker() {}

    public static boolean updateAvailable() throws Exception {
        String checkType = Config.getInstance().getPreferBeta() ? "latest" : "release";
        String version = Soulbound.p.getDescription().getVersion();
        InputStreamReader isr;

        try {
            isr = new InputStreamReader(new URL("http://api.bukget.org/api2/bukkit/plugin/soulbound/" + checkType).openStream());
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

            String[] oldTokens = version.replaceAll("(?i)(-)(.+?)(-)", "-").split("[.]|-b");
            String[] newTokens = newVersion.replaceAll("(?i)(-)(.+?)(-)", "-").split("[.]|-b");

            for (int i = 0; i < 4; i++) {
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
