package pt.ul.fc.css.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import pt.ul.fc.css.javafx.api.ApiClient;
import pt.ul.fc.css.javafx.dto.StationDTO;
import pt.ul.fc.css.javafx.dto.TripModDTO;
import pt.ul.fc.css.javafx.model.Bike;
import pt.ul.fc.css.javafx.model.DataModel;

import java.util.List;

public class StartTripController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;

    private List<StationDTO> stations;

    @FXML
    private ComboBox<StationDTO> stationCombo;

    @FXML
    private ComboBox<Bike> bicycleCombo;

    @FXML
    private Button btnStartTrip;

    @FXML
    private Label labelStatus;

    // ---------------------------
    // Init
    // ---------------------------
    @Override
    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;

        setupStationCombo();
        setupBicycleCombo();

        loadStations();
    }

    // ---------------------------
    // Station ComboBox
    // ---------------------------
    private void setupStationCombo() {
        stationCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(StationDTO s) {
                return s == null ? null : s.name();
            }

            @Override
            public StationDTO fromString(String string) {
                return stationCombo.getItems().stream()
                        .filter(s -> s.name().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        stationCombo.setOnAction(e -> loadBicyclesForStation());
    }

    // ---------------------------
    // Bike ComboBox
    // ---------------------------
    private void setupBicycleCombo() {

        bicycleCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Bike bike) {
                if (bike == null) return null;
                return bike.getDisplayName();
            }

            @Override
            public Bike fromString(String string) {
                return bicycleCombo.getItems().stream()
                        .filter(b -> b.getDisplayName().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        // Simplificado: apenas mostra o nome
        bicycleCombo.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Bike bike, boolean empty) {
                super.updateItem(bike, empty);

                if (bike == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(bike.getDisplayName());
                }
            }
        });
    }

    // ---------------------------
    // Load stations
    // ---------------------------
    private void loadStations() {
        try {
            stations = ApiClient.getAllStations("");
            stationCombo.getItems().setAll(stations);
        } catch (Exception e) {
            showError("Error loading stations", e);
        }
    }

    // ---------------------------
    // Load bikes for selected station
    // ---------------------------
    private void loadBicyclesForStation() {

        StationDTO station = stationCombo.getValue();
        bicycleCombo.getItems().clear();

        if (station == null) return;

        try {
            List<Bike> bikes = ApiClient.getAllBicycles("AVAILABLE").stream()
                    .filter(dto -> dto.stationId().equals(station.id()))
                    .map(dto -> {
                        Bike bike = new Bike(dto, station.weather());
                        bike.setStationName(station.name());
                        return bike;
                    })
                    .toList();
            
            bicycleCombo.getItems().setAll(bikes);

        } catch (Exception e) {
            showError("Error loading bicycles", e);
        }
    }

    // ---------------------------
    // Start trip
    // ---------------------------
    @FXML
    void doStartTrip(ActionEvent event) {

        StationDTO station = stationCombo.getValue();
        Bike bike = bicycleCombo.getValue();

        if (station == null) {
            labelStatus.setText("Select a station first!");
            labelStatus.setVisible(true);
            return;
        }

        if (bike == null) {
            labelStatus.setText("Select a bike!");
            labelStatus.setVisible(true);
            return;
        }

        try {
            Long userId = model.getLoggedUser();

            TripModDTO req = new TripModDTO(
                    bike.getId(),
                    userId
            );

            ApiClient.startTrip(req);

            labelStatus.setText("Trip started successfully ✔");
            labelStatus.setVisible(true);

            bicycleCombo.getItems().remove(bike);

        } catch (Exception e) {
            showError("Failed to start trip", e);
        }
    }

    // ---------------------------
    // Utils
    // ---------------------------
    private void showError(String msg, Exception e) {
        labelStatus.setText(msg + ": " + e.getMessage());
        labelStatus.setVisible(true);
        e.printStackTrace();
    }

    // ---------------------------
    // Back
    // ---------------------------
    @FXML
    void back(ActionEvent event) {
        Util.switchScene(
                stage,
                "/pt/ul/fc/css/javafx/view/init.fxml",
                "Main Menu",
                model
        );
    }

    @Override
    public Stage getStage() {
        return stage;
    }

    @Override
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
