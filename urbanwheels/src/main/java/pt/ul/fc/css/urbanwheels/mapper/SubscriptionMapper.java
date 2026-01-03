package pt.ul.fc.css.urbanwheels.mapper;

import pt.ul.fc.css.urbanwheels.dto.SubscriptionDTO;
import pt.ul.fc.css.urbanwheels.entities.Subscription;

public class SubscriptionMapper {

    public static Subscription toEntity(SubscriptionDTO dto) {
        return new Subscription(dto.name());
    }

    public static SubscriptionDTO toDTO(Subscription entity) {
        return new SubscriptionDTO(entity.getId(), entity.getName());
    }
}
