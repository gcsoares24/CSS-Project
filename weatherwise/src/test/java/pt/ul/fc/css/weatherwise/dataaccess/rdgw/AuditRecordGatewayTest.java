package pt.ul.fc.css.weatherwise.dataaccess.rdgw;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.ul.fc.css.weatherwise.dataaccess.DataSource;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;
import pt.ul.fc.css.weatherwise.entities.AuditRecord;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuditRecordGatewayTest {

    private static AuditRecordGateway auditGateway;
    private Instant testStartTime; 

    static {
        System.setProperty("db.url", "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "sa");
    }
    // --- SETUP & TEARDOWN ---


    @BeforeAll
    static void setUp() {
        auditGateway = new AuditRecordGateway();
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
    
    @BeforeEach
    void setTestStartTime() {
        this.testStartTime = Instant.now();
    }
    /**
     * Reads and executes .sql files     */
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
        Instant testEndTime = Instant.now();
        
        Timestamp startTimestamp = Timestamp.from(testStartTime);
        Timestamp endTimestamp = Timestamp.from(testEndTime);

        String deleteSql = String.format(
            "DELETE FROM AuditLog WHERE query_timestamp >= '%s' AND query_timestamp <= '%s';", 
            startTimestamp.toString(), 
            endTimestamp.toString()
        );

        try {
            System.out.println("Cleaning up records inserted between: " + startTimestamp + " and " + endTimestamp);
            DataSource.INSTANCE.executeSQLUpdate(deleteSql);
        } catch (PersistenceException e) {
            e.printStackTrace();
            System.err.println("WARNING: Could not clean up test data: " + e.getMessage());
        }
    }


    // -------------------------------------------------------------------
    //                             TESTS
    // -------------------------------------------------------------------


    /**
     * Tests the core functionality of registering a query and ensuring it doesn't throw an exception.
     */
    @Test
    void testRegisterQuery_successful() {
        int authorId = 1;
        int locationId = 2;
        String queryType = "FORECAST_SEARCH";
        
        assertDoesNotThrow(() -> 
            auditGateway.registerQuery(authorId, locationId, queryType)
        );
    }

    /**
     * Tests that a registered record can be correctly read back using findAll.
     */
    @Test
    void testRegisterAndFindAll() throws PersistenceException {
        // ARRANGE
        int authorId1 = 1;
        int locationId1 = 5;
        String queryType1 = "TEMP_CHECK";

        int authorId2 = 2;
        int locationId2 = 6;
        String queryType2 = "PRECIPITATION_CHECK";
        
        List<AuditRecord> logsBefore = auditGateway.findAll();

        auditGateway.registerQuery(authorId1, locationId1, queryType1);
        auditGateway.registerQuery(authorId2, locationId2, queryType2); 

        List<AuditRecord> logs = auditGateway.findAll();
        
        assertNotNull(logs, "The list of audit logs should not be null.");
        assertEquals(logsBefore.size() + 2, logs.size(), "Two audit records should have been retrieved in addition to pre-existing data.");
        
        AuditRecord firstLog = logs.get(0);
        assertEquals(authorId2, firstLog.getAuthorId(), "The first log (most recent) should match the second insertion's author ID.");
        
        AuditRecord secondLog = logs.get(1);
        assertEquals(authorId1, secondLog.getAuthorId(), "The second log should match the first insertion's author ID.");
    }
    
    /**
     * Tests the find all method returns a list, even when empty or containing pre-existing data.
     */
    @Test
    void testFindAll_doesNotFail() throws PersistenceException {
        List<AuditRecord> logs = auditGateway.findAll();
        
        assertNotNull(logs, "The list should not be null.");
    }
}