package pt.ul.fc.css.urbanwheels.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class StateUnitTest {

    @Test
    void testDefaultConstructor() {
        State state = new State();

        // campos iniciais devem ser null
        assertNull(state.getId());
        assertNull(state.getDescription());
    }

    @Test
    void testSettersAndGetters() {
        State state = new State();

        state.setId(1);
        assertEquals(1, state.getId());

        state.setDescription("Available");
        assertEquals("Available", state.getDescription());
    }

    @Test
    void testManipulation() {
        State state = new State();

        state.setId(5);
        state.setDescription("Under Maintenance");

        assertAll("Check all fields",
            () -> assertEquals(5, state.getId()),
            () -> assertEquals("Under Maintenance", state.getDescription())
        );
    }
}
