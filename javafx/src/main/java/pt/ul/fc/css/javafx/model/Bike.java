package pt.ul.fc.css.javafx.model;

import javafx.beans.property.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import pt.ul.fc.css.javafx.dto.BikeDTO;
import pt.ul.fc.css.javafx.dto.StateDTO;
import pt.ul.fc.css.javafx.dto.WeatherConditionDTO;

public class Bike {

    // --- Basic properties ---
    private final long id;
    private final StringProperty model = new SimpleStringProperty();
    private final ObjectProperty<StateDTO> state = new SimpleObjectProperty<>();
    private final LongProperty stationId = new SimpleLongProperty();

    // --- Extra properties ---
    private final ObjectProperty<WeatherConditionDTO> weather = new SimpleObjectProperty<>();
    private final StringProperty stationName = new SimpleStringProperty();

    // --- Constructors ---
    public Bike(long id, String model, StateDTO state, long stationId) {
        this.id = id;
        setModel(model);
        setState(state);
        setStationId(stationId);
    }

    public Bike(BikeDTO dto, WeatherConditionDTO weather) {
        this.id = dto.id();
        setModel(dto.model());
        setState(dto.state());
        setStationId(dto.stationId());
        setWeather(weather);
    }

    // --- Basic getters/setters ---
    public long getId() { return id; }

    public StringProperty modelProperty() { return model; }
    public String getModel() { return model.get(); }
    public void setModel(String model) { this.model.set(model); }

    public ObjectProperty<StateDTO> stateProperty() { return state; }
    public StateDTO getState() { return state.get(); }
    public void setState(StateDTO state) { this.state.set(state); }

    public LongProperty stationIdProperty() { return stationId; }
    public long getStationId() { return stationId.get(); }
    public void setStationId(long stationId) { this.stationId.set(stationId); }

    // --- Weather related ---
    public ObjectProperty<WeatherConditionDTO> weatherProperty() { return weather; }
    public WeatherConditionDTO getWeather() { return weather.get(); }
    public void setWeather(WeatherConditionDTO weather) { this.weather.set(weather); }

    // --- Station name ---
    public StringProperty stationNameProperty() { return stationName; }
    public String getStationName() { return stationName.get(); }
    public void setStationName(String name) { this.stationName.set(name); }

    // --- Display ---
    public String getDisplayName() {
        String stateDesc = (getState() != null) ? getState().description() : "Unknown";
        return getModel() + " (" + stateDesc + ")";
    }

    // --- Color logic ---
    public Paint getColor() {
        if (getWeather() == null || getWeather().condition() == null) return Color.GRAY;

        String condition = getWeather().condition();
        Double minTemp = getWeather().minTemp();
        Double maxTemp = getWeather().maxTemp();

        // Heavy/moderate rain or storms → condition-based
        switch (condition) {
            case "Dense drizzle", "Moderate rain", "Moderate rain showers",
                 "Moderate snow fall", "Heavy freezing drizzle":
                return Color.DARKORANGE;
            case "Heavy rain", "Violent rain showers", "Heavy snow fall",
                 "Heavy snow showers", "Thunderstorm with slight hail",
                 "Thunderstorm with heavy hail", "Slight or moderate thunderstorm":
                return Color.RED;
            default:
                // Otherwise, compute based on temperature
                if (minTemp == null || maxTemp == null) return Color.GRAY;
                double avg = (minTemp + maxTemp) / 2.0;

                if (avg < 0 || avg > 35) return Color.RED;
                else if (avg <= 10) return Color.ORANGE;
                else if (avg <= 18) return Color.YELLOW;
                else if (avg <= 22) return Color.GREENYELLOW;
                else if (avg <= 28) return Color.LIMEGREEN;
                else if (avg <= 32) return Color.ORANGE;
                else return Color.RED;
        }
    }
}
