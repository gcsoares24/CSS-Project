package pt.ul.fc.css.javafx.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserDetailsDTO(
        Long id,
        String name,
        String email,
        String subscriptionType,
        LocalDateTime createdAt,
        @JsonProperty("activities") List<TripDTO> trips) {
}
