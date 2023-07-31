package com.example.spriteeditorfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpriteEditorController {
    @FXML
    private TableView<DataModel> tableView;
    @FXML
    private TableColumn<DataModel, Integer> idColumn;
    @FXML
    private TableColumn<DataModel, String> russianNameColumn;
    @FXML
    private TableColumn<DataModel, String> englishNameColumn;
    @FXML
    private TableColumn<DataModel, Integer> position;
    @FXML
    private TableColumn<DataModel, Integer> section;
    @FXML
    private Button saveButton;
    @FXML
    public ToggleButton sectionEditorButton;

    private ObservableList<DataModel> sectionsTableData = getSectionData();
    private ObservableList<DataModel> spritesTableData = getSpriteData();

    public void initialize() {
        // Настройка колонок
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        position.setCellValueFactory(new PropertyValueFactory<>("position"));
        section.setCellValueFactory(new PropertyValueFactory<>("section"));
        russianNameColumn.setCellValueFactory(new PropertyValueFactory<>("russianName"));
        englishNameColumn.setCellValueFactory(new PropertyValueFactory<>("englishName"));

        russianNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        russianNameColumn.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setRussianName(e.getNewValue()));

        saveButton.setOnAction(actionEvent -> {
            if (sectionEditorButton.isSelected()) {
                sectionsTableData = tableView.getItems();
            } else {
                spritesTableData = tableView.getItems();
            }
            saveData(sectionsTableData, spritesTableData);
        });
        sectionEditorButton.setOnAction(actionEvent -> {
            if (sectionEditorButton.isSelected()) {
                spritesTableData = tableView.getItems();
                idColumn.setVisible(true);
                position.setVisible(false);
                section.setVisible(false);
                tableView.setItems(sectionsTableData != null ? sectionsTableData : getSectionData());
            } else {
                sectionsTableData = tableView.getItems();
                idColumn.setVisible(false);
                position.setVisible(true);
                section.setVisible(true);
                tableView.setItems(spritesTableData != null ? spritesTableData : getSpriteData());
            }
        });
        tableView.setEditable(true);
        tableView.setItems(getSpriteData());
    }

    private ObservableList<DataModel> getSectionData() {
        ObservableList<DataModel> data = FXCollections.observableArrayList();

        List<Map<String, String>> sectionsEN = SpriteEditorApplication.getSectionsEN();
        List<Map<String, String>> sectionsRU = SpriteEditorApplication.getSectionsRU();

        for (Map<String, String> sectionEN : sectionsEN) {
            DataModel dataModel = new DataModel(Integer.parseInt(sectionEN.get("id")), "", sectionEN.get("name"));
            for (Map<String, String> sectionRU : sectionsRU) {
                if (String.valueOf(dataModel.getId()).equals(sectionRU.get("id"))) {
                    dataModel.setRussianName(sectionRU.get("name"));
                }
            }
            data.add(dataModel);
        }

        return data;
    }

    private ObservableList<DataModel> getSpriteData() {
        ObservableList<DataModel> data = FXCollections.observableArrayList();

        List<Map<String, String>> spritesEN = SpriteEditorApplication.getSpritesEN();
        List<Map<String, String>> spritesRU = SpriteEditorApplication.getSpritesRU();

        ArrayList<String> positionsEN = new ArrayList<>();

        for (Map<String, String> spriteEN : spritesEN) {
            boolean isExistEN = false;
            DataModel dataModel = new DataModel();
            for (String position : positionsEN) {
                if (isExistEN) {
                    break;
                }
                if (spriteEN.get("pos").equals(position)) {
                    for (DataModel dm : data) {
                        if (String.valueOf(dm.getPosition()).equals(position)) {
                            dataModel = dm;
                            isExistEN = true;
                            break;
                        }
                    }
                }
            }
            if (isExistEN) {
                dataModel.setEnglishName(dataModel.getEnglishName() + "|" + (spriteEN.get("deprecated").equals("true") ? "!" : "") + spriteEN.get("name"));
            } else {
                positionsEN.add(spriteEN.get("pos"));
                dataModel = new DataModel(Integer.parseInt(spriteEN.get("pos")), "", spriteEN.get("name"), Integer.parseInt(spriteEN.get("section")));
                data.add(dataModel);
            }
        }
        for (DataModel dm : data) {
            for (Map<String, String> spriteRU : spritesRU) {
                if (String.valueOf(dm.getPosition()).equals(spriteRU.get("pos"))) {
                    if (!dm.getRussianName().equals(""))
                        dm.setRussianName(dm.getRussianName() + "|" + (spriteRU.get("deprecated").equals("true") ? "!" : "") + spriteRU.get("name"));
                    else
                        dm.setRussianName((spriteRU.get("deprecated").equals("true") ? "!" : "") + spriteRU.get("name"));
                }
            }
        }
        return data;
    }

    private void saveData(List<DataModel> sectionTableData, List<DataModel> spritesTableData) {
        SpriteFormer.generateSpriteList(sectionTableData, spritesTableData);
    }
}