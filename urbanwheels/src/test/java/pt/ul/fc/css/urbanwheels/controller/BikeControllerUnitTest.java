package pt.ul.fc.css.urbanwheels.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ul.fc.css.urbanwheels.dto.AddBikeToFleetDTO;
import pt.ul.fc.css.urbanwheels.dto.BikeDTO;
import pt.ul.fc.css.urbanwheels.dto.MaintenanceDTO;
import pt.ul.fc.css.urbanwheels.dto.MaintenanceRequestDTO;
import pt.ul.fc.css.urbanwheels.dto.StateDTO;
import pt.ul.fc.css.urbanwheels.services.BikeService;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BikeControllerUnitTest {

    private BikeService bikeService;
    private BikeController bikeController;

    @BeforeEach
    void setup() {
        bikeService = mock(BikeService.class);
        bikeController = new BikeController(bikeService);
    }

    @Test
    void testCreateBike_success() {
        AddBikeToFleetDTO req = new AddBikeToFleetDTO("Mountain", 1L);
        BikeDTO bikeDto = new BikeDTO(10L, "Mountain", new StateDTO(1L, "AVAILABLE"), 1L);

        when(bikeService.addBikeToStation(any(BikeDTO.class))).thenReturn(bikeDto);

        BikeDTO response = bikeController.createBike(req).getBody();

        assertNotNull(response);
        assertEquals(10L, response.id());
        assertEquals("Mountain", response.model());
        assertEquals("AVAILABLE", response.state().description());
        assertEquals(1L, response.stationId());
    }

    @Test
    void testListBikes_noStateFilter() {
        BikeDTO bike1 = new BikeDTO(1L, "City", new StateDTO(1L, "AVAILABLE"), 1L);
        BikeDTO bike2 = new BikeDTO(2L, "Mountain", new StateDTO(2L, "IN_USE"), 1L);

        when(bikeService.getAllBikes()).thenReturn(List.of(bike1, bike2));

        List<BikeDTO> response = bikeController.listBikes(null).getBody();

        assertNotNull(response);
        assertEquals(2, response.size());
    }

    @Test
    void testListBikes_withStateFilter() {
        BikeDTO bike1 = new BikeDTO(1L, "City", new StateDTO(1L, "AVAILABLE"), 1L);

        when(bikeService.getBikesByState("AVAILABLE")).thenReturn(List.of(bike1));

        List<BikeDTO> response = bikeController.listBikes("AVAILABLE").getBody();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals("AVAILABLE", response.get(0).state().description());
    }

    @Test
    void testChangeBikeState_success() {
        BikeDTO bikeDto = new BikeDTO(1L, "City", new StateDTO(2L, "IN_USE"), 1L);

        when(bikeService.changeBikeState(1L, "IN_USE")).thenReturn(bikeDto);

        BikeDTO response = bikeController.changeBikeState(1L, "IN_USE").getBody();

        assertNotNull(response);
        assertEquals("IN_USE", response.state().description());
    }

    @Test
    void testRegisterMaintenance_success() {
        MaintenanceRequestDTO req = new MaintenanceRequestDTO(1L, "Fix brakes", 50.0);
        MaintenanceDTO saved = new MaintenanceDTO(1L, 10L, 1L, LocalDateTime.now(), "Fix brakes", 50.0);

        when(bikeService.registerMaintenance(any(MaintenanceDTO.class))).thenReturn(saved);

        MaintenanceDTO response = bikeController.registerMaintenance(10L, req).getBody();

        assertNotNull(response);
        assertEquals("Fix brakes", response.description());
        assertEquals(50.0, response.cost());
        assertEquals(10L, response.bikeId());
        assertEquals(1L, response.adminId());
    }

    @Test
    void testListBikes_empty_throwsException() {
        when(bikeService.getAllBikes()).thenThrow(new ResourceNotFoundException("No bikes found."));

        assertThrows(ResourceNotFoundException.class, () -> bikeController.listBikes(null));
    }

    @Test
    void testGetBikesByState_empty_throwsException() {
        when(bikeService.getBikesByState("AVAILABLE")).thenThrow(new ResourceNotFoundException("No bikes found."));

        assertThrows(ResourceNotFoundException.class, () -> bikeController.listBikes("AVAILABLE"));
    }
}
