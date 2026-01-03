package pt.ul.fc.css.javafx.mapper;

import pt.ul.fc.css.javafx.dto.StateDTO;
import pt.ul.fc.css.javafx.model.State; // Assuming you kept the State model

public class StateMapper {

    // Model -> DTO
    public static StateDTO toDTO(State state) {
        if (state == null) return null;
        return new StateDTO(
            state.getId(),       
            state.getDescription() 
        );
    }

    // DTO -> Model
    public static State toModel(StateDTO dto) {
        if (dto == null) return null;
        // Converts the DTO into the JavaFX Model
        return new State(
            dto.id(), 
            dto.description()
        );
    }
}