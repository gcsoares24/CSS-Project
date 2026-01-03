package pt.ul.fc.css.urbanwheels.mapper;

import org.junit.jupiter.api.Test;
import pt.ul.fc.css.urbanwheels.dto.UserDTO;
import pt.ul.fc.css.urbanwheels.dto.UserDetailsDTO;
import pt.ul.fc.css.urbanwheels.entities.Client;
import pt.ul.fc.css.urbanwheels.entities.Admin;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperUnitTest {

    @Test
    void testToDTO_client() {
        // given
        Client client = new Client();
        client.setId(1L);
        client.setName("Alice");
        client.setEmail("alice@example.com");

        // when
        UserDTO dto = UserMapper.toDTO(client);

        // then
        assertNotNull(dto);
        assertEquals(1L, dto.id());
        assertEquals("Alice", dto.name());
        assertEquals("alice@example.com", dto.email());
        assertEquals("CLIENT", dto.userType());
        assertNotNull(dto.createdAt());
    }

    @Test
    void testToDTO_admin() {
        // given
        Admin admin = new Admin();
        admin.setId(2L);
        admin.setName("Bob");
        admin.setEmail("bob@example.com");

        // when
        UserDTO dto = UserMapper.toDTO(admin);

        // then
        assertNotNull(dto);
        assertEquals(2L, dto.id());
        assertEquals("Bob", dto.name());
        assertEquals("bob@example.com", dto.email());
        assertEquals("ADMIN", dto.userType());
        assertNotNull(dto.createdAt());
    }

    @Test
    void testToDetailsDTO_client() {
        // given
        Client client = new Client();
        client.setId(1L);
        client.setName("Alice");
        client.setEmail("alice@example.com");

        List<String> trips = List.of("Trip1", "Trip2");

        // when
        UserDetailsDTO dto = UserMapper.toDetailsDTO(client, "ANUAL", trips);

        // then
        assertNotNull(dto);
        assertEquals(1L, dto.id());
        assertEquals("Alice", dto.name());
        assertEquals("alice@example.com", dto.email());
        assertEquals("ANUAL", dto.subscriptionType());
        assertNotNull(dto.createdAt());
        assertEquals(trips, dto.activities());
    }

    @Test
    void testToDetailsDTO_admin() {
        // given
        Admin admin = new Admin();
        admin.setId(2L);
        admin.setName("Bob");
        admin.setEmail("bob@example.com");

        List<String> maintenances = List.of("Maintenance1");

        // when
        UserDetailsDTO dto = UserMapper.toDetailsDTO(admin, null, maintenances);

        // then
        assertNotNull(dto);
        assertEquals(2L, dto.id());
        assertEquals("Bob", dto.name());
        assertEquals("bob@example.com", dto.email());
        assertNull(dto.subscriptionType()); // Admin não tem subscription
        assertNotNull(dto.createdAt());
        assertEquals(maintenances, dto.activities());
    }

    @Test
    void testToDTO_nullUser() {
        assertThrows(NullPointerException.class, () -> {
            UserMapper.toDTO(null); // como o método não lida com null, lança NPE
        });
    }
}
