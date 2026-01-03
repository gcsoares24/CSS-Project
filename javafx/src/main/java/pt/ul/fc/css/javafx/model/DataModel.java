package pt.ul.fc.css.javafx.model;

import java.io.File;
import java.util.List;

import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pt.ul.fc.css.javafx.dto.StateDTO;

public class DataModel {

    // --- BICYCLE LIST ---
    /*
     * The bicycleList reports mutations of the elements in it by using the given extractor.
     * We watch 'modelProperty' and 'stateProperty' so that if a bike's status changes
     * (e.g. from AVAILABLE to IN_USE), the UI updates automatically.
     */
    private final ObservableList<Bike> bicycleList = FXCollections.observableArrayList(
            bike -> new Observable[] { 
                bike.modelProperty(), 
                bike.stateProperty(),
                bike.stationIdProperty() 
            });

    // --- STATION LIST (NEW) ---
    /*
     * We watch 'nameProperty' and 'maxDocksProperty' to update UI if station details change.
     */
    private final ObservableList<Station> stationList = FXCollections.observableArrayList(
            station -> new Observable[] {
                station.nameProperty(),
                station.maxDocksProperty()
            });
    
    // --- USER LOGADO ---
    private final ObjectProperty<Long> loggedUser = new SimpleObjectProperty<>(null);

    public ObjectProperty<Long> loggedUserProperty() {
        return loggedUser;
    }

    public Long getLoggedUser() {
        return loggedUser.get();
    }

    public void setLoggedUser(Long userId) {
        this.loggedUser.set(userId);
    }

    // --- GETTERS ---
    public ObservableList<Bike> getBicycleList() {
        return bicycleList;
    }

    public ObservableList<Station> getStationList() {
        return stationList;
    }

    // --- CURRENT SELECTION TRACKING ---
    // Tracks the currently selected bike (useful if you share selection between screens)
    private final ObjectProperty<Bike> currentBike = new SimpleObjectProperty<>(null);

    public ObjectProperty<Bike> currentBikeProperty() {
        return currentBike;
    }

    public final Bike getCurrentBike() {
        return currentBikeProperty().get();
    }

    public final void setCurrentBike(Bike bike) {
        currentBikeProperty().set(bike);
    }

    // --- DATA LOADING ---
    public void loadData(File file) {
        // 1. Mock Data for Bikes
        StateDTO available = new StateDTO(1L, "DISPONIVEL");
        StateDTO maintenance = new StateDTO(2L, "EM_MANUTENCAO");

        bicycleList.setAll(
                new Bike(1, "Mountain Bike X1", available, 10L),
                new Bike(2, "City Cruiser", available, 11L),
                new Bike(3, "Electric Zoom", maintenance, 10L),
                new Bike(4, "Speed Racer", available, 12L),
                new Bike(5, "Kids Bike", available, 11L)
        );

        // 2. Mock Data for Stations (NEW)
        stationList.setAll(
                new Station(10L, "Central Station", 38.7369, -9.1426, 20),
                new Station(11L, "University Campus", 38.7569, -9.1555, 15),
                new Station(12L, "Riverside Park", 38.7000, -9.1600, 10)
        );
    }

    public void saveData(File file) {
        // Implementation for saving data if needed
    }

    // --- SETTERS FOR API INTEGRATION ---
    public void setBicycleList(List<Bike> list) {
        bicycleList.setAll(list);
    }

    public void setStationList(List<Station> list) {
        stationList.setAll(list);
    }
    
    public Bike getBikeById(Long bikeId) {
        if (bikeId == null) return null;

        return bicycleList.stream()
                .filter(bike -> bike.getId() == bikeId)
                .findFirst()
                .orElse(null);
    }

}