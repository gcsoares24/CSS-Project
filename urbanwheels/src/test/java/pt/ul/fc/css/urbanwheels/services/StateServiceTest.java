package pt.ul.fc.css.urbanwheels.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pt.ul.fc.css.urbanwheels.dto.StateDTO;
import pt.ul.fc.css.urbanwheels.entities.State;
import pt.ul.fc.css.urbanwheels.repository.StateRepository;

@DataJpaTest
class StateServiceTest {

    private StateService stateService;

    @Autowired
    private StateRepository stateRepository;

    @Test
    void testGetAllStates() {
        stateService = new StateService(stateRepository);

        // Pre-insert some states
        State s1 = new State();
        s1.setDescription("AVAILABLE");
        State s2 = new State();
        s2.setDescription("IN_USE");
        stateRepository.save(s1);
        stateRepository.save(s2);

        List<StateDTO> states = stateService.getAllStates();

        assertEquals(2, states.size());
        assertTrue(states.stream().anyMatch(s -> s.description().equals("AVAILABLE")));
        assertTrue(states.stream().anyMatch(s -> s.description().equals("IN_USE")));
    }

    @Test
    void testCreateStateNew() {
        stateService = new StateService(stateRepository);

        StateDTO created = stateService.createState("MAINTENANCE");

        assertNotNull(created);
        assertEquals("MAINTENANCE", created.description());

        // Ensure it's persisted in DB
        assertTrue(stateRepository.findByDescription("MAINTENANCE").isPresent());
    }

    @Test
    void testCreateStateAlreadyExists() {
        stateService = new StateService(stateRepository);

        // Pre-insert state
        State s = new State();
        s.setDescription("AVAILABLE");
        stateRepository.save(s);

        StateDTO dto = stateService.createState("AVAILABLE");

        assertNotNull(dto);
        assertEquals("AVAILABLE", dto.description());

        // Only one instance in DB
        assertEquals(1, stateRepository.findAll().size());
    }

    @Test
    void testCreateStateInvalidDescription() {
        stateService = new StateService(stateRepository);

        assertThrows(IllegalArgumentException.class, () -> stateService.createState(""));
        assertThrows(IllegalArgumentException.class, () -> stateService.createState(null));
    }
}
