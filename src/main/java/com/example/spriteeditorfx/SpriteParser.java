package com.example.spriteeditorfx;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpriteParser {
    public static final Map<String, String> INV_SPRITE_RU = new HashMap<>();
    public static final Map<String, String> INV_SPRITE_EN = new HashMap<>();
    static {
        INV_SPRITE_RU.put("lang", "ru");
        INV_SPRITE_RU.put("pageName", URLEncoder.encode("Модуль:ИнвСпрайт", StandardCharsets.UTF_8));
        INV_SPRITE_EN.put("lang", "en");
        INV_SPRITE_EN.put("pageName", "Module:InvSprite");
    }
    public static List<Map<String, String>> spriteParser(String data) {
        List<Map<String, String>> spriteMapList = new ArrayList<>();
        String[] dataSplit = data.split("\n");
        boolean spriteFlag = false;
        for (String str : dataSplit) {
            if (str.contains("['IDы']") || str.contains("ids")) {
                spriteFlag = true;
                continue;
            }
            if (spriteFlag) {
                Map<String, String> spriteMap = new HashMap<>();
                Pattern spritePattern = Pattern.compile("([^\\t\\[\\]]*?|\\['(.*?)'])\\s*?=\\s*?\\{\\s*?(\\['поз']|pos)\\s*?=\\s*?(\\d*?)\\s*?,\\s*?(section|\\['раздел'])\\s*?=\\s*?(\\d*?)\\s*?(,\\s*?(deprecated|\\['устарел'])\\s*?=\\s*?(true))?\\s*?}\\s*?,");
                Matcher matcher = spritePattern.matcher(str);

                while (matcher.find()) {
                    String name = (matcher.group(1).contains("[")) ? matcher.group(2) : matcher.group(1);
                    String pos = matcher.group(4);
                    String section = matcher.group(6);
                    String deprecated = matcher.group(9) == null ? "false" : matcher.group(9);
                    spriteMap.put("name", name);
                    spriteMap.put("pos", pos);
                    spriteMap.put("section", section);
                    spriteMap.put("deprecated", deprecated);
                    spriteMapList.add(spriteMap);
                }
            }
        }
        return spriteMapList;
    }

    public static List<Map<String, String>> sectionParser(String data) {
        List<Map<String, String>> sectionMapList = new ArrayList<>();
        String[] dataSplit = data.split("\n");
        boolean sectionFlag = false;
        for (String str : dataSplit) {
            if (str.contains("['разделы']") || str.contains("sections")) {
                sectionFlag = true;
                continue;
            }
            if (str.contains("['IDы']") || str.contains("ids")) {
                break;
            }
            if (sectionFlag) {
                Map<String, String> map = new HashMap<>();
                Pattern sectionPattern = Pattern.compile("\\{\\s*?(\\['назв']|name)\\s*?=\\s*?'(.*?)'\\s*?,\\s*?(ID|id)\\s*?=\\s*?(\\d*?)\\s*?}\\s*?,");
                Matcher matcher = sectionPattern.matcher(str);

                while (matcher.find()) {
                    String name = matcher.group(2);
                    String id = matcher.group(4);
                    map.put("name", name);
                    map.put("id", id);
                    sectionMapList.add(map);
                }
            }
        }
        return sectionMapList;
    }

    public static String getSettingsData(String data) {
        StringBuilder settings = new StringBuilder();
        String[] dataSplit = data.split("\n");
        boolean settingFlag = false;
        for (String str : dataSplit) {
            if (str.contains("['настройки']") || str.contains("settings")) {
                settingFlag = true;
                continue;
            }
            if (str.contains("['разделы']") || str.contains("section")) {
                break;
            }
            if (settingFlag) {
                if (!str.contains("=")) {
                    break;
                }
                settings.append(str).append("\n");
            }
        }
        return settings.toString();
    }

    public static String getDataInvSprite(String lang, String pageName) {
        String baseURL = "https://minecraft.fandom.com/";
        String langURL = lang.equals("en") ? "" : lang + "/";
        String wikiURL = "wiki/";
        String action = "?action=raw";

        try {
            HttpsURLConnection connection = (HttpsURLConnection) new URL(baseURL + langURL + wikiURL + pageName + action).openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine).append("\n");
                }
                in.close();
                return response.toString();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }
}