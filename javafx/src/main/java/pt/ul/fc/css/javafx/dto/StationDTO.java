package pt.ul.fc.css.javafx.dto;

import java.util.List;

public record StationDTO(
        Long id,
        String name,
        double lat,
        double lon,
        int maxDocks,
        List<BikeDTO> bikes,
        WeatherConditionDTO weather
) {}
