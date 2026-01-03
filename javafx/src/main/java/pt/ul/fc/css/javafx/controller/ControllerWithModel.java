package pt.ul.fc.css.javafx.controller;

import javafx.stage.Stage;

import pt.ul.fc.css.javafx.model.DataModel;

public interface ControllerWithModel {
    void initModel(Stage stage, DataModel model);

    Stage getStage();

    void setStage(Stage stage);
}
