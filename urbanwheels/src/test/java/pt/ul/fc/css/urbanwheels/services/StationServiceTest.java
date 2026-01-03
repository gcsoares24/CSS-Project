package pt.ul.fc.css.urbanwheels.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pt.ul.fc.css.urbanwheels.entities.Station;
import pt.ul.fc.css.urbanwheels.repository.StationRepository;

@DataJpaTest
class StationServiceTest {

    @Autowired
    private StationRepository stationRepository;

    private StationService stationService;

    @Test
    void testCreateAndListStations() {
        stationService = new StationService(stationRepository);

        Station station1 = new Station();
        station1.setName("Station 1");
        station1.setLat(38.7169);
        station1.setLon(-9.139);
        station1.setMaxDocks(10);

        Station station2 = new Station();
        station2.setName("Station 2");
        station2.setLat(38.7170);
        station2.setLon(-9.140);
        station2.setMaxDocks(15);

        stationService.createStation(station1);
        stationService.createStation(station2);

        List<Station> stations = stationService.listAllStations();
        assertEquals(2, stations.size());
        assertTrue(stations.stream().anyMatch(s -> s.getName().equals("Station 1")));
        assertTrue(stations.stream().anyMatch(s -> s.getName().equals("Station 2")));
    }

    @Test
    void testGetStationDetails() {
        stationService = new StationService(stationRepository);

        Station station = new Station();
        station.setName("Test Station");
        station.setLat(38.7169);
        station.setLon(-9.139);
        station.setMaxDocks(12);

        Station saved = stationService.createStation(station);

        Station fetched = stationService.getStationDetails(saved.getId());
        assertNotNull(fetched);
        assertEquals("Test Station", fetched.getName());
        assertEquals(12, fetched.getMaxDocks());
    }

    @Test
    void testGetStationDetailsNotFound() {
        stationService = new StationService(stationRepository);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> stationService.getStationDetails(999L));

        assertEquals("Station with the id: 999 not found", ex.getMessage());
    }
}
