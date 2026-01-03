package pt.ul.fc.css.urbanwheels.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pt.ul.fc.css.urbanwheels.entities.State;

@DataJpaTest
class StateRepositoryTest {

    @Autowired
    private StateRepository stateRepository;

    @Test
    void testFindByDescription_existingState() {
        // Arrange: create a state and save it
        State available = new State();
        available.setDescription("AVAILABLE");
        stateRepository.save(available);

        // Act: retrieve it by description
        Optional<State> found = stateRepository.findByDescription("AVAILABLE");

        // Assert: verify it exists and matches
        assertTrue(found.isPresent());
        assertEquals("AVAILABLE", found.get().getDescription());
    }

    @Test
    void testFindByDescription_nonExistingState() {
        // Act: try to find a state that does not exist
        Optional<State> found = stateRepository.findByDescription("NON_EXISTENT");

        // Assert: should be empty
        assertFalse(found.isPresent());
    }
}
