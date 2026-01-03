package pt.ul.fc.css.javafx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherConditionDTO(
    @JsonProperty("weather") String condition,
    @JsonProperty("temperature_min") Double minTemp,
    @JsonProperty("temperature_max") Double maxTemp,
    @JsonProperty("date") String date

) {}
