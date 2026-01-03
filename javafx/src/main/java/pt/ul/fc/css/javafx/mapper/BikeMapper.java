package pt.ul.fc.css.javafx.mapper;

import pt.ul.fc.css.javafx.dto.BikeDTO;
import pt.ul.fc.css.javafx.model.Bike;

public class BikeMapper {

    // DTO -> JavaFX Model
    // Used when receiving data from the API to display in the UI
    public static Bike toModel(BikeDTO dto) {
        if (dto == null) return null;

        return new Bike(
            dto.id(), 
            dto.model(), 
            dto.state(),     // We store the StateDTO directly in the Bike model
            dto.stationId()
        );
    }

    // JavaFX Model -> DTO
    // Used if you need to send updated bike data back to the server
    public static BikeDTO toDTO(Bike bike) {
        if (bike == null) return null;

        return new BikeDTO(
            bike.getId(),
            bike.getModel(),
            bike.getState(),
            bike.getStationId()
        );
    }
}