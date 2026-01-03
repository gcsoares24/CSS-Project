package pt.ul.fc.css.urbanwheels.mapper;

import org.junit.jupiter.api.Test;
import pt.ul.fc.css.urbanwheels.dto.CreateClientDTO;
import pt.ul.fc.css.urbanwheels.dto.CreateUserDTO;
import pt.ul.fc.css.urbanwheels.entities.Client;
import pt.ul.fc.css.urbanwheels.entities.Subscription;

import static org.junit.jupiter.api.Assertions.*;

class ClientMapperUnitTest {

    @Test
    void testToEntity_valid() {
        // given
        CreateUserDTO dto = new CreateUserDTO("Alice", "alice@example.com", "CLIENT", "ANUAL");
        Subscription subscription = new Subscription("ANUAL");

        // when
        Client client = ClientMapper.toEntity(dto, subscription);

        // then
        assertNotNull(client);
        assertEquals("Alice", client.getName());
        assertEquals("alice@example.com", client.getEmail());
        assertEquals(subscription, client.getSubscription());
        assertNotNull(client.getTrips());
        assertTrue(client.getTrips().isEmpty()); // novo cliente não tem viagens
    }

    @Test
    void testToEntity_nullDTO() {
        Subscription subscription = new Subscription("ANUAL");

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            ClientMapper.toEntity(null, subscription);
        });
        assertEquals("DTO não pode ser null", e.getMessage());
    }

    @Test
    void testToEntity_nullSubscription() {
        CreateUserDTO dto = new CreateUserDTO("Alice", "alice@example.com", "CLIENT", "ANUAL");

        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            ClientMapper.toEntity(dto, null);
        });
        assertEquals("Subscription não pode ser null", e.getMessage());
    }

    @Test
    void testFromCreateClientDTO_valid() {
        // given
        CreateClientDTO clientDTO = new CreateClientDTO("Bob", "bob@example.com", "ANUAL");

        // when
        CreateUserDTO dto = ClientMapper.fromCreateClientDTO(clientDTO);

        // then
        assertNotNull(dto);
        assertEquals("Bob", dto.name());
        assertEquals("bob@example.com", dto.email());
        assertEquals("CLIENT", dto.userType());
        assertEquals("ANUAL", dto.subscriptionType());
    }

    @Test
    void testFromCreateClientDTO_null() {
        CreateUserDTO dto = ClientMapper.fromCreateClientDTO(null);
        assertNull(dto);
    }

    @Test
    void testToEntity_largeTripsList() {
        // given
        CreateUserDTO dto = new CreateUserDTO("Charlie", "charlie@example.com", "CLIENT", "OCASIONAL");
        Subscription subscription = new Subscription("OCASIONAL");

        // when
        Client client = ClientMapper.toEntity(dto, subscription);

        // then
        assertNotNull(client.getTrips());
        assertTrue(client.getTrips().isEmpty()); // sempre inicializado
    }
}
