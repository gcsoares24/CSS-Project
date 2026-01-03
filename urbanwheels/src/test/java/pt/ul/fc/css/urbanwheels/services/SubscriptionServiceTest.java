package pt.ul.fc.css.urbanwheels.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pt.ul.fc.css.urbanwheels.dto.SubscriptionDTO;
import pt.ul.fc.css.urbanwheels.entities.Subscription;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.urbanwheels.repository.SubscriptionRepository;

@DataJpaTest
class SubscriptionServiceTest {

    private SubscriptionService subscriptionService;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    void testGetAllSubscriptions() {
        subscriptionService = new SubscriptionService(subscriptionRepository);

        // Pre-insert subscriptions
        Subscription s1 = new Subscription();
        s1.setName("Monthly");
        Subscription s2 = new Subscription();
        s2.setName("Annual");

        subscriptionRepository.save(s1);
        subscriptionRepository.save(s2);

        List<SubscriptionDTO> subs = subscriptionService.getAllSubscriptions();

        assertEquals(2, subs.size());
        assertTrue(subs.stream().anyMatch(s -> s.name().equals("Monthly")));
        assertTrue(subs.stream().anyMatch(s -> s.name().equals("Annual")));
    }

    @Test
    void testGetAllSubscriptionsEmpty() {
        subscriptionService = new SubscriptionService(subscriptionRepository);

        subscriptionRepository.deleteAll();

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> subscriptionService.getAllSubscriptions());

        assertEquals("No subscriptions found", ex.getMessage());
    }

    @Test
    void testCreateSubscriptionNew() {
        subscriptionService = new SubscriptionService(subscriptionRepository);

        SubscriptionDTO dto = subscriptionService.createSubscription("Weekly");

        assertNotNull(dto);
        assertEquals("Weekly", dto.name());

        assertTrue(subscriptionRepository.findByName("Weekly").isPresent());
    }

    @Test
    void testCreateSubscriptionAlreadyExists() {
        subscriptionService = new SubscriptionService(subscriptionRepository);

        Subscription s = new Subscription();
        s.setName("Monthly");
        subscriptionRepository.save(s);

        SubscriptionDTO dto = subscriptionService.createSubscription("Monthly");

        assertNotNull(dto);
        assertEquals("Monthly", dto.name());

        // Only one instance in DB
        assertEquals(1, subscriptionRepository.findAll().size());
    }

    @Test
    void testCreateSubscriptionInvalidName() {
        subscriptionService = new SubscriptionService(subscriptionRepository);

        assertThrows(IllegalArgumentException.class, () -> subscriptionService.createSubscription(""));
        assertThrows(IllegalArgumentException.class, () -> subscriptionService.createSubscription(null));
    }
}
