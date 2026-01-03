package pt.ul.fc.css.javafx.controller;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.ul.fc.css.javafx.model.DataModel;

public class Util {

    public static void switchScene(Stage stage, String fxmlPath, String title, DataModel model) {
        try {
            FXMLLoader loader = new FXMLLoader(Util.class.getResource(fxmlPath));
            Scene newScene = new Scene(loader.load());

            ControllerWithModel controller = loader.getController();
            controller.initModel(stage, model);

            stage.setTitle(title);
            stage.setScene(newScene);
            stage.show();

        } catch (IOException | ClassCastException e) {
            e.printStackTrace();
        }
    }
}
