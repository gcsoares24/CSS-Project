package pt.ul.fc.css.urbanwheels.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UserDetailsDTO(
        Long id,
        String name,
        String email,
        String subscriptionType,
        LocalDateTime createdAt,
        List<?> activities //list of trips or maintenences (if Client or Admin)
) {}
