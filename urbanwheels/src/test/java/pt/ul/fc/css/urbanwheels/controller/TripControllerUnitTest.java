package pt.ul.fc.css.urbanwheels.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import pt.ul.fc.css.urbanwheels.dto.EndTripDTO;
import pt.ul.fc.css.urbanwheels.dto.TripDTO;
import pt.ul.fc.css.urbanwheels.dto.TripModDTO;
import pt.ul.fc.css.urbanwheels.services.TripService;

class TripControllerUnitTest {

    private TripService tripService;
    private TripController tripController;

    @BeforeEach
    void setup() {
        tripService = mock(TripService.class);
        tripController = new TripController(tripService);
    }

    // 🔗 A. Start trip test
    @Test
    void testStartTrip() {
        TripModDTO request = new TripModDTO(10L, 3L);
        TripDTO expected = new TripDTO(
                1L, "bikeModel", 3L,
                "Oriente", null,
                LocalDateTime.now(), null
        );

        when(tripService.startTrip(request)).thenReturn(expected);

        ResponseEntity<TripDTO> response = tripController.startTrip(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(expected.id(), response.getBody().id());
        verify(tripService, times(1)).startTrip(request);
    }

    // 🔚 B. End trip test
    @Test
    void testEndTrip() {

        EndTripDTO request = new EndTripDTO(6L); 


        String endStationName = "Oriente";

        
        TripDTO expected = new TripDTO(
                1L,                  
                "bikeModel",         
                3L,                  
                "Cais do Sodré",     
                endStationName,      
                LocalDateTime.now(),
                LocalDateTime.now()  
        );

     
        when(tripService.endTrip(1L, request)).thenReturn(expected);

       
        ResponseEntity<TripDTO> response = tripController.endTrip(1L, request);

 
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(endStationName, response.getBody().endStation()); 
        verify(tripService, times(1)).endTrip(1L, request);
    }

    // 📌 C. List trips - no filters
    @Test
    void testListTrips_NoFilters() {
        List<TripDTO> list = List.of(
        		new TripDTO(
                        1L, "bikeModel", 3L,
                        "Oriente", null,
                        LocalDateTime.now(), LocalDateTime.now()
                )
        );

        when(tripService.getAllTrips()).thenReturn(list);

        ResponseEntity<List<TripDTO>> response = tripController.listTrips(null, null);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        verify(tripService, times(1)).getAllTrips();
    }

    @Test
    void testListTrips_FilterByUser() {
        List<TripDTO> list = List.of(
            new TripDTO(
                1L, "bikeModel", 7L,
                "Oriente", null,
                LocalDateTime.now(), LocalDateTime.now()
            )
        );

        when(tripService.getTripsByUser(7L)).thenReturn(list);

        ResponseEntity<List<TripDTO>> response = tripController.listTrips(7L, null);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals(7L, response.getBody().get(0).userId());
        verify(tripService, times(1)).getTripsByUser(7L);
    }

    @Test
    void testListTrips_FilterByBike() {
        List<TripDTO> list = List.of(
            new TripDTO(
                1L, "City Cruiser", 3L,
                "Oriente", null,
                LocalDateTime.now(), LocalDateTime.now()
            )
        );

        when(tripService.getTripsByBike(21L)).thenReturn(list);

        ResponseEntity<List<TripDTO>> response = tripController.listTrips(null, 21L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("City Cruiser", response.getBody().get(0).bikeModel());
        verify(tripService, times(1)).getTripsByBike(21L);
    }

    @Test
    void testListTrips_FilterByBikeAndUser() {
        List<TripDTO> list = List.of(
            new TripDTO(
                1L, "Speed Racer", 15L,
                "Oriente", null,
                LocalDateTime.now(), LocalDateTime.now()
            )
        );

        when(tripService.getTripsByBikeAndUser(30L, 15L)).thenReturn(list);

        ResponseEntity<List<TripDTO>> response = tripController.listTrips(15L, 30L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Speed Racer", response.getBody().get(0).bikeModel());
        assertEquals(15L, response.getBody().get(0).userId());
        verify(tripService, times(1)).getTripsByBikeAndUser(30L, 15L);
    }
}
