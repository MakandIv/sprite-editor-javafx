package com.example.spriteeditorfx.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DataModel {

    private Sprite spriteImage;
    private IntegerProperty id;
    private StringProperty targetName;
    private StringProperty sourceName;
    private IntegerProperty section;
    private IntegerProperty position;

    public DataModel(Integer id, String targetName, String sourceName) {
        this.id = new SimpleIntegerProperty(id);
        this.targetName = new SimpleStringProperty(targetName);
        this.sourceName = new SimpleStringProperty(sourceName);
    }

    public DataModel(Integer position, String targetName, String sourceName, Integer section) {
        this.position = new SimpleIntegerProperty(position);
        this.targetName = new SimpleStringProperty(targetName);
        this.sourceName = new SimpleStringProperty(sourceName);
        this.section = new SimpleIntegerProperty(section);
    }

    public DataModel() {

    }

    public Sprite getSpriteImage() {
        return spriteImage;
    }

    public void setSpriteImage(Sprite spriteImage) {
        this.spriteImage = spriteImage;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getTargetName() {
        return targetName.get();
    }

    public void setTargetName(String targetName) {
        this.targetName.set(targetName);
    }

    public String getSourceName() {
        return sourceName.get();
    }

    public void setSourceName(String sourceName) {
        this.sourceName.set(sourceName);
    }

    public int getSection() {
        return section.get();
    }

    public void setSection(int section) {
        this.section.set(section);
    }

    public int getPosition() {
        return position.get();
    }

    public void setPosition(int position) {
        this.position.set(position);
    }
}