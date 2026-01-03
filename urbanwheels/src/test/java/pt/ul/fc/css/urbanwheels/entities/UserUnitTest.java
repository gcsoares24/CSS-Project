package pt.ul.fc.css.urbanwheels.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UserUnitTest {

    @Test
    void testDefaultConstructorAndGetters() {
        // Instância anônima de User, já que é abstrata
        User user = new User() {};

        // Inicialmente, id, email e name devem ser null, createdAt é inicializado
        assertNull(user.getId());
        assertNull(user.getEmail());
        assertNull(user.getName());
        assertNotNull(user.getCreatedAt());
    }

    @Test
    void testParameterizedConstructor() {
        User user = new User("email@domain.com", "Jane Doe") {};

        // Verifica se os valores do construtor são corretamente atribuídos
        assertEquals("email@domain.com", user.getEmail());
        assertEquals("Jane Doe", user.getName());
        assertNotNull(user.getCreatedAt());

        // id deve ser null até ser setado
        assertNull(user.getId());
    }

    @Test
    void testSetters() {
        User user = new User() {};

        // Usando setters para id, email e name
        user.setId(42L);
        user.setEmail("test@example.com");
        user.setName("Test User");

        assertAll("Check setters",
            () -> assertEquals(42L, user.getId()),
            () -> assertEquals("test@example.com", user.getEmail()),
            () -> assertEquals("Test User", user.getName())
        );
    }

    @Test
    void testSetNullValues() {
        User user = new User() {};

        // Podemos definir email e name para null sem erros
        user.setEmail(null);
        user.setName(null);

        assertNull(user.getEmail());
        assertNull(user.getName());
    }

    @Test
    void testCreatedAtInitialization() {
        User user1 = new User() {};
        User user2 = new User() {};

        // Cada instância deve ter createdAt inicializado de forma diferente
        assertNotNull(user1.getCreatedAt());
        assertNotNull(user2.getCreatedAt());
        assertNotEquals(user1.getCreatedAt(), user2.getCreatedAt());
    }
}
