package pt.ul.fc.css.weatherwise.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class LocationUnitTest {

    @Test
    void testConstructorAndGetters() {
        Location location = new Location(1, "Lisbon");

        assertEquals(1, location.getId(), "ID should match the value passed to the constructor");
        assertEquals("Lisbon", location.getName(), "Name should match the value passed to the constructor");
    }

    @Test
    void testDefaultBehaviorAndSetters() {
        Location location = new Location(0, null);

        location.setId(42);
        location.setName("Porto");

        assertEquals(42, location.getId(), "ID should be correctly set via setter");
        assertEquals("Porto", location.getName(), "Name should be correctly set via setter");
    }

    @Test
    void testMutability() {
        Location location = new Location(5, "Coimbra");

        location.setId(10);
        location.setName("Braga");

        assertEquals(10, location.getId(), "ID should update correctly");
        assertEquals("Braga", location.getName(), "Name should update correctly");
    }
}
