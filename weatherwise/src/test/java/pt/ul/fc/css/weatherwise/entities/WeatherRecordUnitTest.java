package pt.ul.fc.css.weatherwise.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class WeatherRecordTest {

    private final int SAMPLE_LOCATION_ID = 101;
    private final LocalDate SAMPLE_DATE = LocalDate.of(2025, 11, 3);
    private final WeatherCondition SAMPLE_CONDITION = WeatherCondition.SUN;
    private final double SAMPLE_TEMPERATURE = 25.5;


    @Test
    void testFullConstructor() {
        WeatherRecord record = new WeatherRecord(
            SAMPLE_LOCATION_ID, 
            SAMPLE_DATE, 
            SAMPLE_CONDITION, 
            SAMPLE_TEMPERATURE
        );

        assertEquals(SAMPLE_LOCATION_ID, record.getLocationId(), "Location ID must match constructor input.");
        assertEquals(SAMPLE_DATE, record.getRecordDate(), "Record Date must match constructor input.");
        assertEquals(SAMPLE_CONDITION, record.getCondition(), "Condition must match constructor input.");
        assertEquals(SAMPLE_TEMPERATURE, record.getTemperature(), "Temperature must match constructor input.");
    }

    @Test
    void testNoArgsConstructor() {
        WeatherRecord record = new WeatherRecord();

        assertEquals(0, record.getLocationId(), "Default locationId should be 0.");
        assertNull(record.getRecordDate(), "Default recordDate should be null.");
        assertNull(record.getCondition(), "Default condition should be null.");
        assertEquals(0.0, record.getTemperature(), "Default temperature should be 0.0.");
    }


    @Test
    void testGetAndSetLocationId() {
        WeatherRecord record = new WeatherRecord();
        int newId = 200;
        record.setLocationId(newId);
        assertEquals(newId, record.getLocationId(), "setLocationId should update the locationId correctly.");
    }

    @Test
    void testGetAndSetRecordDate() {
        WeatherRecord record = new WeatherRecord();
        LocalDate newDate = LocalDate.of(2026, 1, 15);
        record.setRecordDate(newDate);
        assertEquals(newDate, record.getRecordDate(), "setRecordDate should update the recordDate correctly.");
    }

    @Test
    void testGetAndSetCondition() {
        WeatherRecord record = new WeatherRecord();
        WeatherCondition newCondition = WeatherCondition.RAIN;
        record.setCondition(newCondition);
        assertEquals(newCondition, record.getCondition(), "setCondition should update the condition correctly.");
    }

    @Test
    void testGetAndSetTemperature() {
        WeatherRecord record = new WeatherRecord();
        double newTemperature = 15.3;
        record.setTemperature(newTemperature);
        assertEquals(newTemperature, record.getTemperature(), "setTemperature should update the temperature correctly.");
    }
    
    @Test
    void testSetTemperatureToNegativeValue() {
        WeatherRecord record = new WeatherRecord();
        double negativeTemperature = -5.0;
        record.setTemperature(negativeTemperature);
        assertEquals(negativeTemperature, record.getTemperature(), "The class should allow negative temperatures.");
    }
}