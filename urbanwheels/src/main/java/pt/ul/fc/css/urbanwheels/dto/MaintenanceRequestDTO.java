package pt.ul.fc.css.urbanwheels.dto;

public record MaintenanceRequestDTO(
	    Long adminId,
	    String description,
	    double cost
	) {}
