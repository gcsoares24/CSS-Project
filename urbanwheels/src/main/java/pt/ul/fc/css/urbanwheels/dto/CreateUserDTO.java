package pt.ul.fc.css.urbanwheels.dto;

public record CreateUserDTO(
        String name,
        String email,
        String userType,          // "CLIENT" ou "ADMIN"
        String subscriptionType
) {}
