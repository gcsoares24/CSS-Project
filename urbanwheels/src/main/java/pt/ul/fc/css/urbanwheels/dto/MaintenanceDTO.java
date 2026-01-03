package pt.ul.fc.css.urbanwheels.dto;

import java.time.LocalDateTime;

public record MaintenanceDTO(
        Long id,
        Long bikeId,
        Long adminId,
        LocalDateTime date,
        String description,
        double cost
) {}
