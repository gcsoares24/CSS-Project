package pt.ul.fc.css.urbanwheels.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class SubscriptionUnitTest {

    @Test
    void testDefaultConstructor() {
        Subscription subscription = new Subscription();

        assertNull(subscription.getId());
        assertNull(subscription.getName());
    }

    @Test
    void testParameterizedConstructor() {
        Subscription subscription = new Subscription("Premium");

        assertNull(subscription.getId()); // id não definido no construtor
        assertEquals("Premium", subscription.getName());
    }

    @Test
    void testSettersAndGetters() {
        Subscription subscription = new Subscription();

        subscription.setId(1L);
        subscription.setName("Standard");

        assertAll("Check all fields",
            () -> assertEquals(1L, subscription.getId()),
            () -> assertEquals("Standard", subscription.getName())
        );
    }
}
