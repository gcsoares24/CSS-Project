package pt.ul.fc.css.urbanwheels.mapper;

import org.junit.jupiter.api.Test;
import pt.ul.fc.css.urbanwheels.dto.TripDTO;
import pt.ul.fc.css.urbanwheels.entities.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TripMapperUnitTest {

	@Test
	void testToDTO_full() {
	    Bike bike = new Bike();
	    bike.setId(1L);
	    bike.setModel("Model X");

	    Client client = new Client();
	    client.setId(2L);

	    Station start = new Station();
	    start.setId(10L);
	    start.setName("Oriente");

	    Station end = new Station();
	    end.setId(20L);
	    end.setName("Central Station");

	    Trip trip = new Trip();
	    trip.setId(100L);
	    trip.setBike(bike);
	    trip.setUser(client);
	    trip.setStartStation(start);
	    trip.setEndStation(end);
	    trip.setStartTime(LocalDateTime.of(2025, 11, 23, 10, 0));
	    trip.setEndTime(LocalDateTime.of(2025, 11, 23, 11, 0));

	    TripDTO dto = TripMapper.toDTO(trip);

	    assertNotNull(dto);
	    assertEquals(100L, dto.id());
	    assertEquals("Model X", dto.bikeModel());
	    assertEquals(2L, dto.userId());
	    assertEquals("Oriente", dto.startStation());
	    assertEquals("Central Station", dto.endStation());
	    assertEquals(LocalDateTime.of(2025, 11, 23, 10, 0), dto.startTime());
	    assertEquals(LocalDateTime.of(2025, 11, 23, 11, 0), dto.endTime());
	}

    @Test
    void testToDTO_nullTrip() {
        TripDTO dto = TripMapper.toDTO(null);
        assertNull(dto);
    }

    @Test
    void testToDTO_endStationNull() {
        Bike bike = new Bike();
        bike.setId(1L);

        Client client = new Client();
        client.setId(2L);

        Station start = new Station();
        start.setId(10L);

        Trip trip = new Trip();
        trip.setId(100L);
        trip.setBike(bike);
        trip.setUser(client);
        trip.setStartStation(start);
        trip.setEndStation(null);
        trip.setStartTime(LocalDateTime.now());

        TripDTO dto = TripMapper.toDTO(trip);
        assertNotNull(dto);
        assertNull(dto.endStation());
    }
}
