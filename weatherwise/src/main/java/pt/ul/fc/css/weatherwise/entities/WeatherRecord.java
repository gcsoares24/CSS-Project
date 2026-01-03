package pt.ul.fc.css.weatherwise.entities;

import java.time.LocalDate;

public class WeatherRecord {

    private int locationId; 
    
    private LocalDate recordDate;
    
    private WeatherCondition condition; 
    
    
    private double temperature; 

    public WeatherRecord(int locationId, LocalDate recordDate, WeatherCondition condition, double temperature) {
        this.locationId = locationId;
        this.recordDate = recordDate;
        this.condition = condition;
        this.temperature = temperature;
    }

    public WeatherRecord() {}

    // ===================================
    // Getters and Setters
    // ===================================

    public int getLocationId() {
        return locationId;
    }

    public void setLocationId(int locationId) {
        this.locationId = locationId;
    }

    public LocalDate getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(LocalDate recordDate) {
        this.recordDate = recordDate;
    }

    public WeatherCondition getCondition() {
        return condition;
    }

    public void setCondition(WeatherCondition condition) {
        this.condition = condition;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }
}