package pt.ul.fc.css.urbanwheels.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import pt.ul.fc.css.urbanwheels.dto.EndTripDTO;
import pt.ul.fc.css.urbanwheels.dto.TripDTO;
import pt.ul.fc.css.urbanwheels.dto.TripModDTO;
import pt.ul.fc.css.urbanwheels.entities.*;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.urbanwheels.repository.*;

@DataJpaTest
@Import(TripService.class)
class TripServiceTest {

    @Autowired private TripService tripService;
    @Autowired private TripRepository tripRepo;
    @Autowired private UserRepository userRepo;
    @Autowired private BikeRepository bikeRepo;
    @Autowired private SubscriptionRepository subrepo;
    @Autowired private StationRepository stationRepo;
    @Autowired private StateRepository stateRepo;

    private Client client;
    private Bike bike;
    private Station station;
    private State available;
    private State inUse;
    private State maintenance;

    @BeforeEach
    void setUp() {
        // States
        available = new State();
        available.setDescription("AVAILABLE");
        inUse = new State();
        inUse.setDescription("IN_USE");
        maintenance = new State();
        maintenance.setDescription("MAINTENANCE");
        stateRepo.save(available);
        stateRepo.save(inUse);
        stateRepo.save(maintenance);

        // Client
        Subscription s = new Subscription("teste");
        subrepo.save(s);
        client = new Client();
        client.setName("Alice");
        client.setEmail("alice@example.com");
        client.setTrips(new ArrayList<>());
        client.setSubscription(s);
        userRepo.save(client);

        // Station
        station = new Station();
        station.setMaxDocks(13);
        station.setName("Central Station");
        stationRepo.save(station);

        // Bike
        bike = new Bike();
        bike.setModel("Model X");
        bike.setState(available);
        bike.setStation(station);
        bikeRepo.save(bike);
    }

    @Test
    void testStartTripSuccess() {
        TripModDTO dto = new TripModDTO(bike.getId(), client.getId());
        TripDTO tripDTO = tripService.startTrip(dto);

        assertNotNull(tripDTO);
        assertEquals(client.getId(), tripDTO.userId());
        assertEquals(bike.getModel(), tripDTO.bikeModel());
        assertEquals(station.getName(), tripDTO.startStation());
        assertNotNull(tripDTO.startTime());

        Bike updatedBike = bikeRepo.findById(bike.getId()).orElseThrow();
        assertEquals("IN_USE", updatedBike.getState().getDescription());
    }

    @Test
    void testStartTripBikeNotAvailable() {
        // Set bike to maintenance
        bike.setState(maintenance);
        bikeRepo.save(bike);

        TripModDTO dto = new TripModDTO(bike.getId(), client.getId());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> tripService.startTrip(dto));

        assertEquals("Bike is not available", ex.getMessage());
    }

    @Test
    void testStartTripUserNotFound() {
        TripModDTO dto = new TripModDTO(bike.getId(), 999L);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> tripService.startTrip(dto));

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void testStartTripBikeNotFound() {
        TripModDTO dto = new TripModDTO(999L, client.getId());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> tripService.startTrip(dto));

        assertEquals("Bike not found", ex.getMessage());
    }

    @Test
    void testEndTripSuccess() {
        TripModDTO startDto = new TripModDTO(bike.getId(), client.getId());
        TripDTO startedTrip = tripService.startTrip(startDto);

        EndTripDTO endDto = new EndTripDTO(station.getId());
        TripDTO endedTrip = tripService.endTrip(startedTrip.id(), endDto);

        assertNotNull(endedTrip);
        assertNotNull(endedTrip.endTime());
        assertEquals(station.getName(), endedTrip.endStation());

        Bike updatedBike = bikeRepo.findById(bike.getId()).orElseThrow();
        assertEquals("AVAILABLE", updatedBike.getState().getDescription());
        assertEquals(station.getId(), updatedBike.getStation().getId());
    }

    @Test
    void testEndTripAlreadyEnded() {
        // Start a trip first
        TripModDTO startDto = new TripModDTO(bike.getId(), client.getId());
        TripDTO startedTrip = tripService.startTrip(startDto);

        EndTripDTO endDto = new EndTripDTO(station.getId());
        tripService.endTrip(startedTrip.id(), endDto);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> tripService.endTrip(startedTrip.id(), endDto));

        assertEquals("This trip has already ended.", ex.getMessage());
    }

    @Test
    void testEndTripStationFull() {
        // Start a trip first
        TripModDTO startDto = new TripModDTO(bike.getId(), client.getId());
        TripDTO startedTrip = tripService.startTrip(startDto);

        // Create a full station
        Station fullStation = new Station();
        fullStation.setName("Full Station");
        fullStation.setMaxDocks(0); // No available docks
        stationRepo.save(fullStation);

        EndTripDTO endDto = new EndTripDTO(fullStation.getId());
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> tripService.endTrip(startedTrip.id(), endDto));

        assertEquals("Station is full.", ex.getMessage());
    }

    @Test
    void testGetTripsByUserEmpty() {
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> tripService.getTripsByUser(999L));
        assertEquals("No trips found for that user", ex.getMessage());
    }

    @Test
    void testGetAllTrips() {
        // Start a trip
        TripModDTO dto = new TripModDTO(bike.getId(), client.getId());
        tripService.startTrip(dto);

        List<TripDTO> trips = tripService.getAllTrips();
        assertEquals(1, trips.size());
        assertEquals(client.getId(), trips.get(0).userId());
    }

    @Test
    void testGetTripsByBike() {
        TripModDTO dto = new TripModDTO(bike.getId(), client.getId());
        tripService.startTrip(dto);

        List<TripDTO> trips = tripService.getTripsByBike(bike.getId());
        assertEquals(1, trips.size());
        assertEquals(bike.getModel(), trips.get(0).bikeModel());
    }

    @Test
    void testGetTripsByBikeAndUser() {
        TripModDTO dto = new TripModDTO(bike.getId(), client.getId());
        tripService.startTrip(dto);

        List<TripDTO> trips = tripService.getTripsByBikeAndUser(bike.getId(), client.getId());
        assertEquals(1, trips.size());
        assertEquals(bike.getModel(), trips.get(0).bikeModel());
        assertEquals(client.getId(), trips.get(0).userId());
    }
}
