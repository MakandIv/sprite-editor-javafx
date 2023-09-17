package com.example.spriteeditorfx;

import com.example.spriteeditorfx.model.DataModel;
import com.example.spriteeditorfx.model.Sprite;
import com.example.spriteeditorfx.model.TextAreaTableCell;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.example.spriteeditorfx.SpriteParser.SETTINGS_APPLICATION;

public class SpriteEditorController {
    @FXML
    private TableView<DataModel> tableView;
    @FXML
    public TableColumn<DataModel, Sprite> spriteImage;
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
    private ToggleButton sectionEditorButton;
    @FXML
    private TextField search;

    private ObservableList<DataModel> sectionsTableData = getSectionData();
    private ObservableList<DataModel> spritesTableData = getSpriteData();

    private boolean isFiltered = false;
    private String filter = "";

    public void initialize() {
        // Настройка колонок
        spriteImage.setCellValueFactory(new PropertyValueFactory<>("spriteImage"));
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        position.setCellValueFactory(new PropertyValueFactory<>("position"));
        section.setCellValueFactory(new PropertyValueFactory<>("section"));
        russianNameColumn.setCellValueFactory(new PropertyValueFactory<>("russianName"));
        englishNameColumn.setCellValueFactory(new PropertyValueFactory<>("englishName"));

        russianNameColumn.setCellFactory(TextAreaTableCell.forTableColumn());
        russianNameColumn.setOnEditCommit(e -> e.getTableView().getItems().get(e.getTablePosition().getRow()).setRussianName(e.getNewValue()));

        spriteImage.setStyle("-fx-alignment: CENTER;");
        idColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        position.setStyle("-fx-alignment: CENTER-LEFT;");
        section.setStyle("-fx-alignment: CENTER-LEFT;");
        russianNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
        englishNameColumn.setStyle("-fx-alignment: CENTER-LEFT;");

        saveButton.setOnAction(actionEvent -> {
            if (sectionEditorButton.isSelected()) {
                sectionsTableData = tableView.getItems();
            } else {
                tempSavingSpriteTableData();
            }
            saveData(sectionsTableData, spritesTableData);
        });
        sectionEditorButton.setOnAction(actionEvent -> {
            if (sectionEditorButton.isSelected()) {

                tempSavingSpriteTableData();

                spriteImage.setVisible(false);
                idColumn.setVisible(true);
                position.setVisible(false);
                section.setVisible(false);

                search.textProperty().removeListener(this::searchChangeListener);
                search.setText("");
                search.setDisable(true);

                tableView.setItems(sectionsTableData != null ? sectionsTableData : getSectionData());
            } else {
                sectionsTableData = tableView.getItems();
                spriteImage.setVisible(true);
                idColumn.setVisible(false);
                position.setVisible(true);
                section.setVisible(true);

                search.textProperty().addListener(this::searchChangeListener);
                search.setDisable(false);

                tableView.setItems(getSpriteData());
            }
        });

        search.textProperty().addListener(this::searchChangeListener);

        tableView.setItems(getSpriteData());

    }

    private void tempSavingSpriteTableData() {
        if (isFiltered) {
            spritesTableData = FXCollections.observableArrayList(spritesTableData.stream().filter(dataModel -> !dataModel.getRussianName().toLowerCase().contains(filter.toLowerCase()) && !dataModel.getEnglishName().toLowerCase().contains(filter.toLowerCase())).toList());
            spritesTableData.addAll(tableView.getItems());
            spritesTableData = FXCollections.observableArrayList(spritesTableData.stream().sorted(Comparator.comparing(DataModel::getRussianName)).toList());
        } else {
            spritesTableData = FXCollections.observableArrayList(tableView.getItems().stream().sorted(Comparator.comparing(DataModel::getRussianName)).toList());
        }
    }

    private ObservableList<DataModel> getSectionData() {
        ObservableList<DataModel> data = FXCollections.observableArrayList();

        List<Map<String, String>> sectionsEN = SpriteEditorApplication.getSectionsSource();
        List<Map<String, String>> sectionsRU = SpriteEditorApplication.getSectionsTarget();

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

        List<Map<String, String>> spritesEN = SpriteEditorApplication.getSpritesSource();
        List<Map<String, String>> spritesRU = SpriteEditorApplication.getSpritesTarget();

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
                dataModel.setEnglishName(dataModel.getEnglishName() + "\n" + (spriteEN.get("deprecated").equals("true") ? "!" : "") + spriteEN.get("name"));
            } else {
                positionsEN.add(spriteEN.get("pos"));
                dataModel = new DataModel(Integer.parseInt(spriteEN.get("pos")), "", spriteEN.get("name"), Integer.parseInt(spriteEN.get("section")));
                dataModel.setSpriteImage(new Sprite(SpriteEditorApplication.getSpriteImageSource(), dataModel.getPosition()));
                data.add(dataModel);
            }
        }
        for (DataModel dm : data) {
            for (Map<String, String> spriteRU : spritesRU) {
                if (String.valueOf(dm.getPosition()).equals(spriteRU.get("pos"))) {
                    if (!dm.getRussianName().equals(""))
                        dm.setRussianName(dm.getRussianName() + "\n" + (spriteRU.get("deprecated").equals("true") ? "!" : "") + spriteRU.get("name"));
                    else
                        dm.setRussianName((spriteRU.get("deprecated").equals("true") ? "!" : "") + spriteRU.get("name"));
                }
            }
        }
        return data;
    }

    private void saveData(List<DataModel> sectionTableData, List<DataModel> spritesTableData) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(SETTINGS_APPLICATION.get("targetPage").toString());
        fileChooser.setTitle("Save File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt"));
        File file = fileChooser.showSaveDialog(tableView.getScene().getWindow());
        fileChooser.setInitialDirectory(file.getParentFile());

        SpriteFormer.generateSpriteList(sectionTableData, spritesTableData, file);
    }

    private void searchChangeListener(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        isFiltered = !newValue.equals("");
        filter = newValue;
        if (spritesTableData != null) {
            tableView.setItems(FXCollections.observableArrayList(spritesTableData.stream().filter(dataModel -> dataModel.getRussianName().toLowerCase().contains(newValue.toLowerCase()) || dataModel.getEnglishName().toLowerCase().contains(newValue.toLowerCase())).toList()));
        } else {
            tableView.setItems(FXCollections.observableArrayList(SpriteEditorController.this.getSpriteData().stream().filter(dataModel -> dataModel.getRussianName().toLowerCase().contains(newValue.toLowerCase()) || dataModel.getEnglishName().toLowerCase().contains(newValue.toLowerCase())).toList()));
        }
    }
}