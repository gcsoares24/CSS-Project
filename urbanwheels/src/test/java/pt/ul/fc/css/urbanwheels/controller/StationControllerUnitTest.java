package pt.ul.fc.css.urbanwheels.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pt.ul.fc.css.urbanwheels.dto.StationDTO;
import pt.ul.fc.css.urbanwheels.dto.StationRequestDTO;
import pt.ul.fc.css.urbanwheels.entities.Station;
import pt.ul.fc.css.urbanwheels.services.StationService;
import pt.ul.fc.css.urbanwheels.services.WeatherService;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StationControllerUnitTest {

	private StationService stationService;
	private WeatherService weatherService; // whatever the second dependency is
	private StationController stationController;

	@BeforeEach
	void setup() {
	    stationService = mock(StationService.class);
	    weatherService = mock(WeatherService.class); // mock it
	    stationController = new StationController(stationService, weatherService);
	}


    @Test
    void testGetAllStations_nonEmpty() {
        // given
        Station s1 = new Station();
        s1.setId(1L);
        s1.setName("Station 1");
        Station s2 = new Station();
        s2.setId(2L);
        s2.setName("Station 2");

        when(stationService.listAllStations()).thenReturn(List.of(s1, s2));

        // when
        ResponseEntity<List<StationDTO>> response = stationController.getAllStations(null);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());   // <-- atualizado
        assertEquals(2, response.getBody().size());
        assertEquals("Station 1", response.getBody().get(0).name());
        assertEquals("Station 2", response.getBody().get(1).name());
    }

    @Test
    void testGetAllStations_empty() {
        when(stationService.listAllStations()).thenReturn(new ArrayList<>());

        ResponseEntity<List<StationDTO>> response = stationController.getAllStations(null);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());   // <-- atualizado
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetStationById_exists() {
        Station s = new Station();
        s.setId(1L);
        s.setName("Central");

        when(stationService.getStationDetails(1L)).thenReturn(s);

        ResponseEntity<StationDTO> response = stationController.getStationById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());   // <-- atualizado
        assertEquals("Central", response.getBody().name());
    }

    @Test
    void testGetStationById_notExists() {
        when(stationService.getStationDetails(1L)).thenThrow(new RuntimeException("Station with the id: 1 not found"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            stationController.getStationById(1L);
        });

        assertEquals("Station with the id: 1 not found", exception.getMessage());
    }

    @Test
    void testCreateStation_success() {
        StationRequestDTO dto = new StationRequestDTO("New Station", 12.34, 56.78, 10);
        Station saved = new Station();
        saved.setId(5L);
        saved.setName("New Station");

        when(stationService.createStation(any(Station.class))).thenReturn(saved);

        ResponseEntity<StationDTO> response = stationController.createStation(dto);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());   // <-- atualizado
        assertEquals("New Station", response.getBody().name());
        assertEquals(5L, response.getBody().id());
    }
}
