package pt.ul.fc.css.javafx.controller;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import pt.ul.fc.css.javafx.api.ApiClient;
import pt.ul.fc.css.javafx.dto.TripDTO;
import pt.ul.fc.css.javafx.dto.TripReservedDTO;
import pt.ul.fc.css.javafx.model.DataModel;

import java.util.List;

public class CancelBicycleController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;

    @FXML
    private ComboBox<TripReservedDTO> tripCombo;

    @FXML
    private Button btnCancel;

    @FXML
    private Label labelStatus;

    @Override
    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;

        setupComboBox();
        loadReservations();
    }

    private void setupComboBox() {
        tripCombo.setConverter(new StringConverter<TripReservedDTO>() {
            @Override
            public String toString(TripReservedDTO trip) {
                if (trip == null) return null;
                return "Bike Model: " + trip.bikeModel()
                        + " | Reserved for: " + trip.date();
                        
            }

            @Override
            public TripReservedDTO fromString(String string) {
                return null; // Not needed
            }
        });
    }


    private void loadReservations() {
        try {
            Long userId = model.getLoggedUser();
            List<TripReservedDTO> trips = ApiClient.getTrips(userId, "RESERVED");

            tripCombo.getItems().clear();

            if (trips.isEmpty()) {
                labelStatus.setText("No reservations found.");
                labelStatus.setVisible(true);
                tripCombo.setDisable(true);
                btnCancel.setDisable(true);
            } else {
                tripCombo.getItems().addAll(trips);
                labelStatus.setVisible(false);
                tripCombo.setDisable(false);
                btnCancel.setDisable(false);  
            }

        } catch (Exception e) {
            labelStatus.setText("Error loading reservations: " + e.getMessage());
            labelStatus.setVisible(true);
            e.printStackTrace();
        }
    }


    @FXML
    void doCancel(ActionEvent event) {
    	TripReservedDTO selectedTrip = tripCombo.getValue();

        if (selectedTrip == null) {
            labelStatus.setText("Select a reservation!");
            labelStatus.setVisible(true);
            return;
        }

        try {
            ApiClient.cancelTrip(selectedTrip.tripId());

            labelStatus.setText("Reservation cancelled successfully! ✔");
            labelStatus.setVisible(true);

            tripCombo.getItems().remove(selectedTrip);
            btnCancel.setDisable(true);

        } catch (Exception e) {
            labelStatus.setText("Failed to cancel reservation ✘");
            labelStatus.setVisible(true);
            e.printStackTrace();
        }
    }

    @FXML
    void back(ActionEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/css/javafx/view/init.fxml", "Main Menu", model);
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
