package pt.ul.fc.css.urbanwheels.dto;

public record CreateClientDTO(
        String name,
        String email,
        String subscriptionType
) {}