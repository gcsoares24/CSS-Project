package pt.ul.fc.css.javafx.controller;

import javafx.beans.property.SimpleStringProperty;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import pt.ul.fc.css.javafx.api.ApiClient;
import pt.ul.fc.css.javafx.dto.BikeDTO;
import pt.ul.fc.css.javafx.dto.StationDTO;
import pt.ul.fc.css.javafx.model.DataModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class StationDetailsController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;

    @FXML private Label lblName;
    @FXML private Label lblCoords;
    @FXML private Label lblCapacity;

    @FXML private TableView<BikeDTO> bikeTable;
    @FXML private TableColumn<BikeDTO, Long> colBikeId;
    @FXML private TableColumn<BikeDTO, String> colBikeModel;
    @FXML private TableColumn<BikeDTO, String> colBikeState;

    @Override
    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;
        
        colBikeId.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().id()));
        
        colBikeModel.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().model()));
        
        colBikeState.setCellValueFactory(data -> {
            if (data.getValue().state() != null) {
                return new SimpleStringProperty(data.getValue().state().description());
            }
            return new SimpleStringProperty("Unknown");
        });
        
        bikeTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    public void loadStationData(Long stationId) {
        try {
            StationDTO station = ApiClient.getStationDetails(stationId);

            if (station != null) {
                lblName.setText(station.name());
                lblCoords.setText(station.lat() + ", " + station.lon());
                lblCapacity.setText(String.valueOf(station.maxDocks()));

                if (station.bikes() != null) {
                    bikeTable.setItems(FXCollections.observableArrayList(station.bikes()));
                } else {
                    bikeTable.getItems().clear();
                }
            }
        } catch (Exception e) {
            lblName.setText("Error loading data");
            e.printStackTrace();
        }
    }

    @FXML
    void backToList(ActionEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/css/javafx/view/stations.fxml", "Stations List", model);
    }

    @Override public Stage getStage() { return stage; }
    @Override public void setStage(Stage stage) { this.stage = stage; }
}