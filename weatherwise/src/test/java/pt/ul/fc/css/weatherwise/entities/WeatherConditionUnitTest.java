package pt.ul.fc.css.weatherwise.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class WeatherConditionUnitTest {

    // --- Tests for the getId() method ---

    @Test
    void testGetId_SUN() {
        assertEquals(1, WeatherCondition.SUN.getId(), "SUN ID should be 1.");
    }

    @Test
    void testGetId_RAIN() {
        assertEquals(2, WeatherCondition.RAIN.getId(), "RAIN ID should be 2.");
    }

    @Test
    void testGetId_THUNDERSTORM() {
        assertEquals(3, WeatherCondition.THUNDERSTORM.getId(), "THUNDERSTORM ID should be 3.");
    }

    @Test
    void testGetId_CLOUDY() {
        assertEquals(4, WeatherCondition.CLOUDY.getId(), "CLOUDY ID should be 4.");
    }

    @Test
    void testGetId_SNOW() {
        assertEquals(5, WeatherCondition.SNOW.getId(), "SNOW ID should be 5.");
    }

    @Test
    void testGetId_WINDY() {
        assertEquals(6, WeatherCondition.WINDY.getId(), "WINDY ID should be 6.");
    }

    // --- Tests for the getDescription() method ---

    @Test
    void testGetDescription_SUN() {
        assertEquals("Sun", WeatherCondition.SUN.getDescription(), "SUN description should be 'Sun'.");
    }

    @Test
    void testGetDescription_RAIN() {
        assertEquals("Rain", WeatherCondition.RAIN.getDescription(), "RAIN description should be 'Rain'.");
    }

    @Test
    void testGetDescription_THUNDERSTORM() {
        assertEquals("Thunderstorm", WeatherCondition.THUNDERSTORM.getDescription(), "THUNDERSTORM description should be 'Thunderstorm'.");
    }

    @Test
    void testGetDescription_CLOUDY() {
        assertEquals("Cloudy", WeatherCondition.CLOUDY.getDescription(), "CLOUDY description should be 'Cloudy'.");
    }

    @Test
    void testGetDescription_SNOW() {
        assertEquals("Snow", WeatherCondition.SNOW.getDescription(), "SNOW description should be 'Snow'.");
    }

    @Test
    void testGetDescription_WINDY() {
        assertEquals("Windy", WeatherCondition.WINDY.getDescription(), "WINDY description should be 'Windy'.");
    }

    // --- Tests for the toString() method ---

    @Test
    void testToString_SUN() {
        // toString() should return the description ("Sun")
        assertEquals("Sun", WeatherCondition.SUN.toString(), "toString() should return the description 'Sun'.");
    }

    @Test
    void testToString_RAIN() {
        // toString() should return the description ("Rain")
        assertEquals("Rain", WeatherCondition.RAIN.toString(), "toString() should return the description 'Rain'.");
    }


    // --- Tests for the static fromId(int id) method ---

    @Test
    void testFromId_Valid_SUN() {
        assertEquals(WeatherCondition.SUN, WeatherCondition.fromId(1), "ID 1 should map to SUN.");
    }

    @Test
    void testFromId_Valid_THUNDERSTORM() {
        assertEquals(WeatherCondition.THUNDERSTORM, WeatherCondition.fromId(3), "ID 3 should map to THUNDERSTORM.");
    }

    @Test
    void testFromId_Valid_WINDY() {
        assertEquals(WeatherCondition.WINDY, WeatherCondition.fromId(6), "ID 6 should map to WINDY.");
    }

    @Test
    void testFromId_InvalidId() {
        int invalidId = 99; // An ID that does not exist

        // Verify that the correct exception is thrown
        assertThrows(IllegalArgumentException.class,
                     () -> WeatherCondition.fromId(invalidId),
                     "fromId() must throw IllegalArgumentException for invalid ID 99.");
    }

    @Test
    void testFromId_NegativeId() {
        int negativeId = -1; // A negative ID

        // Verify that the correct exception is thrown
        assertThrows(IllegalArgumentException.class,
                     () -> WeatherCondition.fromId(negativeId),
                     "fromId() must throw IllegalArgumentException for a negative ID.");
    }
}
