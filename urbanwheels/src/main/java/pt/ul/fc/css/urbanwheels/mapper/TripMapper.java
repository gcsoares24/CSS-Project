package pt.ul.fc.css.urbanwheels.mapper;

import pt.ul.fc.css.urbanwheels.dto.TripDTO;
import pt.ul.fc.css.urbanwheels.entities.Bike;
import pt.ul.fc.css.urbanwheels.entities.Client;
import pt.ul.fc.css.urbanwheels.entities.Station;
import pt.ul.fc.css.urbanwheels.entities.Trip;

public class TripMapper {

    public static TripDTO toDTO(Trip trip) {
        if (trip == null) return null;

        return new TripDTO(
                trip.getId(),
                trip.getBike().getModel(),
                trip.getUser().getId(),
                trip.getStartStation().getName(),
                trip.getEndStation() != null ? trip.getEndStation().getName() : null,
                trip.getStartTime(),
                trip.getEndTime()
        );
    }
    
}
