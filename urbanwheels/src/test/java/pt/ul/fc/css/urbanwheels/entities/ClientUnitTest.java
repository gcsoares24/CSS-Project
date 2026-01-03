package pt.ul.fc.css.urbanwheels.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ClientUnitTest {

    @Test
    void testDefaultConstructor() {
        Client client = new Client();

        // listas devem estar inicializadas (não nulas)
        assertNotNull(client.getTrips());
        assertTrue(client.getTrips().isEmpty());

        // subscription deve ser null
        assertNull(client.getSubscription());
    }

    @Test
    void testParameterizedConstructor() {
        Subscription subscription = new Subscription("ANUAL");
        Client client = new Client("user@example.com", "User", subscription);

        assertEquals("User", client.getName());
        assertEquals("user@example.com", client.getEmail());
        assertEquals(subscription, client.getSubscription());

        // a lista trips deve estar inicializada mesmo no construtor parametrizado
        assertNotNull(client.getTrips());
        assertTrue(client.getTrips().isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        Client client = new Client();

        Subscription subscription = new Subscription("OCASIONAL");
        client.setSubscription(subscription);
        assertEquals(subscription, client.getSubscription());

        List<Trip> trips = new ArrayList<>();
        client.setTrips(trips);
        assertEquals(trips, client.getTrips());
    }

    @Test
    void testTripsListManipulation() {
        Client client = new Client();

        // mesmo que não seja estritamente necessário, podemos garantir que a lista é manipulável
        assertDoesNotThrow(() -> {
            Trip t1 = new Trip();
            Trip t2 = new Trip();
            client.getTrips().add(t1);
            client.getTrips().add(t2);
        });

        assertEquals(2, client.getTrips().size());
    }
}
