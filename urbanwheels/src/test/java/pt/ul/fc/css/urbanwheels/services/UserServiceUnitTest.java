package pt.ul.fc.css.urbanwheels.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pt.ul.fc.css.urbanwheels.dto.CreateUserDTO;
import pt.ul.fc.css.urbanwheels.dto.UserDTO;
import pt.ul.fc.css.urbanwheels.dto.UserDetailsDTO;
import pt.ul.fc.css.urbanwheels.entities.Admin;
import pt.ul.fc.css.urbanwheels.entities.Client;
import pt.ul.fc.css.urbanwheels.entities.Subscription;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.urbanwheels.repository.SubscriptionRepository;
import pt.ul.fc.css.urbanwheels.repository.UserRepository;

class UserServiceUnitTest {

    private UserRepository userRepo;
    private SubscriptionRepository subRepo;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepo = mock(UserRepository.class);
        subRepo = mock(SubscriptionRepository.class);
        userService = new UserService(userRepo, subRepo);
    }

    @Test
    void testGetAllUsers() {
        // Criar mocks de users
        Client client = new Client();
        client.setId(1L);
        client.setEmail("c@c.com");

        Admin admin = new Admin();
        admin.setId(2L);
        admin.setEmail("a@a.com");

        // Mock do repositório
        when(userRepo.findAll()).thenReturn(List.of(client, admin));
        when(userRepo.findById(1L)).thenReturn(Optional.of(client));
        when(userRepo.findById(2L)).thenReturn(Optional.of(admin));

        // Chamar o método
        List<UserDetailsDTO> dtos = userService.getAllUsers();

        // Verificação simples apenas do tamanho
        assertEquals(2, dtos.size());
    }

    
    @Test
    void testGetAllUsersEmpty() {
        when(userRepo.findAll()).thenReturn(List.of());
        assertThrows(ResourceNotFoundException.class, () -> userService.getAllUsers());
    }

    @Test
    void testCreateUserClientSuccess() {
        // Corrected DTO order and subscription spelling
        CreateUserDTO dto = new CreateUserDTO("Alice", "alice@example.com", "CLIENT", "STANDARD");
        Subscription sub = new Subscription();
        sub.setName("STANDARD");

        when(subRepo.findByName("STANDARD")).thenReturn(Optional.of(sub));
        when(userRepo.existsByEmail("alice@example.com")).thenReturn(false);
        when(userRepo.save(any(Client.class))).thenAnswer(i -> i.getArguments()[0]);

        UserDTO created = userService.createUser(dto);
        assertEquals("alice@example.com", created.email());
    }

    @Test
    void testCreateUserAdminSuccess() {
        // Corrected DTO order
        CreateUserDTO dto = new CreateUserDTO("Bob", "bob@example.com", "ADMIN", null);
        when(userRepo.existsByEmail("bob@example.com")).thenReturn(false);
        when(userRepo.save(any(Admin.class))).thenAnswer(i -> i.getArguments()[0]);

        UserDTO created = userService.createUser(dto);
        assertEquals("bob@example.com", created.email());
    }

    @Test
    void testCreateUserAlreadyExists() {
        CreateUserDTO dto = new CreateUserDTO("Bob", "bob@example.com", "ADMIN", null);
        when(userRepo.existsByEmail("bob@example.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.createUser(dto));
    }

    @Test
    void testGetUserDetailsClient() {
        Client client = new Client();
        client.setId(1L);
        client.setEmail("c@c.com");
        client.setTrips(new ArrayList<>());
        Subscription sub = new Subscription(); sub.setName("PREMIUM");
        client.setSubscription(sub);

        when(userRepo.findById(1L)).thenReturn(Optional.of(client));

        var details = userService.getUserDetails(1L);
        assertEquals("PREMIUM", details.subscriptionType());
    }

    @Test
    void testGetUserDetailsAdmin() {
        Admin admin = new Admin();
        admin.setId(2L);
        admin.setEmail("a@a.com");
        admin.setMaintenances(new ArrayList<>());

        when(userRepo.findById(2L)).thenReturn(Optional.of(admin));

        var details = userService.getUserDetails(2L);
        assertEquals("ADMIN_ACCOUNT", details.subscriptionType());
    }
    
    @Test
    void validateAdminEmail_whenAdminExists() {
        String email = "admin@example.com";
        Admin admin = new Admin();
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(admin));

        assertDoesNotThrow(() -> userService.loginAdmin(email));
    }

    @Test
    void validateAdminEmail_whenUserDoesNotExist() {
        String email = "notfound@example.com";
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.loginAdmin(email));
        assertEquals("Admin not found", exception.getMessage());
    }

    @Test
    void validateAdminEmail_whenUserIsNotAdmin() {
        String email = "user@example.com";
        Client normalUser = new Client();
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(normalUser));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.loginAdmin(email));
        assertEquals("User is not an admin", exception.getMessage());
    }
}