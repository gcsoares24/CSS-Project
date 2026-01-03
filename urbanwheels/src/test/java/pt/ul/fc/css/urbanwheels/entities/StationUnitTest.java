package pt.ul.fc.css.urbanwheels.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class StationUnitTest {

    @Test
    void testDefaultConstructor() {
        Station station = new Station();

        // Campos iniciais
        assertNull(station.getId());
        assertNull(station.getName());
        assertEquals(0.0, station.getLat());
        assertEquals(0.0, station.getLon());
        assertEquals(0, station.getMaxDocks());
        assertNotNull(station.getBikes());
        assertTrue(station.getBikes().isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        Station station = new Station();

        station.setId(1L);
        station.setName("Central");
        station.setLat(38.7169);
        station.setLon(-9.1396);
        station.setMaxDocks(20);

        List<Bike> bikeList = new ArrayList<>();
        station.setBikes(bikeList);

        assertAll("Check all fields",
            () -> assertEquals(1L, station.getId()),
            () -> assertEquals("Central", station.getName()),
            () -> assertEquals(38.7169, station.getLat()),
            () -> assertEquals(-9.1396, station.getLon()),
            () -> assertEquals(20, station.getMaxDocks()),
            () -> assertEquals(bikeList, station.getBikes())
        );
    }

    @Test
    void testManipulation() {
        Station station = new Station();
        station.setBikes(new ArrayList<>());

        Bike bike1 = new Bike();
        Bike bike2 = new Bike();
        station.getBikes().add(bike1);
        station.getBikes().add(bike2);

        assertEquals(2, station.getBikes().size());
        assertTrue(station.getBikes().contains(bike1));
        assertTrue(station.getBikes().contains(bike2));
    }
}
