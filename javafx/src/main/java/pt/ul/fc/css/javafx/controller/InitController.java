package pt.ul.fc.css.javafx.controller;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import pt.ul.fc.css.javafx.model.DataModel;

public class InitController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;

    @FXML
    private Button btnRentBicycle;
    @FXML
    void openStations(ActionEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/css/javafx/view/stations.fxml",
                "Stations", model);
    }

    @FXML
    void openRentBike(ActionEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/css/javafx/view/rentBike.fxml",
                "Rent a Bicycle", model);
    }

    @FXML
    void openCancelBike(ActionEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/css/javafx/view/cancelBike.fxml",
                "Cancel Reservation", model);
    }

    @FXML
    void openPickupBike(ActionEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/css/javafx/view/pickupBike.fxml",
                "Pick Up Bicycle", model);
    }

    @FXML
    void openReturnBike(ActionEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/css/javafx/view/returnBike.fxml",
                "Return Bicycle", model);
    }

    @FXML
    void openUserDetails(ActionEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/css/javafx/view/userDetails.fxml",
                "My Account / Trips", model);
    }
    
    @FXML
    private void doLogout(ActionEvent event) {
    	Util.switchScene(stage, "/pt/ul/fc/css/javafx/view/login.fxml",
                "Login", model);
    }



    @FXML
    void quit(ActionEvent event) {
        System.exit(0);
    }

    @Override
    public void initModel(Stage stage, DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.stage = stage;
        this.model = model;
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