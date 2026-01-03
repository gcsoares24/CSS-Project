package pt.ul.fc.css.javafx.controller;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import pt.ul.fc.css.javafx.api.ApiClient;
import pt.ul.fc.css.javafx.dto.TripDTO;
import pt.ul.fc.css.javafx.dto.UserDetailsDTO;
import pt.ul.fc.css.javafx.mapper.TripMapper;
import pt.ul.fc.css.javafx.mapper.UserMapper;
import pt.ul.fc.css.javafx.model.DataModel;
import pt.ul.fc.css.javafx.model.User;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserDetailsController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;

    @FXML
    private Label labelUserName;

    @FXML
    private Label labelUserEmail;

    @FXML
    private Label labelSubscription;

    @FXML
    private Label labelCreatedAt;

    @FXML
    private ListView<TripDTO> tripsList;

    @FXML
    private Label labelStatus;

    @FXML
    private Button btnBack;

    @FXML
    private void initialize() {
        tripsList.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(TripDTO trip, boolean empty) {
                super.updateItem(trip, empty);

                if (empty || trip == null) {
                    setText(null);
                } else {
                    int position = getIndex() + 1; // posição na lista começa em 1
                    setText(TripMapper.toString(trip, position));
                }
            }
        });
    }

    @Override
    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;
        loadUserDetails();
    }

    private void loadUserDetails() {
        try {
            Long userId = model.getLoggedUser();
            UserDetailsDTO userDTO = ApiClient.getUserDetails(userId);

            if (userDTO == null) {
                labelStatus.setText("No logged user found ✘");
                labelStatus.setVisible(true);
                return;
            }

            User user = UserMapper.toModel(userDTO);;

            labelUserName.setText("Name: " + user.getName());
            labelUserEmail.setText("Email: " + user.getEmail());
            labelSubscription.setText("Subscription: " + user.getSubscriptionType());
            labelCreatedAt.setText(
                    "Created At: " +
                    user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            );

            List<TripDTO> trips = user.getTrips().stream()
                    .sorted((t1, t2) -> t2.startTime().compareTo(t1.startTime()))
                    .toList();

            tripsList.getItems().setAll(trips);

        } catch (Exception e) {
            labelStatus.setText("Failed to load user details ✘");
            labelStatus.setVisible(true);
            e.printStackTrace();
        }
    }

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
