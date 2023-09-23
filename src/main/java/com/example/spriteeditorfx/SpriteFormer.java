package com.example.spriteeditorfx;

import com.example.spriteeditorfx.model.DataModel;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static com.example.spriteeditorfx.SpriteEditorApplication.getSettingsTarget;
import static com.example.spriteeditorfx.SpriteParser.INV_SPRITE_TARGET;

public class SpriteFormer {

    public static void generateSpriteList(List<DataModel> sectionsTableData, List<DataModel> spritesTableData, File file) {
        try (FileWriter fileWriter = new FileWriter(file)) {
            JSONObject spriteModuleParams = (JSONObject) INV_SPRITE_TARGET.get("spriteModuleParams");
            fileWriter.write("");

            fileWriter.append("""
                            return {
                            \t""")
                    .append(spriteModuleParams.get("settingsName").toString())
                    .append("""
                             = {
                            """)
                    .append(getSettingsTarget()).append("""
                            \t},
                            \t""")
                    .append(spriteModuleParams.get("sectionsName").toString())
                    .append(" = {");
            for (DataModel section : sectionsTableData) {
                if (section.getTargetName().equals("")) {
                    continue;
                }
                int i = 1;
                if (section.getTargetName().contains("'")){
                    i = 2;
                }
                fileWriter.append("\n\t\t{ ")
                        .append(spriteModuleParams.get("sectionsNameKey").toString())
                        .append(" = ")
                        .append((i == 1) ? "'" : "\"")
                        .append(section.getTargetName())
                        .append((i == 1) ? "'" : "\"")
                        .append(", ")
                        .append(spriteModuleParams.get("sectionsIDKey").toString())
                        .append(" = ")
                        .append(String.valueOf(section.getId()))
                        .append(" },");
            }
            fileWriter.append("""
                    
                    \t},
                    \t""").append(spriteModuleParams.get("idsName").toString()).append(" = {");
            for (DataModel sprite : spritesTableData) {
                if (sprite.getTargetName().equals("")) {
                    continue;
                }
                for (String name : sprite.getTargetName().split("\n")) {
                    int i = 1;
                    if (name.matches("^[a-zA-Z]*$")) {
                        i = 0;
                    } else if (name.contains("'")) {
                        i = 2;
                    }
                    fileWriter.append("\n\t\t")
                            .append((i > 0) ? "[" : "")
                            .append((i == 1) ? "'" : (i == 2) ? "\"" : "")
                            .append(name.replace("!", ""))
                            .append((i == 1) ? "'" : (i == 2) ? "\"" : "")
                            .append((i > 0) ? "]" : "")
                            .append(" = { ")
                            .append(spriteModuleParams.get("posName").toString())
                            .append(" = ")
                            .append(String.valueOf(sprite.getPosition()))
                            .append(", ")
                            .append(spriteModuleParams.get("sectionName").toString())
                            .append(" = ")
                            .append(String.valueOf(sprite.getSection()));
                    if (name.contains("!")) {
                        fileWriter.append(", ")
                                .append(spriteModuleParams.get("deprecatedName").toString())
                                .append(" = true");
                    }
                    fileWriter.append(" },");
                }
            }
            fileWriter.append("\n\t},\n}");


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
