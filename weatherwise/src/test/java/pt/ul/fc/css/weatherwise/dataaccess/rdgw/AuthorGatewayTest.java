package pt.ul.fc.css.weatherwise.dataaccess.rdgw;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ul.fc.css.weatherwise.dataaccess.DataSource;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.entities.Author;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

class AuthorGatewayTest {

    private static AuthorGateway authorGateway;
    private static final String TEST_AUTHOR_NAME = "TestAuthor_JUnit";
    private static final String NON_EXISTENT_NAME = "NonExistentAuthor_123";

    static {
        System.setProperty("db.url", "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "sa");
    }

    @BeforeAll
    static void setUp() {
        authorGateway = new AuthorGateway();
        DataSource dataSource = DataSource.INSTANCE;

        try {
            dataSource.connect();

            executeSQLFile("_schema.sql");
            executeSQLFile("data.sql");

        } catch (PersistenceException e) {
            System.err.println("FATAL: Could not setup the database for testing.");
            e.printStackTrace();
            fail("FATAL: Could not setup the database for testing: " + e.getMessage());
        }
    }
    private static void executeSQLFile(String resourcePath) {
        try (InputStream in = LocationRowGateway.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (in == null) {
                fail("Resource not found: " + resourcePath);
            }

            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            String[] statements = sql.split(";");

            try (Connection conn = DataSource.INSTANCE.getConnection();
                 Statement stmt = conn.createStatement()) {

                for (String statement : statements) {
                    String trimmed = statement.trim();
                    if (!trimmed.isEmpty()) {
                        stmt.execute(trimmed);
                    }
                }
            }
        } catch (Exception e) {
            fail("Error executing SQL resource: " + resourcePath + "\n" + e.getMessage());
        }
    }

    @AfterEach
    void tearDown() {
        try {
            String deleteSql = String.format("DELETE FROM Author WHERE name = '%s';", TEST_AUTHOR_NAME);
            DataSource.INSTANCE.executeSQLUpdate(deleteSql);
        } catch (PersistenceException e) {
            e.printStackTrace();
            System.err.println("WARNING: Could not clean up test author data: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------
    //                             TESTS
    // -------------------------------------------------------------------

    /**
     * Tests that findByName returns null when the author does not exist.
     */
    @Test
    void testFindByName_NotFound() throws PersistenceException {
        Author author = authorGateway.findByName(NON_EXISTENT_NAME);

        assertNull(author, "Should return null for a non-existent author name.");
    }

    /**
     * Tests that findByName returns the correct Author object when the author exists.
     * We rely on the findOrCreateByName to safely insert the test data first.
     */
    @Test
    void testFindByName_Found() throws PersistenceException {
        Author insertedAuthor = authorGateway.findOrCreateByName(TEST_AUTHOR_NAME);
        assertNotNull(insertedAuthor, "Pre-condition failed: Test author must be created.");

        Author foundAuthor = authorGateway.findByName(TEST_AUTHOR_NAME);

        assertNotNull(foundAuthor, "Should find the previously inserted author.");
        assertEquals(TEST_AUTHOR_NAME, foundAuthor.getName(), "The name of the retrieved author must match.");
        assertTrue(foundAuthor.getId() > 0, "The retrieved author must have a valid ID.");
    }

    /**
     * Tests the 'create' part of the logic: inserting a new author.
     */
    @Test
    void testFindOrCreateByName_CreatesNewAuthor() throws PersistenceException {
        Author preCheck = authorGateway.findByName(TEST_AUTHOR_NAME);
        assertNull(preCheck, "Pre-condition failed: Author should not exist before test starts.");

        Author newAuthor = authorGateway.findOrCreateByName(TEST_AUTHOR_NAME);

        assertNotNull(newAuthor, "A new Author object should be returned.");
        assertEquals(TEST_AUTHOR_NAME, newAuthor.getName(), "The name should be set correctly.");
        assertTrue(newAuthor.getId() > 0, "A new ID should have been generated.");

        Author verification = authorGateway.findByName(TEST_AUTHOR_NAME);
        assertNotNull(verification, "The author must be persistent in the database.");
        assertEquals(newAuthor.getId(), verification.getId(), "Verification ID must match the created ID.");
    }

    /**
     * Tests the 'find' part of the logic: retrieving an existing author.
     */
    @Test
    void testFindOrCreateByName_FindsExistingAuthor() throws PersistenceException {
        Author initialAuthor = authorGateway.findOrCreateByName(TEST_AUTHOR_NAME);
        assertNotNull(initialAuthor, "Pre-condition failed: Initial author must be created.");
        int initialId = initialAuthor.getId();
        
        Author foundAuthor = authorGateway.findOrCreateByName(TEST_AUTHOR_NAME);

        assertNotNull(foundAuthor, "An existing Author object should be returned.");
        assertEquals(initialId, foundAuthor.getId(), 
                     "The ID of the found author must match the ID of the initial author (no new row created).");
        assertEquals(initialAuthor.getName(), foundAuthor.getName(), "The name must remain the same.");
    }
}