package pt.ul.fc.css.urbanwheels.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import pt.ul.fc.css.urbanwheels.dto.CreateUserDTO;
import pt.ul.fc.css.urbanwheels.dto.UserDTO;
import pt.ul.fc.css.urbanwheels.dto.UserDetailsDTO;
import pt.ul.fc.css.urbanwheels.entities.Admin;
import pt.ul.fc.css.urbanwheels.entities.Client;
import pt.ul.fc.css.urbanwheels.entities.Subscription;
import pt.ul.fc.css.urbanwheels.repository.SubscriptionRepository;
import pt.ul.fc.css.urbanwheels.repository.UserRepository;

@DataJpaTest
@Import(UserService.class)
class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepo;
    @Autowired private SubscriptionRepository subRepo;

    private Subscription sub;

    @BeforeEach
    void setUp() {
        sub = new Subscription();
        sub.setName("STANDARD");
        subRepo.save(sub);
    }

    @Test
    void testCreateClientIntegration() {
        // Corrected order: name, email, userType, subscriptionType
        CreateUserDTO dto = new CreateUserDTO("Alice", "alice@example.com", "CLIENT", "STANDARD");
        UserDTO userDTO = userService.createUser(dto);

        assertNotNull(userDTO.id());
        assertEquals("alice@example.com", userDTO.email());

        Client client = (Client) userRepo.findById(userDTO.id()).orElseThrow();
        assertEquals("STANDARD", client.getSubscription().getName());
    }

    @Test
    void testCreateAdminIntegration() {
        // Corrected order
        CreateUserDTO dto = new CreateUserDTO("Bob", "bob@example.com", "ADMIN", null);
        UserDTO userDTO = userService.createUser(dto);

        Admin admin = (Admin) userRepo.findById(userDTO.id()).orElseThrow();
        assertEquals("Bob", admin.getName());
    }

    @Test
    void testGetAllUsersIntegration() {
        userService.createUser(new CreateUserDTO("Alice", "alice@example.com", "CLIENT", "STANDARD"));
        userService.createUser(new CreateUserDTO("Bob", "bob@example.com", "ADMIN", null));

        List<UserDetailsDTO> users = userService.getAllUsers();
        assertEquals(2, users.size());
    }

    @Test
    void testGetUserDetailsIntegration() {
        UserDTO dto = userService.createUser(new CreateUserDTO("Alice", "alice@example.com", "CLIENT", "STANDARD"));
        var details = userService.getUserDetails(dto.id());

        assertEquals("STANDARD", details.subscriptionType());
    }
}
