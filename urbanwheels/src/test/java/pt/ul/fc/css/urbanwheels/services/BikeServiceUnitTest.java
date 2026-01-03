package pt.ul.fc.css.urbanwheels.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pt.ul.fc.css.urbanwheels.dto.BikeDTO;
import pt.ul.fc.css.urbanwheels.dto.StateDTO;
import pt.ul.fc.css.urbanwheels.entities.Bike;
import pt.ul.fc.css.urbanwheels.entities.State;
import pt.ul.fc.css.urbanwheels.entities.Station;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.urbanwheels.repository.BikeRepository;
import pt.ul.fc.css.urbanwheels.repository.MaintenanceRepository;
import pt.ul.fc.css.urbanwheels.repository.StateRepository;
import pt.ul.fc.css.urbanwheels.repository.StationRepository;
import pt.ul.fc.css.urbanwheels.repository.UserRepository;

class BikeServiceUnitTest {

    @Mock
    private BikeRepository bikeRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private StateRepository stateRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @InjectMocks
    private BikeService bikeService;

    private Station station;
    private State availableState;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Sample Station
        station = new Station();
        station.setId(1L);
        station.setName("Central Station");
        station.setBikes(new ArrayList<>());
        station.setMaxDocks(12);

        // Sample State
        availableState = new State();
        availableState.setId(1);               // ID is now set
        availableState.setDescription("AVAILABLE");
    }

    @Test
    void testAddBikeToStation() {
        BikeDTO inputDto = new BikeDTO(null, "Model X", new StateDTO(1L, "AVAILABLE"), station.getId());

        when(stationRepository.findById(1L)).thenReturn(Optional.of(station));
        when(stateRepository.findByDescription("AVAILABLE")).thenReturn(Optional.of(availableState));
        when(bikeRepository.save(any(Bike.class))).thenAnswer(invocation -> {
            Bike bike = invocation.getArgument(0);
            bike.setId(1L);                     // assign ID when saving
            return bike;
        });

        BikeDTO created = bikeService.addBikeToStation(inputDto);

        assertNotNull(created);
        assertEquals(1L, created.state().id());
        assertEquals("AVAILABLE", created.state().description());
        assertEquals("Model X", created.model());

        verify(bikeRepository, times(1)).save(any(Bike.class));
    }

    @Test
    void testGetBikesByStateThrowsWhenEmpty() {
        when(stateRepository.findByDescription("AVAILABLE")).thenReturn(Optional.of(availableState));
        when(bikeRepository.findByState(availableState)).thenReturn(List.of());

        assertThrows(ResourceNotFoundException.class, () -> bikeService.getBikesByState("AVAILABLE"));
    }

    @Test
    void testGetAllBikesThrowsWhenEmpty() {
        when(bikeRepository.findAll()).thenReturn(List.of());
        assertThrows(ResourceNotFoundException.class, () -> bikeService.getAllBikes());
    }

    @Test
    void testGetBikesByStateSuccess() {
        Bike bike1 = new Bike();
        bike1.setId(1L);
        bike1.setModel("Model A");
        bike1.setState(availableState);
        bike1.setStation(station);

        Bike bike2 = new Bike();
        bike2.setId(2L);
        bike2.setModel("Model B");
        bike2.setState(availableState);
        bike2.setStation(station);

        when(stateRepository.findByDescription("AVAILABLE")).thenReturn(Optional.of(availableState));
        when(bikeRepository.findByState(availableState)).thenReturn(List.of(bike1, bike2));

        List<BikeDTO> bikes = bikeService.getBikesByState("AVAILABLE");

        assertEquals(2, bikes.size());
        assertTrue(bikes.stream().anyMatch(b -> b.model().equals("Model A")));
        assertTrue(bikes.stream().anyMatch(b -> b.model().equals("Model B")));
    }
}
