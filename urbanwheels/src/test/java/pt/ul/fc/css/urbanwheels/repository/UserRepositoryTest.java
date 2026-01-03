package pt.ul.fc.css.urbanwheels.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pt.ul.fc.css.urbanwheels.entities.Client;
import pt.ul.fc.css.urbanwheels.entities.Subscription;
import pt.ul.fc.css.urbanwheels.entities.User;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    void testExistsByEmail_existingEmail() {
        Subscription sub = new Subscription();
        sub.setName("Monthly");
        subscriptionRepository.save(sub);

        Client client = new Client("alice@example.com", "Alice", sub);
        userRepository.save(client);

        boolean exists = userRepository.existsByEmail("alice@example.com");

        assertTrue(exists);
    }

    @Test
    void testExistsByEmail_nonExistingEmail() {
        boolean exists = userRepository.existsByEmail("bob@example.com");

        assertFalse(exists);
    }
    
    @Test
    void testFindByEmail_existingEmail() {
        Subscription sub = new Subscription();
        sub.setName("Monthly");
        subscriptionRepository.save(sub);

        Client client = new Client("alice@example.com", "Alice", sub);
        userRepository.save(client);

        Optional<User> result = userRepository.findByEmail("alice@example.com");

        assertTrue(result.isPresent());
        assertEquals("alice@example.com", result.get().getEmail());
        assertTrue(result.get() instanceof Client);
    }

    @Test
    void testFindByEmail_nonExistingEmail() {

        Optional<User> result = userRepository.findByEmail("notfound@example.com");

        // Assert
        assertTrue(result.isEmpty());
    }
}
