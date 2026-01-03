package pt.ul.fc.css.urbanwheels.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pt.ul.fc.css.urbanwheels.dto.*;

import pt.ul.fc.css.urbanwheels.services.UserService;

import java.time.LocalDateTime;
import java.util.List;

class UserControllerUnitTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setup() {
        userService = mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void testCreateClient() {
        CreateClientDTO dto = new CreateClientDTO("Alice", "alice@example.com", "ANUAL");
        UserDTO userDTO = new UserDTO(1L, "Alice", "alice@example.com", "CLIENT", null);

        when(userService.createUser(any())).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.createClient(dto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testCreateAdmin() {
        CreateAdminDTO dto = new CreateAdminDTO("Bob", "bob@example.com");
        UserDTO userDTO = new UserDTO(2L, "Bob", "bob@example.com", "ADMIN", null);

        when(userService.createUser(any())).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.createAdmin(dto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testGetAllUsers() {
    	UserDetailsDTO user1 = new UserDetailsDTO(
    	        1L,
    	        "Alice",
    	        "alice@example.com",
    	        "CLIENT",
    	        LocalDateTime.now(),
    	        List.of() // sem atividades
    	);

    	UserDetailsDTO user2 = new UserDetailsDTO(
    	        2L,
    	        "Bob",
    	        "bob@example.com",
    	        "ADMIN_ACCOUNT",
    	        LocalDateTime.now(),
    	        List.of() // sem atividades
    	);

        when(userService.getAllUsers()).thenReturn(List.of(user1, user2));

        ResponseEntity<List<UserDetailsDTO>> response = userController.getAllUsers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(user1, response.getBody().get(0));
        assertEquals(user2, response.getBody().get(1));
    }

    @Test
    void testGetDetailsById() {
        long userId = 1L;
        UserDetailsDTO details = new UserDetailsDTO(userId, "Alice", "alice@example.com", "ANUAL", null, List.of());

        when(userService.getUserDetails(userId)).thenReturn(details);

        ResponseEntity<UserDetailsDTO> response = userController.getDetailsById(userId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(details, response.getBody());
    }
}
