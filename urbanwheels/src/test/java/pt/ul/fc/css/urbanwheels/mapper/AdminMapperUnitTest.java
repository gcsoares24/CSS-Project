package pt.ul.fc.css.urbanwheels.mapper;

import org.junit.jupiter.api.Test;
import pt.ul.fc.css.urbanwheels.dto.CreateAdminDTO;
import pt.ul.fc.css.urbanwheels.dto.CreateUserDTO;
import pt.ul.fc.css.urbanwheels.entities.Admin;

import static org.junit.jupiter.api.Assertions.*;

class AdminMapperUnitTest {

    @Test
    void testToEntity_valid() {
        // given
        CreateUserDTO dto = new CreateUserDTO("Alice", "alice@example.com", "ADMIN", null);

        // when
        Admin admin = AdminMapper.toEntity(dto);

        // then
        assertNotNull(admin);
        assertEquals("Alice", admin.getName());
        assertEquals("alice@example.com", admin.getEmail());
        assertNotNull(admin.getMaintenances());
        assertTrue(admin.getMaintenances().isEmpty());
    }

    @Test
    void testToEntity_nullDTO() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> {
            AdminMapper.toEntity(null);
        });
        assertEquals("DTO não pode ser null", e.getMessage());
    }

    @Test
    void testFromCreateAdminDTO_valid() {
        // given
        CreateAdminDTO adminDTO = new CreateAdminDTO("Bob", "bob@example.com");

        // when
        CreateUserDTO dto = AdminMapper.fromCreateAdminDTO(adminDTO);

        // then
        assertNotNull(dto);
        assertEquals("Bob", dto.name());
        assertEquals("bob@example.com", dto.email());
        assertEquals("ADMIN", dto.userType());
        assertNull(dto.subscriptionType());
    }

    @Test
    void testFromCreateAdminDTO_null() {
        CreateUserDTO dto = AdminMapper.fromCreateAdminDTO(null);
        assertNull(dto);
    }

    @Test
    void testToEntity_largeMaintenancesList() {
        // given
        CreateUserDTO dto = new CreateUserDTO("Charlie", "charlie@example.com", "ADMIN", null);

        // when
        Admin admin = AdminMapper.toEntity(dto);

        // then
        assertNotNull(admin.getMaintenances());
        assertTrue(admin.getMaintenances().isEmpty()); // sempre inicializado
    }
}
