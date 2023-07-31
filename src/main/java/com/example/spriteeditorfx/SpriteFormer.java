package com.example.spriteeditorfx;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static com.example.spriteeditorfx.SpriteEditorApplication.getSettingsRU;

public class SpriteFormer {

    public static void generateSpriteList(List<DataModel> sectionsTableData, List<DataModel> spritesTableData) {
        File file = new File("ИнвСпрайт.txt");
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write("");
            fileWriter.append("""
                    return {
                    \t['настройки'] = {
                    """).append(getSettingsRU()).append("""
                    \t},
                    \t['разделы'] = {""");
            for (DataModel section : sectionsTableData) {
                if (section.getRussianName().equals("")) {
                    continue;
                }
                fileWriter.append("\n\t\t{ ['назв'] = '").append(section.getRussianName()).append("', ID = ").append(String.valueOf(section.getId())).append(" },");
            }
            fileWriter.append("""
                    
                    \t},
                    \t['IDы'] = {""");
            for (DataModel sprite : spritesTableData) {
                if (sprite.getRussianName().equals("")) {
                    continue;
                }
                for (String name : sprite.getRussianName().split("\\|")) {
                    fileWriter.append("\n\t\t['").append(name.replace("!", "")).append("'] = { ['поз'] = ").append(String.valueOf(sprite.getPosition())).append(", ['раздел'] = ").append(String.valueOf(sprite.getSection()));
                    if (name.contains("!")) {
                        fileWriter.append(", ['устарел'] = true");
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
