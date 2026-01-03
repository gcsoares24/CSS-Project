package pt.ul.fc.css.urbanwheels.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pt.ul.fc.css.urbanwheels.entities.Station;
import pt.ul.fc.css.urbanwheels.repository.StationRepository;

class StationServiceUnitTest {

    @Mock
    private StationRepository stationRepository;

    @InjectMocks
    private StationService stationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateStation() {
        Station station = new Station();
        station.setName("Station 1");
        station.setLat(38.7);
        station.setLon(-9.1);
        station.setMaxDocks(10);

        when(stationRepository.save(station)).thenReturn(station);

        Station saved = stationService.createStation(station);
        assertNotNull(saved);
        assertEquals("Station 1", saved.getName());
        verify(stationRepository, times(1)).save(station);
    }

    @Test
    void testListAllStations() {
        Station station1 = new Station();
        station1.setName("Station 1");

        Station station2 = new Station();
        station2.setName("Station 2");

        when(stationRepository.findAll()).thenReturn(Arrays.asList(station1, station2));

        List<Station> stations = stationService.listAllStations();
        assertEquals(2, stations.size());
        assertTrue(stations.stream().anyMatch(s -> s.getName().equals("Station 1")));
        assertTrue(stations.stream().anyMatch(s -> s.getName().equals("Station 2")));

        verify(stationRepository, times(1)).findAll();
    }

    @Test
    void testGetStationDetailsFound() {
        Station station = new Station();
        station.setId(1L);
        station.setName("Station 1");

        when(stationRepository.findById(1L)).thenReturn(Optional.of(station));

        Station fetched = stationService.getStationDetails(1L);
        assertNotNull(fetched);
        assertEquals("Station 1", fetched.getName());
        verify(stationRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStationDetailsNotFound() {
        when(stationRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> stationService.getStationDetails(999L));
        assertEquals("Station with the id: 999 not found", ex.getMessage());

        verify(stationRepository, times(1)).findById(999L);
    }
}
