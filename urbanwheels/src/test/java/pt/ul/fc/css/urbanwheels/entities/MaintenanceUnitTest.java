package pt.ul.fc.css.urbanwheels.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class MaintenanceUnitTest {

    @Test
    void testDefaultConstructor() {
        Maintenance m = new Maintenance();

        // campos obrigatórios não inicializados devem ser null ou default
        assertNull(m.getId());
        assertNull(m.getBike());
        assertNull(m.getAdmin());

        assertNotNull(m.getDate()); // inicializado com LocalDateTime.now()
        assertEquals(0.0, m.getCost());

        assertNull(m.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        Maintenance m = new Maintenance();

        Bike bike = new Bike();
        bike.setId(1L);
        m.setBike(bike);
        assertEquals(bike, m.getBike());

        Admin admin = new Admin();
        admin.setId(2L);
        m.setAdmin(admin);
        assertEquals(admin, m.getAdmin());

        LocalDateTime now = LocalDateTime.now();
        m.setDate(now);
        assertEquals(now, m.getDate());

        m.setDescription("Troca de corrente");
        assertEquals("Troca de corrente", m.getDescription());

        m.setCost(25.5);
        assertEquals(25.5, m.getCost());
        
        m.setId(100L);
        assertEquals(100L, m.getId());
    }

    @Test
    void testManipulation() {
        Maintenance m = new Maintenance();

        Bike bike = new Bike();
        Admin admin = new Admin();
        LocalDateTime date = LocalDateTime.now();
        String description = "Revisão geral";
        double cost = 50.0;

        m.setBike(bike);
        m.setAdmin(admin);
        m.setDate(date);
        m.setDescription(description);
        m.setCost(cost);

        assertAll("Check all fields",
            () -> assertEquals(bike, m.getBike()),
            () -> assertEquals(admin, m.getAdmin()),
            () -> assertEquals(date, m.getDate()),
            () -> assertEquals(description, m.getDescription()),
            () -> assertEquals(cost, m.getCost())
        );
    }
}
