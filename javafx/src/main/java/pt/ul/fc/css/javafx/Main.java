package pt.ul.fc.css.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.ul.fc.css.javafx.controller.ControllerWithModel;
import pt.ul.fc.css.javafx.model.DataModel;
// Importa o LoginController para garantir que a classe existe
import pt.ul.fc.css.javafx.controller.LoginController; 

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // 1. Initialize the Data Model
        DataModel model = new DataModel();
        model.loadData(null); 

        // --- Test connection ---
        boolean connected = pt.ul.fc.css.javafx.api.ApiClient.testConnection();
        if (connected) {
            System.out.println("Connected to UrbanWheels backend! ✔");
        } else {
            System.out.println("Failed to connect to UrbanWheels backend ✘");
        }
        // -----------------------

        // 2. Load the Login View (MUDANÇA AQUI)
        // Certifica-te que criaste este ficheiro em src/main/resources/.../view/
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/pt/ul/fc/css/javafx/view/login.fxml"));
        
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("UrbanWheels - Login"); // Muda o título da janela
        stage.setScene(scene);

        // 3. Pass the model to the controller
        // O teu LoginController TEM de estender ControllerWithModel para isto funcionar
        ControllerWithModel controller = fxmlLoader.getController();
        controller.initModel(stage, model);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}