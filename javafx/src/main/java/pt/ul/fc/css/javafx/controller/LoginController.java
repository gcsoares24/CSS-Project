package pt.ul.fc.css.javafx.controller;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import pt.ul.fc.css.javafx.model.DataModel;
import pt.ul.fc.css.javafx.model.User;
import pt.ul.fc.css.javafx.api.ApiClient;
import pt.ul.fc.css.javafx.dto.UserDetailsDTO;
import pt.ul.fc.css.javafx.mapper.UserMapper;

public class LoginController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Label labelStatus;

    @Override
    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;
    }

    @FXML
    void doLogin(ActionEvent event) {
        String email = txtEmail.getText();
        String password = txtPassword.getText();

        if (email.isEmpty() || password.isEmpty()) {
            labelStatus.setText("Please enter email and password.");
            labelStatus.setVisible(true);
            return;
        }

        try {
            // Autenticar via API (mock) e guardar token internamente
            Long userId = ApiClient.login(email, password);
            model.setLoggedUser(userId);
            // Redirecionar para a cena principal (UserDetails ou Main Menu)
            Util.switchScene(stage, "/pt/ul/fc/css/javafx/view/init.fxml", "Main Menu", model);

        } catch (Exception e) {
            labelStatus.setText("Login failed: " + e.getMessage());
            labelStatus.setVisible(true);
            e.printStackTrace();
        }
    }


    @Override
    public Stage getStage() { return stage; }

    @Override
    public void setStage(Stage stage) { this.stage = stage; }
}
