package com.example.spriteeditorfx.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DataModel {

    private Sprite spriteImage;
    private IntegerProperty id;
    private StringProperty russianName;
    private StringProperty englishName;
    private IntegerProperty section;
    private IntegerProperty position;

    public DataModel(Integer id, String russianName, String englishName) {
        this.id = new SimpleIntegerProperty(id);
        this.russianName = new SimpleStringProperty(russianName);
        this.englishName = new SimpleStringProperty(englishName);
    }

    public DataModel(Integer position, String russianName, String englishName, Integer section) {
        this.position = new SimpleIntegerProperty(position);
        this.russianName = new SimpleStringProperty(russianName);
        this.englishName = new SimpleStringProperty(englishName);
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

    public String getRussianName() {
        return russianName.get();
    }

    public void setRussianName(String russianName) {
        this.russianName.set(russianName);
    }

    public String getEnglishName() {
        return englishName.get();
    }

    public void setEnglishName(String englishName) {
        this.englishName.set(englishName);
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