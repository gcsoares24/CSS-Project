package pt.ul.fc.css.urbanwheels.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ul.fc.css.urbanwheels.dto.StateDTO;
import pt.ul.fc.css.urbanwheels.services.StateService;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StateControllerUnitTest {

    private StateService stateService;
    private StateController stateController;

    @BeforeEach
    void setup() {
        stateService = mock(StateService.class);
        stateController = new StateController(stateService);
    }

    @Test
    void testGetAllBikeStates_nonEmpty() {
        StateDTO s1 = new StateDTO(1L, "AVAILABLE");
        StateDTO s2 = new StateDTO(2L, "IN_USE");

        when(stateService.getAllStates()).thenReturn(List.of(s1, s2));

        ResponseEntity<List<StateDTO>> response = stateController.getAllBikeStates();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("AVAILABLE", response.getBody().get(0).description());
        assertEquals("IN_USE", response.getBody().get(1).description());
    }

    @Test
    void testGetAllBikeStates_empty() {
        when(stateService.getAllStates()).thenReturn(new ArrayList<>());

        ResponseEntity<List<StateDTO>> response = stateController.getAllBikeStates();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testCreateBikeState_success() {
        StateDTO created = new StateDTO(5L, "MAINTENANCE");

        when(stateService.createState("MAINTENANCE")).thenReturn(created);

        ResponseEntity<StateDTO> response = stateController.createBikeState("MAINTENANCE");

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(5L, response.getBody().id());
        assertEquals("MAINTENANCE", response.getBody().description());
    }

    @Test
    void testCreateBikeState_nullDescription() {
        // simulate service throwing exception if description is null
        when(stateService.createState(null)).thenThrow(new IllegalArgumentException("Description cannot be null"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            stateController.createBikeState(null);
        });

        assertEquals("Description cannot be null", ex.getMessage());
    }

    @Test
    void testGetAllBikeStates_serviceThrowsException() {
        when(stateService.getAllStates()).thenThrow(new ResourceNotFoundException("No states found"));

        assertThrows(ResourceNotFoundException.class, () -> {
            stateController.getAllBikeStates();
        });
    }
}
