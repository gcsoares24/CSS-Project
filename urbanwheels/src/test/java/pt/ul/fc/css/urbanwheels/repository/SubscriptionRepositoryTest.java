package pt.ul.fc.css.urbanwheels.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pt.ul.fc.css.urbanwheels.entities.Subscription;

@DataJpaTest
class SubscriptionRepositoryTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    void testFindByName_existingSubscription() {
        // Arrange: create a subscription and save it
        Subscription sub = new Subscription();
        sub.setName("Monthly Pass");
        subscriptionRepository.save(sub);

        // Act: retrieve it by name
        Optional<Subscription> found = subscriptionRepository.findByName("Monthly Pass");

        // Assert: verify it exists and matches
        assertTrue(found.isPresent());
        assertEquals("Monthly Pass", found.get().getName());
    }

    @Test
    void testFindByName_nonExistingSubscription() {
        // Act: try to find a subscription that does not exist
        Optional<Subscription> found = subscriptionRepository.findByName("Yearly Pass");

        // Assert: should be empty
        assertFalse(found.isPresent());
    }
}
