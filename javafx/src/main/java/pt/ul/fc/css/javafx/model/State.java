package pt.ul.fc.css.javafx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class State {

    private final long id; // Using long to match standard ID types, even if Entity uses Integer
    private final StringProperty description = new SimpleStringProperty();

    public State(long id, String description) {
        this.id = id;
        setDescription(description);
    }

    public long getId() {
        return id;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public String toString() {
        return getDescription();
    }
}