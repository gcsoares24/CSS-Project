package pt.ul.fc.css.urbanwheels.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class BikeUnitTest {

    @Test
    void testNoArgsConstructorAndGettersSetters() {
        // given
        Bike bike = new Bike();

        State state = new State();
        state.setId(1);
        state.setDescription("Available");

        Station station = new Station();
        station.setId(10L);
        station.setName("Central");
        station.setLat(38.7);
        station.setLon(-9.1);
        station.setMaxDocks(15);

        List<Trip> trips = new ArrayList<>();
        Trip t1 = new Trip();
        t1.setId(100L);
        trips.add(t1);

        List<Maintenance> maints = new ArrayList<>();
        Maintenance m1 = new Maintenance();
        m1.setId(200L);
        maints.add(m1);

        // when
        bike.setId(5L);
        bike.setModel("Mountain");
        bike.setState(state);
        bike.setStation(station);
        bike.setTrips(trips);
        bike.setMaintenances(maints);

        // then
        assertEquals(5L, bike.getId());
        assertEquals("Mountain", bike.getModel());
        assertNotNull(bike.getState());
        assertEquals("Available", bike.getState().getDescription());
        assertNotNull(bike.getStation());
        assertEquals("Central", bike.getStation().getName());
        assertEquals(1, bike.getTrips().size());
        assertEquals(100L, bike.getTrips().get(0).getId());
        assertEquals(1, bike.getMaintenances().size());
        assertEquals(200L, bike.getMaintenances().get(0).getId());
    }

    @Test
    void testTripsAndMaintenancesManipulation() {
        Bike bike = new Bike();

        Trip trip = new Trip();
        trip.setId(101L);
        bike.setTrips(new ArrayList<>());
        bike.getTrips().add(trip);

        Maintenance m = new Maintenance();
        m.setId(201L);
        bike.setMaintenances(new ArrayList<>());
        bike.getMaintenances().add(m);

        assertEquals(1, bike.getTrips().size());
        assertEquals(101L, bike.getTrips().get(0).getId());
        assertEquals(1, bike.getMaintenances().size());
        assertEquals(201L, bike.getMaintenances().get(0).getId());

        bike.getTrips().clear();
        bike.getMaintenances().clear();

        assertTrue(bike.getTrips().isEmpty());
        assertTrue(bike.getMaintenances().isEmpty());
    }

    @Test
    void testStateAndStationNull() {
        Bike bike = new Bike();
        bike.setState(null);
        bike.setStation(null);

        assertNull(bike.getState());
        assertNull(bike.getStation());
    }
}
