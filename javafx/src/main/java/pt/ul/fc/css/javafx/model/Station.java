package pt.ul.fc.css.javafx.model;

import javafx.beans.property.*;

public class Station {

    private final long id;
    private final StringProperty name = new SimpleStringProperty();
    private final DoubleProperty lat = new SimpleDoubleProperty();
    private final DoubleProperty lon = new SimpleDoubleProperty();
    private final IntegerProperty maxDocks = new SimpleIntegerProperty();

    // You can add a list of bikes here if needed, usually managed separately in DataModel
    // private final ObservableList<Bike> bikes = FXCollections.observableArrayList();

    public Station(long id, String name, double lat, double lon, int maxDocks) {
        this.id = id;
        setName(name);
        setLat(lat);
        setLon(lon);
        setMaxDocks(maxDocks);
    }

    public long getId() {
        return id;
    }

    // --- Name ---
    public StringProperty nameProperty() { return name; }
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    // --- Latitude ---
    public DoubleProperty latProperty() { return lat; }
    public double getLat() { return lat.get(); }
    public void setLat(double lat) { this.lat.set(lat); }

    // --- Longitude ---
    public DoubleProperty lonProperty() { return lon; }
    public double getLon() { return lon.get(); }
    public void setLon(double lon) { this.lon.set(lon); }

    // --- Max Docks ---
    public IntegerProperty maxDocksProperty() { return maxDocks; }
    public int getMaxDocks() { return maxDocks.get(); }
    public void setMaxDocks(int maxDocks) { this.maxDocks.set(maxDocks); }

    @Override
    public String toString() {
        return getName() + " (" + getLat() + ", " + getLon() + ")";
    }
}