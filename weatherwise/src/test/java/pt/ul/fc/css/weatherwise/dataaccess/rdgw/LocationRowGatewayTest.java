package pt.ul.fc.css.weatherwise.dataaccess.rdgw;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ul.fc.css.weatherwise.dataaccess.DataSource;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.entities.Location;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class LocationRowGatewayTest {

    private static LocationRowGateway locationGateway;

    static {
        System.setProperty("db.url", "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "sa");
    }

    @BeforeAll
    static void setUp() {
        locationGateway = new LocationRowGateway();
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



    // -------------------------------------------------------------------
    //                             TESTS
    // -------------------------------------------------------------------

    @Test
    void testGetByNameExistingLocation() {
        try {
            Location location = locationGateway.getByName("Lisboa");
            assertNotNull(location, "Location should not be null for an existing name");
            assertEquals("Lisboa", location.getName(), "Location name mismatch");
            assertTrue(location.getId() > 0, "Location ID should be positive");
        } catch (PersistenceException e) {
            fail("PersistenceException thrown: " + e.getMessage());
        }
    }

    @Test
    void testGetByNameNonExistentLocation() {
        try {
            Location location = locationGateway.getByName("CidadeInexistente");
            assertNull(location, "Location should be null for a non-existent name");
        } catch (PersistenceException e) {
            fail("PersistenceException thrown: " + e.getMessage());
        }
    }

    @Test
    void testGetByNameNull() {
        try {
            Location location = locationGateway.getByName(null);
            assertNull(location, "Location should be null when searching for null");
        } catch (PersistenceException e) {
            fail("PersistenceException thrown unexpectedly: " + e.getMessage());
        }
    }
    
}
