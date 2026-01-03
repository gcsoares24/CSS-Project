package pt.ul.fc.css.javafx.mapper;

import pt.ul.fc.css.javafx.dto.TripDTO;
import pt.ul.fc.css.javafx.dto.UserDetailsDTO;
import pt.ul.fc.css.javafx.model.User;

import java.util.List;

public class UserMapper {

    // Converte do DTO recebido da API para o modelo JavaFX
    public static User toModel(UserDetailsDTO dto) {
        List<TripDTO> trips = dto.trips().stream()
                .map(trip -> (TripDTO) trip)
                .toList();

        return new User(
                dto.id(),
                dto.name(),
                dto.email(),
                dto.subscriptionType(),
                dto.createdAt(),
                trips
        );
    }

    // Converte do modelo JavaFX para DTO, se necessário
    public static UserDetailsDTO toDTO(User user) {
        return new UserDetailsDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getSubscriptionType(),
                user.getCreatedAt(),
                user.getTrips() // já é List<TripDTO>
        );
    }

    // Apenas para exibir informações do user
    public static String formatUserInfo(User user) {
        return String.format(
                "Name: %s\nEmail: %s\nSubscription: %s\nCreated At: %s",
                user.getName(),
                user.getEmail(),
                user.getSubscriptionType(),
                user.getCreatedAt()
        );
    }
}
