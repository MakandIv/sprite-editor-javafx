package com.example.spriteeditorfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.spriteeditorfx.SpriteParser.*;

public class SpriteEditorApplication extends Application {

    private static String ruData;
    private static String enData;
    private static List<Map<String, String>> sectionsRU;
    private static List<Map<String, String>> sectionsEN;
    private static List<Map<String, String>> spritesRU;
    private static List<Map<String, String>> spritesEN;
    private static String settingsRU;

    public static String getSettingsRU() {
        return settingsRU;
    }

    public static void setSettingsRU(String settingsRU) {
        SpriteEditorApplication.settingsRU = settingsRU;
    }

    public static String getRuData() {
        return ruData;
    }

    public static void setRuData(String ruData) {
        SpriteEditorApplication.ruData = ruData;
    }

    public static String getEnData() {
        return enData;
    }

    public static void setEnData(String enData) {
        SpriteEditorApplication.enData = enData;
    }

    public static List<Map<String, String>> getSectionsRU() {
        return sectionsRU;
    }

    public static void setSectionsRU(List<Map<String, String>> sectionsRU) {
        SpriteEditorApplication.sectionsRU = sectionsRU;
    }

    public static List<Map<String, String>> getSectionsEN() {
        return sectionsEN;
    }

    public static void setSectionsEN(List<Map<String, String>> sectionsEN) {
        SpriteEditorApplication.sectionsEN = sectionsEN;
    }

    public static List<Map<String, String>> getSpritesRU() {
        return spritesRU;
    }

    public static void setSpritesRU(List<Map<String, String>> spritesRU) {
        SpriteEditorApplication.spritesRU = spritesRU;
    }

    public static List<Map<String, String>> getSpritesEN() {
        return spritesEN;
    }

    public static void setSpritesEN(List<Map<String, String>> spritesEN) {
        SpriteEditorApplication.spritesEN = spritesEN;
    }


    @Override
    public void init() throws Exception {
        setRuData(getDataInvSprite(INV_SPRITE_RU.get("lang"), INV_SPRITE_RU.get("pageName")));
        setEnData(getDataInvSprite(INV_SPRITE_EN.get("lang"), INV_SPRITE_EN.get("pageName")));
        setSettingsRU(getSettingsData(Objects.requireNonNull(getRuData())));
        setSectionsRU(sectionParser(Objects.requireNonNull(getRuData())));
        setSectionsEN(sectionParser(Objects.requireNonNull(getEnData())));
        setSpritesRU(spriteParser(getRuData()));
        setSpritesEN(spriteParser(getEnData()));

        super.init();
    }

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(SpriteEditorApplication.class.getResource("sprite-editor-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Sprite Editor");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.sizeToScene();
        stage.setMinHeight(300);
        stage.setMinWidth(300);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}