package com.example.spriteeditorfx;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SpriteParser {

    public static JSONObject SETTINGS_APPLICATION;

    static {
        try (InputStream inputStream = SpriteParser.class.getClassLoader().getResourceAsStream("spritesEditorConfig.json")) {
            String text;
            if (inputStream != null) {
                text = new BufferedReader(
                        new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                        .lines()
                        .collect(Collectors.joining("\n"));
            } else {
                throw new RuntimeException();
            }
            SETTINGS_APPLICATION = (JSONObject) new JSONParser().parse(text);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }


//        File resourceURL = new File(Objects.requireNonNull(SpriteParser.class.getClassLoader().getResource("spritesEditorConfig.json")).getFile());
//
//        try (FileReader fileReader = new FileReader(resourceURL)) {
//            SETTINGS_APPLICATION = (JSONObject) new JSONParser().parse(fileReader);
//        } catch (IOException | ParseException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static JSONObject INV_SPRITE_TARGET;
    public static JSONObject INV_SPRITE_SOURCE;

    static {
        for (Object language : (JSONArray) SETTINGS_APPLICATION.get("languages")) {
            JSONObject langParams = (JSONObject) language;
            if (langParams.get("name").toString().equals(SETTINGS_APPLICATION.get("target").toString())) {
                INV_SPRITE_TARGET = langParams;
                INV_SPRITE_TARGET.put("targetPage", URLEncoder.encode(((JSONObject) langParams.get("spriteModules")).get(SETTINGS_APPLICATION.get("targetPage")).toString(), StandardCharsets.UTF_8));
            }
            if (langParams.get("name").toString().equals(SETTINGS_APPLICATION.get("source").toString())) {
                INV_SPRITE_SOURCE = langParams;
                INV_SPRITE_SOURCE.put("targetPage", URLEncoder.encode(((JSONObject) langParams.get("spriteModules")).get(SETTINGS_APPLICATION.get("targetPage")).toString(), StandardCharsets.UTF_8));
            }
        }
    }

    public static List<Map<String, String>> spriteParser(String data, JSONObject spriteModuleParams) {
        List<Map<String, String>> spriteMapList = new ArrayList<>();
        String[] dataSplit = data.split("\n");
        boolean spriteFlag = false;
        for (String str : dataSplit) {
            if (str.contains(spriteModuleParams.get("idsName").toString())) {
                spriteFlag = true;
                continue;
            }
            if (spriteFlag) {
                Map<String, String> spriteMap = new HashMap<>();
                Pattern spritePattern = Pattern.compile("([^\\t\\[\\]]*?|\\['(.*?)'])\\s*?=\\s*?\\{\\s*?"
                        + spriteModuleParams.get("posName").toString().replace("[", "\\[")
                        + "\\s*?=\\s*?(\\d*?)\\s*?,\\s*?"
                        + spriteModuleParams.get("sectionName").toString().replace("[", "\\[")
                        + "\\s*?=\\s*?(\\d*?)\\s*?(,\\s*?"
                        + (spriteModuleParams.get("deprecatedName") != null ? spriteModuleParams.get("deprecatedName").toString().replace("[", "\\[") : "deprecated")
                        + "\\s*?=\\s*?(true))?\\s*?}\\s*?,");
                Matcher matcher = spritePattern.matcher(str);

                while (matcher.find()) {
                    String name = matcher.group(1).contains("[") ? matcher.group(2) :matcher.group(1);
                    String pos = matcher.group(3);
                    String section = matcher.group(4);
                    String deprecated = matcher.group(6) == null ? "false" : matcher.group(6);
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

    public static List<Map<String, String>> sectionParser(String data, JSONObject spriteModuleParams) {
        List<Map<String, String>> sectionMapList = new ArrayList<>();
        String[] dataSplit = data.split("\n");
        boolean sectionFlag = false;
        for (String str : dataSplit) {
            if (str.contains(spriteModuleParams.get("sectionsName").toString())) {
                sectionFlag = true;
                continue;
            }
            if (str.contains(spriteModuleParams.get("idsName").toString())) {
                break;
            }
            if (sectionFlag) {
                Map<String, String> map = new HashMap<>();
                Pattern sectionPattern = Pattern.compile(
                        "\\{\\s*?"
                                + spriteModuleParams.get("sectionsNameKey").toString().replace("[", "\\[")
                                + "\\s*?=\\s*?'(.*?)'\\s*?,\\s*?"
                                + spriteModuleParams.get("sectionsIDKey").toString().replace("[", "\\[")
                                + "\\s*?=\\s*?(\\d*?)\\s*?}\\s*?,"
                );
                Matcher matcher = sectionPattern.matcher(str);

                while (matcher.find()) {
                    map.put("name", matcher.group(1));
                    map.put("id", matcher.group(2));
                    sectionMapList.add(map);
                }
            }
        }
        return sectionMapList;
    }

    public static String getSettingsData(String data, JSONObject spriteModuleParams) {
        StringBuilder settings = new StringBuilder();
        String[] dataSplit = data.split("\n");
        boolean settingFlag = false;
        for (String str : dataSplit) {
            if (str.contains(spriteModuleParams.get("settingsName").toString())) {
                settingFlag = true;
                continue;
            }
            if (str.contains(spriteModuleParams.get("sectionsName").toString())) {
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

    public static String getDataSpriteFile(URL url) {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
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