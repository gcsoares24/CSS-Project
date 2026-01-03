package pt.ul.fc.css.urbanwheels.mapper;


import java.util.List;

import pt.ul.fc.css.urbanwheels.dto.UserDTO;
import pt.ul.fc.css.urbanwheels.dto.UserDetailsDTO;
import pt.ul.fc.css.urbanwheels.entities.Client;
import pt.ul.fc.css.urbanwheels.entities.User;

public class UserMapper {

    public static UserDTO toDTO(User user) {
        String userType = user instanceof Client ? "CLIENT" : "ADMIN";
        return new UserDTO(
            user.getId(),
            user.getName(),
            user.getEmail(),
            userType,
            user.getCreatedAt()
        );
    }
    
    public static UserDetailsDTO toDetailsDTO(User user, String subscription, List<?> activities) {
        return new UserDetailsDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                subscription,
                user.getCreatedAt(),
                activities
        );
    }
}
