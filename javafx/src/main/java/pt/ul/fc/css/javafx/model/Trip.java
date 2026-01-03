package pt.ul.fc.css.javafx.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Trip {

    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty bikeModel = new SimpleStringProperty();
    private final LongProperty userId = new SimpleLongProperty();
    private final StringProperty startStation = new SimpleStringProperty();
    private final StringProperty endStation = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> startTime = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> endTime = new SimpleObjectProperty<>();

    public Trip(long id, String bikeModel, long userId, String startStation, String endStation,
                LocalDateTime startTime, LocalDateTime endTime) {
        setId(id);
        setBikeModel(bikeModel);
        setUserId(userId);
        setStartStation(startStation);
        setEndStation(endStation);
        setStartTime(startTime);
        setEndTime(endTime);
    }

    public LongProperty idProperty() { return id; }
    public long getId() { return id.get(); }
    public void setId(long id) { this.id.set(id); }

    public StringProperty bikeModelProperty() { return bikeModel; }
    public String getBikeModel() { return bikeModel.get(); }
    public void setBikeModel(String bikeModel) { this.bikeModel.set(bikeModel); }

    public LongProperty userIdProperty() { return userId; }
    public long getUserId() { return userId.get(); }
    public void setUserId(long userId) { this.userId.set(userId); }

    public StringProperty startStationProperty() { return startStation; }
    public String getStartStation() { return startStation.get(); }
    public void setStartStation(String startStation) { this.startStation.set(startStation); }

    public StringProperty endStationProperty() { return endStation; }
    public String getEndStation() { return endStation.get(); }
    public void setEndStation(String endStation) { this.endStation.set(endStation); }

    public ObjectProperty<LocalDateTime> startTimeProperty() { return startTime; }
    public LocalDateTime getStartTime() { return startTime.get(); }
    public void setStartTime(LocalDateTime startTime) { this.startTime.set(startTime); }

    public ObjectProperty<LocalDateTime> endTimeProperty() { return endTime; }
    public LocalDateTime getEndTime() { return endTime.get(); }
    public void setEndTime(LocalDateTime endTime) { this.endTime.set(endTime); }

    @Override
    public String toString() {
        return "Trip " + getId() + ": " + getBikeModel() + " from " + getStartStation() +
               " to " + getEndStation();
    }
}
