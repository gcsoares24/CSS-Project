package pt.ul.fc.css.urbanwheels.dto;

import java.time.LocalDateTime;

public record UserDTO(
        Long id,
        String name,
        String email,
        String userType,
        LocalDateTime createdAt
) {}
