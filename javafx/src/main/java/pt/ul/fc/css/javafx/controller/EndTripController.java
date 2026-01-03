package pt.ul.fc.css.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import pt.ul.fc.css.javafx.api.ApiClient;
import pt.ul.fc.css.javafx.dto.EndTripDTO;
import pt.ul.fc.css.javafx.dto.StationDTO;
import pt.ul.fc.css.javafx.dto.TripReservedDTO;
import pt.ul.fc.css.javafx.model.DataModel;

import java.time.LocalDate;
import java.util.List;

public class EndTripController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;

    @FXML
    private ComboBox<TripReservedDTO> tripCombo;

    @FXML
    private ComboBox<StationDTO> stationCombo;

    @FXML
    private Button btnEndTrip;

    @FXML
    private Label labelStatus;

    @Override
    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;

        setupTripCombo();
        setupStationCombo();
        loadActiveTrips();
        loadStations();
    }

    // ---------------------------
    // Trip ComboBox
    // ---------------------------
    private void setupTripCombo() {
        tripCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(TripReservedDTO trip) {
                if (trip == null) return null;
                return "Bike: " + trip.bikeModel() + " | Since: " + trip.date();
            }

            @Override
            public TripReservedDTO fromString(String string) {
                return null;
            }
        });
    }

    // ---------------------------
    // Station ComboBox
    // ---------------------------
    private void setupStationCombo() {
        stationCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(StationDTO station) {
                if (station == null) return null;
                return station.name();
            }

            @Override
            public StationDTO fromString(String string) {
                return null;
            }
        });
    }

    // ---------------------------
    // Load IN_USE trips
    // ---------------------------
    private void loadActiveTrips() {
        try {
            Long userId = model.getLoggedUser(); // TODO: Logged user ID
            List<TripReservedDTO> trips = ApiClient.getTrips(userId, "IN_USE");

            if (trips.isEmpty()) {
                labelStatus.setText("No active trips found.");
                labelStatus.setVisible(true);
            }

            tripCombo.getItems().setAll(trips);
        } catch (Exception e) {
            showError("Error loading active trips", e);
        }
    }


    // ---------------------------
    // Load stations
    // ---------------------------
    private void loadStations() {
        try {
            String date = LocalDate.now().toString();
            List<StationDTO> stations = ApiClient.getAllStations(date);

            stationCombo.getItems().setAll(stations);

        } catch (Exception e) {
            showError("Error loading stations", e);
        }
    }

    // ---------------------------
    // End trip
    // ---------------------------
    @FXML
    void doEndTrip(ActionEvent event) {
    	TripReservedDTO trip = tripCombo.getValue();
        StationDTO station = stationCombo.getValue();

        if (trip == null) {
            labelStatus.setText("Select a trip to end!");
            labelStatus.setVisible(true);
            return;
        }

        if (station == null) {
            labelStatus.setText("Select a return station!");
            labelStatus.setVisible(true);
            return;
        }

        try {
            EndTripDTO req = new EndTripDTO(station.id());

            ApiClient.endTrip(trip.tripId(), req);

            labelStatus.setText("Trip ended successfully ✔");
            labelStatus.setVisible(true);

            tripCombo.getItems().remove(trip);
            btnEndTrip.setDisable(true);

        } catch (Exception e) {
            showError("Failed to end trip", e);
        }
    }

    // ---------------------------
    // Back
    // ---------------------------
    @FXML
    void back(ActionEvent event) {
        Util.switchScene(stage,
                "/pt/ul/fc/css/javafx/view/init.fxml",
                "Main Menu",
                model);
    }

    private void showError(String msg, Exception e) {
        labelStatus.setText(msg + ": " + e.getMessage());
        labelStatus.setVisible(true);
        e.printStackTrace();
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
