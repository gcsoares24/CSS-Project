package pt.ul.fc.css.urbanwheels.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pt.ul.fc.css.urbanwheels.dto.StateDTO;
import pt.ul.fc.css.urbanwheels.entities.State;
import pt.ul.fc.css.urbanwheels.repository.StateRepository;

class StateServiceUnitTest {

    @Mock
    private StateRepository stateRepository;

    private StateService stateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stateService = new StateService(stateRepository); // constructor injection
    }

    @Test
    void testGetAllStates() {
        State s1 = new State();
        s1.setId(1);                     // Set ID to avoid NPE
        s1.setDescription("AVAILABLE");

        State s2 = new State();
        s2.setId(2);                     // Set ID to avoid NPE
        s2.setDescription("IN_USE");

        when(stateRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<StateDTO> states = stateService.getAllStates();

        assertEquals(2, states.size());
        assertTrue(states.stream().anyMatch(s -> s.description().equals("AVAILABLE")));
        assertTrue(states.stream().anyMatch(s -> s.description().equals("IN_USE")));

        verify(stateRepository, times(1)).findAll();
    }

    @Test
    void testCreateStateNew() {
        State s = new State();
        s.setId(3);                       // Set ID to avoid NPE
        s.setDescription("MAINTENANCE");

        when(stateRepository.findByDescription("MAINTENANCE")).thenReturn(Optional.empty());
        when(stateRepository.save(any(State.class))).thenReturn(s);

        StateDTO created = stateService.createState("MAINTENANCE");

        assertNotNull(created);
        assertEquals("MAINTENANCE", created.description());
        assertEquals(3L, created.id());   // Ensure ID is correctly mapped

        verify(stateRepository, times(1)).findByDescription("MAINTENANCE");
        verify(stateRepository, times(1)).save(any(State.class));
    }

    @Test
    void testCreateStateAlreadyExists() {
        State s = new State();
        s.setId(4);                     // Set ID to avoid NPE
        s.setDescription("AVAILABLE");

        when(stateRepository.findByDescription("AVAILABLE")).thenReturn(Optional.of(s));

        StateDTO dto = stateService.createState("AVAILABLE");

        assertNotNull(dto);
        assertEquals("AVAILABLE", dto.description());
        assertEquals(4L, dto.id());

        // save should not be called because state already exists
        verify(stateRepository, times(1)).findByDescription("AVAILABLE");
        verify(stateRepository, never()).save(any());
    }

    @Test
    void testCreateStateInvalidDescription() {
        assertThrows(IllegalArgumentException.class, () -> stateService.createState(""));
        assertThrows(IllegalArgumentException.class, () -> stateService.createState(null));

        verifyNoInteractions(stateRepository);
    }
}
