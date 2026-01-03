package pt.ul.fc.css.urbanwheels.dto;

public record StationRequestDTO(
        String name,
        double lat,
        double lon,
        int maxDocks
) {}