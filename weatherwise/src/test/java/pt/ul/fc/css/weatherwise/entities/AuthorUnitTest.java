package pt.ul.fc.css.weatherwise.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AuthorUnitTest {

	@Test
    void testConstructorAndGetters() {
        Author author = new Author(1, "Alice");
        
        assertEquals(1, author.getId(), "ID should match the value passed to the constructor");
        assertEquals("Alice", author.getName(), "Name should match the value passed to the constructor");
    }

    @Test
    void testDefaultConstructorAndSetters() {
        Author author = new Author();

        author.setId(42);
        author.setName("Bob");

        assertEquals(42, author.getId(), "ID should be correctly set via setter");
        assertEquals("Bob", author.getName(), "Name should be correctly set via setter");
    }

    @Test
    void testMutability() {
        Author author = new Author(5, "Carol");

        author.setId(10);
        author.setName("David");

        assertEquals(10, author.getId(), "ID should update correctly");
        assertEquals("David", author.getName(), "Name should update correctly");
    }
    
}
