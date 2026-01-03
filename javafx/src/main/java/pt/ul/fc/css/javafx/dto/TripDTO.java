package pt.ul.fc.css.javafx.dto;

import java.time.LocalDateTime;

public record TripDTO(
        Long id,
        String bikeModel,
        Long userId,
        String startStation,
        String endStation,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}
