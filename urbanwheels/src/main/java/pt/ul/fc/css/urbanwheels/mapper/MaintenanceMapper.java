package pt.ul.fc.css.urbanwheels.mapper;

import java.time.LocalDateTime;

import pt.ul.fc.css.urbanwheels.dto.MaintenanceDTO;
import pt.ul.fc.css.urbanwheels.dto.MaintenanceRequestDTO;
import pt.ul.fc.css.urbanwheels.entities.Admin;
import pt.ul.fc.css.urbanwheels.entities.Bike;
import pt.ul.fc.css.urbanwheels.entities.Maintenance;

public class MaintenanceMapper {

    public static MaintenanceDTO toDTO(Maintenance m) {
        if (m == null) return null;

        return new MaintenanceDTO(
                m.getId(),
                m.getBike().getId(),
                m.getAdmin().getId(),
                m.getDate(),
                m.getDescription(),
                m.getCost()
        );
    }
    //to DTO From MaintenanceRecordDTO
    public static MaintenanceDTO toDTOFromMR(MaintenanceRequestDTO req, Long bikeId) {
        if (req == null) return null;
        if (bikeId == null) return null;
        return new MaintenanceDTO(
	            null,
	            bikeId,
	            req.adminId(),
	            LocalDateTime.now(),
	            req.description(),
	            req.cost()
	    );

    }
    
    public static Maintenance toEntity(MaintenanceDTO dto) {
        if (dto == null) return null;

        Maintenance m = new Maintenance();
        m.setId(dto.id());

        Bike bike = new Bike();
        bike.setId(dto.bikeId());
        m.setBike(bike);

        Admin admin = new Admin();
        admin.setId(dto.adminId());
        m.setAdmin(admin);

        m.setDate(dto.date());
        m.setDescription(dto.description());
        m.setCost(dto.cost());

        return m;
    }
    
    
}
