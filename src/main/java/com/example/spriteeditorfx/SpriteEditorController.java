package com.example.spriteeditorfx;

import com.example.spriteeditorfx.model.DataModel;
import com.example.spriteeditorfx.model.Sprite;
import com.example.spriteeditorfx.model.TextAreaTableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
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
    public ToggleButton sectionEditorButton;

    private ObservableList<DataModel> sectionsTableData = getSectionData();
    private ObservableList<DataModel> spritesTableData = getSpriteData();

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
                spritesTableData = tableView.getItems();
            }
            saveData(sectionsTableData, spritesTableData);
        });
        sectionEditorButton.setOnAction(actionEvent -> {
            if (sectionEditorButton.isSelected()) {
                spritesTableData = tableView.getItems();
                spriteImage.setVisible(false);
                idColumn.setVisible(true);
                position.setVisible(false);
                section.setVisible(false);
                tableView.setItems(sectionsTableData != null ? sectionsTableData : getSectionData());
            } else {
                sectionsTableData = tableView.getItems();
                spriteImage.setVisible(true);
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
}