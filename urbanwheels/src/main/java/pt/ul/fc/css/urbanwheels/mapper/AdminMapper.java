package pt.ul.fc.css.urbanwheels.mapper;


import java.util.List;

import pt.ul.fc.css.urbanwheels.dto.CreateAdminDTO;
import pt.ul.fc.css.urbanwheels.dto.CreateUserDTO;
import pt.ul.fc.css.urbanwheels.entities.Admin;

public class AdminMapper {
    
    public static Admin toEntity(CreateUserDTO dto) {
        if (dto == null) throw new IllegalArgumentException("DTO não pode ser null");

        Admin admin = new Admin();
        admin.setName(dto.name());
        admin.setEmail(dto.email());
        admin.setMaintenances(List.of()); // new admin novo did not do anything yet

        return admin;
    }
    
    public static CreateUserDTO fromCreateAdminDTO(CreateAdminDTO adminDTO) {
        if (adminDTO == null) return null;

        return new CreateUserDTO(
                adminDTO.name(),
                adminDTO.email(),
                "ADMIN",    // userType fix for admin
                null        // subscriptionType not appliable
        );
    }
}
