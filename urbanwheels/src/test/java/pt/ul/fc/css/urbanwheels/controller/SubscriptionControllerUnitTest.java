package pt.ul.fc.css.urbanwheels.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ul.fc.css.urbanwheels.dto.SubscriptionDTO;
import pt.ul.fc.css.urbanwheels.services.SubscriptionService;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SubscriptionControllerUnitTest {

    private SubscriptionService subscriptionService;
    private SubscriptionController subscriptionController;

    @BeforeEach
    void setup() {
        subscriptionService = mock(SubscriptionService.class);
        subscriptionController = new SubscriptionController(subscriptionService);
    }

    @Test
    void testListSubscriptions_nonEmpty() {
        SubscriptionDTO s1 = new SubscriptionDTO(1L, "ANUAL");
        SubscriptionDTO s2 = new SubscriptionDTO(2L, "MENSAL");

        when(subscriptionService.getAllSubscriptions()).thenReturn(List.of(s1, s2));

        ResponseEntity<List<SubscriptionDTO>> response = subscriptionController.listSubscriptions();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("ANUAL", response.getBody().get(0).name());
        assertEquals("MENSAL", response.getBody().get(1).name());
    }

    @Test
    void testListSubscriptions_empty() {
        when(subscriptionService.getAllSubscriptions()).thenReturn(new ArrayList<>());

        ResponseEntity<List<SubscriptionDTO>> response = subscriptionController.listSubscriptions();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testListSubscriptions_serviceThrowsException() {
        when(subscriptionService.getAllSubscriptions())
            .thenThrow(new ResourceNotFoundException("No subscriptions found"));

        assertThrows(ResourceNotFoundException.class, () -> {
            subscriptionController.listSubscriptions();
        });
    }

    @Test
    void testCreateSubscription_success() {
        SubscriptionDTO created = new SubscriptionDTO(5L, "VIP");

        when(subscriptionService.createSubscription("VIP")).thenReturn(created);

        ResponseEntity<SubscriptionDTO> response = subscriptionController.createSubscription("VIP");

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(5L, response.getBody().id());
        assertEquals("VIP", response.getBody().name());
    }

    @Test
    void testCreateSubscription_nullName() {
        when(subscriptionService.createSubscription(null))
            .thenThrow(new IllegalArgumentException("Subscription name cannot be null"));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            subscriptionController.createSubscription(null);
        });

        assertEquals("Subscription name cannot be null", ex.getMessage());
    }
}
