package pt.ul.fc.css.javafx.model;

import javafx.beans.property.*;
import pt.ul.fc.css.javafx.dto.TripDTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.util.List;

public class User {

    private final long id;
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty subscriptionType = new SimpleStringProperty();
    private final ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>();
    private final ObservableList<TripDTO> trips = FXCollections.observableArrayList(); 

    public User(long id, String name, String email, String subscriptionType, LocalDateTime createdAt, List<TripDTO> trips) {
        this.id = id;
        setName(name);
        setEmail(email);
        setSubscriptionType(subscriptionType);
        setCreatedAt(createdAt);
        setTrips(trips);
    }

    public long getId() {
        return id;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty subscriptionTypeProperty() {
        return subscriptionType;
    }

    public String getSubscriptionType() {
        return subscriptionType.get();
    }

    public void setSubscriptionType(String subscriptionType) {
        this.subscriptionType.set(subscriptionType);
    }

    public ObjectProperty<LocalDateTime> createdAtProperty() {
        return createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt.get();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt.set(createdAt);
    }

    public ObservableList<TripDTO> tripsProperty() {
        return trips;
    }

    public ObservableList<TripDTO> getTrips() {
        return trips;
    }

    public void setTrips(List<TripDTO> trips2) {
        this.trips.setAll(trips2); // funciona agora
    }

    @Override
    public String toString() {
        return getName() + " (" + getEmail() + ")";
    }
}
