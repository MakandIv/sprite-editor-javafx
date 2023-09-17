package com.example.spriteeditorfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.example.spriteeditorfx.SpriteParser.*;

public class SpriteEditorApplication extends Application {

    private static String targetData;
    private static String sourceData;
    private static List<Map<String, String>> sectionsTarget;
    private static List<Map<String, String>> sectionsSource;
    private static List<Map<String, String>> spritesTarget;
    private static List<Map<String, String>> spritesSource;
    private static String settingsTarget;
    private static Image spriteImageSource;

    public static String getSettingsTarget() {
        return settingsTarget;
    }

    public static void setSettingsTarget(String settingsRU) {
        SpriteEditorApplication.settingsTarget = settingsRU;
    }

    public static String getTargetData() {
        return targetData;
    }

    public static void setTargetData(String ruData) {
        SpriteEditorApplication.targetData = ruData;
    }

    public static String getSourceData() {
        return sourceData;
    }

    public static void setSourceData(String enData) {
        SpriteEditorApplication.sourceData = enData;
    }

    public static List<Map<String, String>> getSectionsTarget() {
        return sectionsTarget;
    }

    public static void setSectionsTarget(List<Map<String, String>> sectionsRU) {
        SpriteEditorApplication.sectionsTarget = sectionsRU;
    }

    public static List<Map<String, String>> getSectionsSource() {
        return sectionsSource;
    }

    public static void setSectionsSource(List<Map<String, String>> sectionsEN) {
        SpriteEditorApplication.sectionsSource = sectionsEN;
    }

    public static List<Map<String, String>> getSpritesTarget() {
        return spritesTarget;
    }

    public static void setSpritesTarget(List<Map<String, String>> spritesRU) {
        SpriteEditorApplication.spritesTarget = spritesRU;
    }

    public static List<Map<String, String>> getSpritesSource() {
        return spritesSource;
    }

    public static void setSpritesSource(List<Map<String, String>> spritesEN) {
        SpriteEditorApplication.spritesSource = spritesEN;
    }

    public static Image getSpriteImageSource() {
        return spriteImageSource;
    }

    public static void setSpriteImageSource(Image spriteImageSource) {
        SpriteEditorApplication.spriteImageSource = spriteImageSource;
    }

    @Override
    public void init() throws Exception {

        setTargetData(getDataSpriteFile(
                new URL(INV_SPRITE_TARGET.get("baseURL").toString()
                        + INV_SPRITE_TARGET.get("name").toString() + "/"
                        + INV_SPRITE_TARGET.get("suffix").toString()
                        + INV_SPRITE_TARGET.get("targetPage").toString()
                        + "?action=raw")));

        setSourceData(getDataSpriteFile(
                new URL(INV_SPRITE_SOURCE.get("baseURL").toString()
                        + INV_SPRITE_SOURCE.get("name").toString() + "/"
                        + INV_SPRITE_SOURCE.get("suffix").toString()
                        + INV_SPRITE_SOURCE.get("targetPage").toString()
                        + "?action=raw")));
        setSettingsTarget(getSettingsData(Objects.requireNonNull(getTargetData()), (JSONObject) INV_SPRITE_TARGET.get("spriteModuleParams")));
        setSectionsTarget(sectionParser(Objects.requireNonNull(getTargetData()), (JSONObject) INV_SPRITE_TARGET.get("spriteModuleParams")));
        setSectionsSource(sectionParser(Objects.requireNonNull(getSourceData()), (JSONObject) INV_SPRITE_SOURCE.get("spriteModuleParams")));
        setSpritesTarget(spriteParser(getTargetData(), (JSONObject) INV_SPRITE_TARGET.get("spriteModuleParams")));
        setSpritesSource(spriteParser(getSourceData(), (JSONObject) INV_SPRITE_SOURCE.get("spriteModuleParams")));
        setSpriteImageSource(getSpriteFile(SETTINGS_APPLICATION.get("sourceFile").toString()));
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