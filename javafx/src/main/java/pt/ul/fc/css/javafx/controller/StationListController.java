package pt.ul.fc.css.javafx.controller;

import javafx.collections.FXCollections;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pt.ul.fc.css.javafx.api.ApiClient;
import pt.ul.fc.css.javafx.dto.StationDTO;
import pt.ul.fc.css.javafx.model.DataModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;

public class StationListController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;

    @FXML private TableView<StationDTO> stationTable;
    @FXML private TableColumn<StationDTO, Long> colId;
    @FXML private TableColumn<StationDTO, String> colName;
    @FXML private TableColumn<StationDTO, Integer> colDocks;

    @Override
    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;


        colId.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().id()));

        colName.setCellValueFactory(cellData -> 
            new SimpleStringProperty(cellData.getValue().name()));

        colDocks.setCellValueFactory(cellData -> 
            new SimpleObjectProperty<>(cellData.getValue().maxDocks()));


        loadStations();

        stationTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && stationTable.getSelectionModel().getSelectedItem() != null) {
                openDetails(stationTable.getSelectionModel().getSelectedItem());
            }
        });
    }

    private void loadStations() {
        try {
            stationTable.setItems(FXCollections.observableArrayList(ApiClient.getAllStations()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openDetails(StationDTO station) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/pt/ul/fc/css/javafx/view/stationdetails.fxml"));
            Parent root = loader.load();

            StationDetailsController detailsController = loader.getController();
            detailsController.initModel(stage, model);
            
            detailsController.loadStationData(station.id());

            stage.getScene().setRoot(root); 
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void backToMenu(ActionEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/css/javafx/view/init.fxml", "UrbanWheels - Menu", model);
    }

    @Override public Stage getStage() { return stage; }
    @Override public void setStage(Stage stage) { this.stage = stage; }
}