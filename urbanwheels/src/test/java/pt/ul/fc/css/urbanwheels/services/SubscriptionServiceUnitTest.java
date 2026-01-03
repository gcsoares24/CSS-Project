package pt.ul.fc.css.urbanwheels.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pt.ul.fc.css.urbanwheels.dto.SubscriptionDTO;
import pt.ul.fc.css.urbanwheels.entities.Subscription;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.urbanwheels.repository.SubscriptionRepository;

class SubscriptionServiceUnitTest {

    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subscriptionService = new SubscriptionService(subscriptionRepository);
    }

    @Test
    void testGetAllSubscriptions() {
        Subscription s1 = new Subscription();
        s1.setName("Monthly");
        Subscription s2 = new Subscription();
        s2.setName("Annual");

        when(subscriptionRepository.findAll()).thenReturn(List.of(s1, s2));

        List<SubscriptionDTO> result = subscriptionService.getAllSubscriptions();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(dto -> dto.name().equals("Monthly")));
        assertTrue(result.stream().anyMatch(dto -> dto.name().equals("Annual")));
    }

    @Test
    void testGetAllSubscriptionsEmpty() {
        when(subscriptionRepository.findAll()).thenReturn(new ArrayList<>());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> subscriptionService.getAllSubscriptions());

        assertEquals("No subscriptions found", ex.getMessage());
    }

    @Test
    void testCreateSubscriptionNew() {
        String name = "Weekly";
        Subscription saved = new Subscription();
        saved.setName(name);

        when(subscriptionRepository.findByName(name)).thenReturn(Optional.empty());
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(saved);

        SubscriptionDTO dto = subscriptionService.createSubscription(name);

        assertNotNull(dto);
        assertEquals(name, dto.name());
        verify(subscriptionRepository).save(any(Subscription.class));
    }

    @Test
    void testCreateSubscriptionAlreadyExists() {
        String name = "Monthly";
        Subscription existing = new Subscription();
        existing.setName(name);

        when(subscriptionRepository.findByName(name)).thenReturn(Optional.of(existing));

        SubscriptionDTO dto = subscriptionService.createSubscription(name);

        assertNotNull(dto);
        assertEquals(name, dto.name());
        verify(subscriptionRepository, never()).save(any(Subscription.class));
    }

    @Test
    void testCreateSubscriptionInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> subscriptionService.createSubscription(""));
        assertThrows(IllegalArgumentException.class, () -> subscriptionService.createSubscription(null));
    }
}
