package pt.ul.fc.css.urbanwheels.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class AdminUnitTest {

    @Test
    void testNoArgsConstructorAndGettersSetters() {
        // given
        Admin admin = new Admin();

        // inicializa lista de manutenção manualmente
        List<Maintenance> maints = new ArrayList<>();
        Maintenance m1 = new Maintenance();
        m1.setDescription("Checkup");
        m1.setCost(50.0);
        maints.add(m1);

        // when
        admin.setMaintenances(maints);

        // then
        assertNotNull(admin.getMaintenances());
        assertEquals(1, admin.getMaintenances().size());
        assertEquals("Checkup", admin.getMaintenances().get(0).getDescription());
        assertEquals(50.0, admin.getMaintenances().get(0).getCost());
    }

    @Test
    void testParameterizedConstructor() {
        // given
        Admin admin = new Admin("alice@email.com", "Alice");

        // then
        assertNotNull(admin.getMaintenances()); // lista inicializada
        assertEquals("Alice", admin.getName());
        assertEquals("alice@email.com", admin.getEmail());
    }

    @Test
    void testSetAndGetEmailAndName() {
        Admin admin = new Admin();
        admin.setEmail("bob@email.com");
        admin.setName("Bob");

        assertEquals("Bob", admin.getName());
        assertEquals("bob@email.com", admin.getEmail());
    }

    @Test
    void testMaintenancesListManipulation() {
        Admin admin = new Admin();
        Maintenance m1 = new Maintenance();
        m1.setDescription("Repair");
        admin.getMaintenances().add(m1);

        assertEquals(1, admin.getMaintenances().size());
        assertEquals("Repair", admin.getMaintenances().get(0).getDescription());

        admin.getMaintenances().clear();
        assertTrue(admin.getMaintenances().isEmpty());
    }

    @Test
    void testMaintenancesCannotBeNullAfterClear() {
        Admin admin = new Admin();
        admin.getMaintenances().clear(); // limpar lista
        assertNotNull(admin.getMaintenances()); // lista nunca é null
    }
}
