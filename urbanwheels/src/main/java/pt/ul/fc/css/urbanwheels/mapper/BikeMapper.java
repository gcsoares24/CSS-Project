package pt.ul.fc.css.urbanwheels.mapper;

import pt.ul.fc.css.urbanwheels.dto.AddBikeToFleetDTO;
import pt.ul.fc.css.urbanwheels.dto.BikeDTO;
import pt.ul.fc.css.urbanwheels.entities.Bike;
import pt.ul.fc.css.urbanwheels.entities.State;
import pt.ul.fc.css.urbanwheels.entities.Station;

public class BikeMapper {

    public static BikeDTO toDTO(Bike bike) {
        if (bike == null) return null;

        return new BikeDTO(
                bike.getId(),
                bike.getModel(),
                StateMapper.toDTO(bike.getState()),
                bike.getStation() != null ? bike.getStation().getId() : null
        );
    }
    // AddBikeToFleet TO DTO
    public static BikeDTO ABTFtoDTO(AddBikeToFleetDTO bike) {
        if (bike == null) return null;

        return new BikeDTO(
                null,
                bike.model(),
                StateMapper.getStateFromDesc("AVAILABLE"),
                bike.stationId() != null ? bike.stationId() : null
        );
    }
    
    public static Bike toEntity(BikeDTO bikeDTO, Station station, State state) {
        if (bikeDTO == null) return null;

        Bike bike = new Bike();
        bike.setId(bikeDTO.id());
        bike.setModel(bikeDTO.model());
        bike.setState(state);
        bike.setStation(station); 

        return bike;
    }

}
