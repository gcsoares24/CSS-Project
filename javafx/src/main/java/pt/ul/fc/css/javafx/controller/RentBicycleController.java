package pt.ul.fc.css.javafx.controller;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import pt.ul.fc.css.javafx.api.ApiClient;
import pt.ul.fc.css.javafx.dto.BikeDTO;
import pt.ul.fc.css.javafx.dto.StationDTO;
import pt.ul.fc.css.javafx.dto.WeatherConditionDTO;
import pt.ul.fc.css.javafx.model.Bike;
import pt.ul.fc.css.javafx.model.DataModel;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RentBicycleController implements ControllerWithModel {

    private DataModel model;
    private Stage stage;

    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Bike> bicycleCombo;
    @FXML private Button btnReserve;
    @FXML private Label labelStatus;
    @FXML private Label labelWeather;

    @Override
    public void initModel(Stage stage, DataModel model) {
        this.stage = stage;
        this.model = model;

        // DatePicker format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        datePicker.setConverter(new StringConverter<>() {
            @Override public String toString(LocalDate date) { return date != null ? date.format(formatter) : ""; }
            @Override public LocalDate fromString(String string) { return (string != null && !string.isBlank()) ? LocalDate.parse(string, formatter) : null; }
        });
        datePicker.setValue(LocalDate.now());

        setupComboBox();
        loadBicycles();
        setupListeners();
    }

    private void setupComboBox() {
        // Display bike model, state, and station name
        bicycleCombo.setConverter(new StringConverter<>() {
            @Override
            public String toString(Bike bike) {
                if (bike == null) return null;
                return bike.getDisplayName() + " | Station: " + (bike.getStationName() != null ? bike.getStationName() : "Unknown");
            }

            @Override
            public Bike fromString(String string) {
                return bicycleCombo.getItems().stream()
                        .filter(b -> toString(b).equals(string))
                        .findFirst().orElse(null);
            }
        });

        // Optional: color each cell according to weather
        bicycleCombo.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Bike bike, boolean empty) {
                super.updateItem(bike, empty);

                if (bike == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    
                    String text = bike.getDisplayName() + " | Station: " + (bike.getStationName() != null ? bike.getStationName() : "Unknown");

                    // Texto em Label
                    Label label = new Label(text);

                    // Quadrado colorido representando a cor da bike
                    Region colorBox = new Region();
                    colorBox.setPrefSize(10, 10);
                    colorBox.setStyle("-fx-background-color: " + toHex(bike.getColor()) + ";");

                    // HBox com espaçamento
                    HBox hbox = new HBox(5);
                    hbox.getChildren().addAll(colorBox, label);

                    setGraphic(hbox);
                }
            }
        });


    }

    private String toHex(Paint paint) {
        if (paint instanceof Color c) {
            return String.format("#%02X%02X%02X",
                    (int) (c.getRed() * 255),
                    (int) (c.getGreen() * 255),
                    (int) (c.getBlue() * 255));
        }
        return "#FFFFFF";
    }

    private void loadBicycles() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            labelStatus.setText("Select a date first.");
            labelStatus.setVisible(true);
            return;
        }

        String dateStr = selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        List<StationDTO> stations;
        List<BikeDTO> bikeDTOs;

        try {
            stations = ApiClient.getAllStations(dateStr);
        } catch (Exception e) {
            labelStatus.setText("Failed to load stations: " + e.getMessage());
            labelStatus.setVisible(true);
            return;
        }

        try {
            bikeDTOs = ApiClient.getAllBicycles("AVAILABLE");
        } catch (Exception e) {
            labelStatus.setText("No bikes available for reservation.");
            labelStatus.setVisible(true);
            bicycleCombo.getItems().clear();
            return;
        }

        List<Bike> bikes = bikeDTOs.stream().map(dto -> {
            StationDTO station = stations.stream()
                    .filter(s -> s.id().equals(dto.stationId()))
                    .findFirst()
                    .orElse(null);
            Bike bike = new Bike(dto, station != null ? station.weather() : null);
            if (station != null) bike.setStationName(station.name());
            return bike;
        }).toList();

        bicycleCombo.getItems().setAll(bikes);
        labelStatus.setVisible(false);
    }




    @FXML
    void doReserve(ActionEvent event) {
        LocalDate date = datePicker.getValue();
        Bike selectedBike = bicycleCombo.getValue();

        if (selectedBike == null) {
            labelStatus.setText("Select a bike!");
            labelStatus.setVisible(true);
            return;
        }
        if (date.isBefore(LocalDate.now())) {
            labelStatus.setText("Pick a day today or after!");
            labelStatus.setVisible(true);
            return;
        }

        try {
            Long userId = model.getLoggedUser();
            String dateStr = date.atStartOfDay().toString();
            ApiClient.reserveTrip(userId, selectedBike.getId(), dateStr);

            labelStatus.setText("Reservation completed successfully! ✔");
            labelStatus.setVisible(true);
            btnReserve.setDisable(true);
        } catch (Exception e) {
            labelStatus.setText("Failed to make reservation ✘");
            labelStatus.setVisible(true);
            e.printStackTrace();
        }
    }

    private void setupListeners() {
        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> updateWeather());
        bicycleCombo.valueProperty().addListener((obs, oldBike, newBike) -> updateWeather());
    }

    private void updateWeather() {
        Bike selectedBike = bicycleCombo.getValue();
        LocalDate selectedDate = datePicker.getValue();

        if (selectedBike == null || selectedDate == null) {
            labelWeather.setText("Select a bike and a date to see the weather.");
            return;
        }

        try {
            String dateStr = selectedDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            List<StationDTO> stations = ApiClient.getAllStations(dateStr);

            StationDTO station = stations.stream()
                    .filter(s -> s.id().equals(selectedBike.getStationId()))
                    .findFirst().orElse(null);

            if (station != null) selectedBike.setWeather(station.weather());

            WeatherConditionDTO weather = selectedBike.getWeather();
            String weatherText;

            if (weather == null || "UNAVAILABLE".equalsIgnoreCase(weather.condition())) {
                weatherText = "Unavailable";
            } else {
                weatherText = weather.condition() + ", " + weather.minTemp() + "°C → " + weather.maxTemp() + "°C";
            }

            labelWeather.setText("Weather at " + (station != null ? station.name() : "Unknown")
                    + " on " + dateStr + ": " + weatherText);

        } catch (Exception e) {
            labelWeather.setText("Failed to get weather: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void back(ActionEvent event) {
        Util.switchScene(stage, "/pt/ul/fc/css/javafx/view/init.fxml", "Main Menu", model);
    }

    @Override
    public Stage getStage() { return stage; }

    @Override
    public void setStage(Stage stage) { this.stage = stage; }
}
