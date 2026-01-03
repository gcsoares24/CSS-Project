package pt.ul.fc.css.urbanwheels.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pt.ul.fc.css.urbanwheels.dto.TripDTO;
import pt.ul.fc.css.urbanwheels.dto.TripModDTO;
import pt.ul.fc.css.urbanwheels.entities.*;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.urbanwheels.repository.*;

class TripServiceUnitTest {

    private TripService tripService;

    @Mock private TripRepository tripRepo;
    @Mock private UserRepository userRepo;
    @Mock private BikeRepository bikeRepo;
    @Mock private StationRepository stationRepo;
    @Mock private StateRepository stateRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tripService = new TripService(tripRepo, userRepo, bikeRepo, stateRepo, stationRepo);
    }

    @Test
    void testStartTripSuccess() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Alice");
        client.setTrips(new ArrayList<>());

        Station station = new Station();
        station.setId(2L);
        station.setName("Central Station");

        State available = new State();
        available.setId(3);
        available.setDescription("AVAILABLE");

        Bike bike = new Bike();
        bike.setId(4L);
        bike.setModel("Model X");
        bike.setState(available);
        bike.setStation(station);

        TripModDTO dto = new TripModDTO(bike.getId(), client.getId());

        when(userRepo.findById(client.getId())).thenReturn(Optional.of(client));
        when(bikeRepo.findById(bike.getId())).thenReturn(Optional.of(bike));
        when(stationRepo.findById(station.getId())).thenReturn(Optional.of(station));
        when(stateRepo.findByDescription("AVAILABLE")).thenReturn(Optional.of(available));

        State inUse = new State();
        inUse.setId(5);
        inUse.setDescription("IN_USE");
        when(stateRepo.findByDescription("IN_USE")).thenReturn(Optional.of(inUse));

        when(tripRepo.save(any(Trip.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TripDTO tripDTO = tripService.startTrip(dto);

        assertNotNull(tripDTO);
        assertEquals(client.getId(), tripDTO.userId());
        assertEquals(bike.getModel(), tripDTO.bikeModel());
        assertEquals(station.getName(), tripDTO.startStation());
        assertNotNull(tripDTO.startTime());
        assertEquals("IN_USE", bike.getState().getDescription());
    }


    @Test
    void testStartTripUserNotFound() {
        TripModDTO dto = new TripModDTO(1L, 2L);
        when(userRepo.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> tripService.startTrip(dto));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void testStartTripBikeNotAvailable() {
        Client client = new Client();
        client.setId(1L);
        client.setName("Alice");
        client.setTrips(new ArrayList<>());

        Bike bike = new Bike();
        bike.setId(2L);
        bike.setModel("Model X");
        State maintenance = new State();
        maintenance.setId(1);
        maintenance.setDescription("MAINTENANCE");
        bike.setState(maintenance);

        Station station = new Station();
        station.setId(3L);
        station.setName("Central Station");

        TripModDTO dto = new TripModDTO(bike.getId(), client.getId());

        // Mock repository responses
        when(userRepo.findById(client.getId())).thenReturn(Optional.of(client));
        when(bikeRepo.findById(bike.getId())).thenReturn(Optional.of(bike));
        when(stationRepo.findById(station.getId())).thenReturn(Optional.of(station));

        // Mock AVAILABLE state
        State available = new State();
        available.setId(2);
        available.setDescription("AVAILABLE");
        when(stateRepo.findByDescription("AVAILABLE")).thenReturn(Optional.of(available));

        // Expect IllegalStateException, not ResourceNotFoundException
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> tripService.startTrip(dto));

        assertEquals("Bike is not available", ex.getMessage());
    }

    @Test
    void testGetTripsByUserEmpty() {
        when(tripRepo.findByUserId(1L)).thenReturn(List.of());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> tripService.getTripsByUser(1L));

        assertEquals("No trips found for that user", ex.getMessage());
    }

    @Test
    void testGetTripsByBikeEmpty() {
        when(tripRepo.findByBikeId(2L)).thenReturn(List.of());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> tripService.getTripsByBike(2L));

        assertEquals("No trips found for that bike", ex.getMessage());
    }

    @Test
    void testGetTripsByBikeAndUserEmpty() {
        when(tripRepo.findByBikeIdAndUserId(2L, 1L)).thenReturn(List.of());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> tripService.getTripsByBikeAndUser(2L, 1L));

        assertEquals("No trips found for that bike and user combination", ex.getMessage());
    }
}
