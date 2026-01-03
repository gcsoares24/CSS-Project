package pt.ul.fc.css.weatherwise.dataaccess.rdgw;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pt.ul.fc.css.weatherwise.dataaccess.DataSource;
import pt.ul.fc.css.weatherwise.entities.WeatherCondition;
import pt.ul.fc.css.weatherwise.entities.WeatherRecord;
import pt.ul.fc.css.weatherwise.dataaccess.exception.PersistenceException;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WeatherRecordGatewayTest {
    private static WeatherRecordGateway weatherRecordGateway;
    private final int TEST_LOCATION_ID = 1;
    private final LocalDate TEST_DATE = LocalDate.of(2050, 1, 1);
    private final Date TEST_SQL_DATE = Date.valueOf(TEST_DATE);


    static {
        System.setProperty("db.url", "jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
        System.setProperty("db.user", "sa");
        System.setProperty("db.password", "sa");
    }
    
    @BeforeAll
    static void setUp() {
        weatherRecordGateway = new WeatherRecordGateway(); 
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



    // -------------------------------------------------------------------
    //                             TESTS
    // -------------------------------------------------------------------


//    H2 NÃO SUPORTA UPSERT(UPDATE+INSERT APARTIR DE ON CONFLICT)
    
    //@Test
//    void testAddForecast_InsertNewRecord() {
//        int locationId = TEST_LOCATION_ID;
//        WeatherCondition initialCondition = WeatherCondition.RAIN;
//        double initialTemp = 10.5;
//
//        Integer generatedId = weatherRecordGateway.addForecast(locationId, TEST_SQL_DATE, initialCondition, initialTemp);
//
//        assertNotNull(generatedId, "A non-null ID should be returned upon successful insertion.");
//        
//        WeatherRecord insertedRecord = weatherRecordGateway.getWeatherByLocationAndDate(locationId, TEST_SQL_DATE);
//        assertNotNull(insertedRecord, "The record should be retrievable after insertion.");
//        assertEquals(initialCondition, insertedRecord.getCondition(), "Condition should match initial insertion.");
//        assertEquals(initialTemp, insertedRecord.getTemperature(), 0.1, "Temperature should match initial insertion.");
//    }
//
//    @Test
//    void testAddForecast_UpdateExistingRecord() {
//        int locationId = TEST_LOCATION_ID;
//        WeatherCondition initialCondition = WeatherCondition.RAIN;
//        double initialTemp = 10.5;
//        
//        WeatherCondition updatedCondition = WeatherCondition.SUN;
//        double updatedTemp = 25.0;
//
//        weatherRecordGateway.addForecast(locationId, TEST_SQL_DATE, initialCondition, initialTemp);
//
//        Integer updatedId = weatherRecordGateway.addForecast(locationId, TEST_SQL_DATE, updatedCondition, updatedTemp);
//
//        assertNotNull(updatedId, "A non-null ID should be returned upon successful update.");
//        
//        WeatherRecord updatedRecord = weatherRecordGateway.getWeatherByLocationAndDate(locationId, TEST_SQL_DATE);
//        assertNotNull(updatedRecord, "The record should still be retrievable after update.");
//        
//        assertNotEquals(initialCondition, updatedRecord.getCondition(), "Condition should be updated.");
//        assertEquals(updatedCondition, updatedRecord.getCondition(), "Condition should match the updated value.");
//        
//        assertNotEquals(initialTemp, updatedRecord.getTemperature(), 0.1, "Temperature should be updated.");
//        assertEquals(updatedTemp, updatedRecord.getTemperature(), 0.1, "Temperature should match the updated value.");
//    }


    @Test
    void testGetLastByLocation_Lisboa() throws PersistenceException {
        DataSource.INSTANCE.executeSQLUpdate("DELETE FROM DailyForecast WHERE location_id = 1 AND record_date = '" + TEST_DATE.toString() + "';");

        WeatherRecord record = weatherRecordGateway.getLastByLocation(2); 
        
        assertNotNull(record, "Weather record should not be null (assuming data exists).");
        assertEquals(2, record.getLocationId(), "Location ID should be Porto (1).");
    }

    // Test for getting the last 3 weather records for Porto
    @Test
    void testGetLastNHistoricalByLocation_Porto() throws PersistenceException {
        
        DataSource.INSTANCE.executeSQLUpdate("DELETE FROM DailyForecast WHERE location_id = 2 AND record_date >= '" + TEST_DATE.minusDays(5).toString() + "';");

        List<WeatherRecord> records = weatherRecordGateway.getLastNHistoricalByLocation(2, 3); // Porto has location_id = 2
        
        assertNotNull(records, "Weather records list should not be null");
       
        LocalDate previousDate = LocalDate.now().plusDays(1); 
        for (WeatherRecord record : records) {
            assertTrue(record.getRecordDate().isBefore(previousDate) || record.getRecordDate().isEqual(previousDate),
                "Record date should be before or equal to the previous one");
            previousDate = record.getRecordDate();
        }

        
    }

    // Test for getting weather by location and date (for Porto on 2025-10-30)
    @Test
    void testGetWeatherByLocationAndDate_Porto_2025_10_30() {
        LocalDate testDate = LocalDate.of(2025, 10, 30); // Specific date for Porto
        Date sqlDate = Date.valueOf(testDate);
        WeatherRecord record = weatherRecordGateway.getWeatherByLocationAndDate(2, sqlDate); // Porto location_id = 2

        assertNotNull(record, "Weather record should not be null");
        assertEquals(testDate, record.getRecordDate(), "Record date should match the requested date");
        
    }

    // Test for location with no weather records (non-existent location)
    @Test
    void testGetWeatherByLocationAndDate_NoRecord() {
        // Uses the globally defined non-existent ID
        WeatherRecord record = weatherRecordGateway.getWeatherByLocationAndDate(9999, TEST_SQL_DATE); 
        assertNull(record, "Weather record should be null for a non-existent location");
    }

    // Test for invalid date where no record exists
    @Test
    void testGetWeatherByLocationAndDate_InvalidDate() {
        // Uses the globally defined date which we ensure doesn't exist for location 1
        WeatherRecord record = weatherRecordGateway.getWeatherByLocationAndDate(1, TEST_SQL_DATE);
        assertNull(record, "Weather record should be null for a date with no record");
    }
}