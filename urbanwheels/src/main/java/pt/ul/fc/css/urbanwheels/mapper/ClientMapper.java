package pt.ul.fc.css.urbanwheels.mapper;


import java.util.List;

import pt.ul.fc.css.urbanwheels.dto.CreateClientDTO;
import pt.ul.fc.css.urbanwheels.dto.CreateUserDTO;
import pt.ul.fc.css.urbanwheels.entities.Client;
import pt.ul.fc.css.urbanwheels.entities.Subscription;

public class ClientMapper {   
    
    public static Client toEntity(CreateUserDTO dto, Subscription subscription) {
        if (dto == null) throw new IllegalArgumentException("DTO não pode ser null");
        if (subscription == null) throw new IllegalArgumentException("Subscription não pode ser null");

        Client client = new Client();
        client.setName(dto.name());
        client.setEmail(dto.email());
        client.setSubscription(subscription);
        client.setTrips(List.of()); // new client has no trips

        return client;
    }
    
    public static CreateUserDTO fromCreateClientDTO(CreateClientDTO clientDTO) {
        if (clientDTO == null) return null;

        return new CreateUserDTO(
                clientDTO.name(),
                clientDTO.email(),
                "CLIENT",                 // userType fix for client
                clientDTO.subscriptionType() // client subscriptionType
        );
    }
    
}
