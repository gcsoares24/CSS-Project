package pt.ul.fc.css.urbanwheels.mapper;

import org.junit.jupiter.api.Test;
import pt.ul.fc.css.urbanwheels.dto.MaintenanceDTO;
import pt.ul.fc.css.urbanwheels.dto.MaintenanceRequestDTO;
import pt.ul.fc.css.urbanwheels.entities.Admin;
import pt.ul.fc.css.urbanwheels.entities.Bike;
import pt.ul.fc.css.urbanwheels.entities.Maintenance;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class MaintenanceMapperUnitTest {

    @Test
    void testToDTO() {
        // given
        Bike bike = new Bike();
        bike.setId(1L);

        Admin admin = new Admin();
        admin.setId(2L);

        Maintenance m = new Maintenance();
        m.setId(3L);
        m.setBike(bike);
        m.setAdmin(admin);
        m.setDate(LocalDateTime.of(2025, 11, 23, 12, 0));
        m.setDescription("Check brakes");
        m.setCost(15.5);

        // when
        MaintenanceDTO dto = MaintenanceMapper.toDTO(m);

        // then
        assertNotNull(dto);
        assertEquals(3L, dto.id());
        assertEquals(1L, dto.bikeId());
        assertEquals(2L, dto.adminId());
        assertEquals(LocalDateTime.of(2025, 11, 23, 12, 0), dto.date());
        assertEquals("Check brakes", dto.description());
        assertEquals(15.5, dto.cost());
    }

    @Test
    void testToDTO_null() {
        assertNull(MaintenanceMapper.toDTO(null));
    }

    @Test
    void testToDTOFromMR() {
        // given
        MaintenanceRequestDTO req = new MaintenanceRequestDTO(2L, "Repair chain", 20.0);
        Long bikeId = 1L;

        // when
        MaintenanceDTO dto = MaintenanceMapper.toDTOFromMR(req, bikeId);

        // then
        assertNotNull(dto);
        assertNull(dto.id());
        assertEquals(1L, dto.bikeId());
        assertEquals(2L, dto.adminId());
        assertEquals("Repair chain", dto.description());
        assertEquals(20.0, dto.cost());
        assertNotNull(dto.date());
    }

    @Test
    void testToDTOFromMR_nullInput() {
        assertNull(MaintenanceMapper.toDTOFromMR(null, 1L));
        assertNull(MaintenanceMapper.toDTOFromMR(new MaintenanceRequestDTO(1L, "desc", 5), null));
    }

    @Test
    void testToEntity() {
        // given
        MaintenanceDTO dto = new MaintenanceDTO(
                3L,
                1L,
                2L,
                LocalDateTime.of(2025, 11, 23, 12, 0),
                "Check tires",
                10.0
        );

        // when
        Maintenance m = MaintenanceMapper.toEntity(dto);

        // then
        assertNotNull(m);
        assertEquals(3L, m.getId());
        assertNotNull(m.getBike());
        assertEquals(1L, m.getBike().getId());
        assertNotNull(m.getAdmin());
        assertEquals(2L, m.getAdmin().getId());
        assertEquals(LocalDateTime.of(2025, 11, 23, 12, 0), m.getDate());
        assertEquals("Check tires", m.getDescription());
        assertEquals(10.0, m.getCost());
    }

    @Test
    void testToEntity_null() {
        assertNull(MaintenanceMapper.toEntity(null));
    }
}
