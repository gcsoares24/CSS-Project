package pt.ul.fc.css.javafx.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Subscription {

    private final long id;
    private final StringProperty name = new SimpleStringProperty();

    public Subscription(long id, String name) {
        this.id = id;
        setName(name);
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

    @Override
    public String toString() {
        return getName();
    }
}