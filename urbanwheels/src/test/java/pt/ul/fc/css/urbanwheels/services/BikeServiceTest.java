package pt.ul.fc.css.urbanwheels.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import pt.ul.fc.css.urbanwheels.dto.BikeDTO;
import pt.ul.fc.css.urbanwheels.dto.MaintenanceDTO;
import pt.ul.fc.css.urbanwheels.dto.StateDTO;
import pt.ul.fc.css.urbanwheels.entities.Admin;
import pt.ul.fc.css.urbanwheels.entities.Bike;
import pt.ul.fc.css.urbanwheels.entities.State;
import pt.ul.fc.css.urbanwheels.entities.Station;
import pt.ul.fc.css.urbanwheels.entities.Subscription;
import pt.ul.fc.css.urbanwheels.entities.Client;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.urbanwheels.repository.*;

@SpringBootTest
@Transactional
class BikeServiceTest {

    @Autowired
    private BikeRepository bikeRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MaintenanceRepository maintenanceRepository;

    private BikeService bikeService;

    private Station station;
    private State availableState;
    private Admin admin;

    @BeforeEach
    void setUp() {
        bikeService = new BikeService(bikeRepository, stationRepository, stateRepository, userRepository, maintenanceRepository);

        // Create test data
        station = new Station();
        station.setName("Central");
        station.setLat(0.0);
        station.setLon(0.0);
        station.setMaxDocks(10);
        stationRepository.save(station);


        availableState = new State();
        availableState.setDescription("AVAILABLE");
        stateRepository.save(availableState);

        admin = new Admin("Admin Name", "admin@test.com");
        userRepository.save(admin);
    }

    @Test
    void testAddBikeToStation() {
        // Wrap the State entity in a StateDTO
        StateDTO stateDTO = new StateDTO(null, availableState.getDescription());

        BikeDTO bikeDTO = new BikeDTO(null, "Model X", stateDTO, station.getId());
        BikeDTO created = bikeService.addBikeToStation(bikeDTO);

        assertNotNull(created);
        assertEquals("Model X", created.model());
        assertEquals("AVAILABLE", created.state().description());
    }


    @Test
    void testGetBikesByState() {
        Bike bike1 = new Bike();
        bike1.setModel("Bike1");
        bike1.setState(availableState);
        bike1.setStation(station);
        bikeRepository.save(bike1);

        List<BikeDTO> bikes = bikeService.getBikesByState("AVAILABLE");
        assertEquals(1, bikes.size());
        assertEquals("Bike1", bikes.get(0).model());
    }

    @Test
    void testGetAllBikes() {
        Bike bike1 = new Bike();
        bike1.setModel("Bike1");
        bike1.setState(availableState);
        bike1.setStation(station);
        bikeRepository.save(bike1);

        Bike bike2 = new Bike();
        bike2.setModel("Bike2");
        bike2.setState(availableState);
        bike2.setStation(station);
        bikeRepository.save(bike2);

        List<BikeDTO> bikes = bikeService.getAllBikes();
        assertEquals(2, bikes.size());
    }

    @Test
    void testChangeBikeState() {
        Bike bike = new Bike();
        bike.setModel("Bike1");
        bike.setState(availableState);
        bike.setStation(station);
        bikeRepository.save(bike);

        State maintenanceState = new State();
        maintenanceState.setDescription("MAINTENANCE");
        stateRepository.save(maintenanceState);

        BikeDTO updated = bikeService.changeBikeState(bike.getId(), "MAINTENANCE");
        assertEquals("MAINTENANCE", updated.state().description());
    }

    @Test
    void testRegisterMaintenance() {
        Bike bike = new Bike();
        bike.setModel("Bike1");
        bike.setState(availableState);
        bike.setStation(station);
        bikeRepository.save(bike);

        MaintenanceDTO maintenanceDTO = new MaintenanceDTO(null, bike.getId(), admin.getId(), LocalDateTime.now(), "Fix brakes", 12);
        MaintenanceDTO saved = bikeService.registerMaintenance(maintenanceDTO);

        assertNotNull(saved);
        assertEquals("Fix brakes", saved.description());
    }

    @Test
    void testGetBikesByState_notFound() {
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> {
            bikeService.getBikesByState("NON_EXISTENT");
        });
        assertEquals("State not found", ex.getMessage());
    }
}
