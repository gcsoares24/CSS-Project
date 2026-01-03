package pt.ul.fc.css.urbanwheels.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pt.ul.fc.css.urbanwheels.entities.Bike;
import pt.ul.fc.css.urbanwheels.entities.State;

@DataJpaTest
class BikeRepositoryTest {

    @Autowired
    private BikeRepository bikeRepository;

    @Autowired
    private StateRepository stateRepository;

    @Test
    void testFindByState() {

        // Create and save States
        State available = new State();
        available.setDescription("AVAILABLE");
        stateRepository.save(available);

        State unavailable = new State();
        unavailable.setDescription("UNAVAILABLE");
        stateRepository.save(unavailable);

        // Bikes
        Bike bike1 = new Bike();
        bike1.setModel("A");
        bike1.setState(available);

        Bike bike2 = new Bike();
        bike2.setModel("B");
        bike2.setState(unavailable);

        Bike bike3 = new Bike();
        bike3.setModel("C");
        bike3.setState(available);

        bikeRepository.save(bike1);
        bikeRepository.save(bike2);
        bikeRepository.save(bike3);

        // Test query
        List<Bike> availableBikes = bikeRepository.findByState(available);

        assertEquals(2, availableBikes.size());
        assertTrue(availableBikes.stream().allMatch(b -> 
            b.getState().getDescription().equals("AVAILABLE")
        ));
    }
}

