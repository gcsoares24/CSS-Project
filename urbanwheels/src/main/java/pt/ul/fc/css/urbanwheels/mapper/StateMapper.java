package pt.ul.fc.css.urbanwheels.mapper;

import pt.ul.fc.css.urbanwheels.dto.StateDTO;
import pt.ul.fc.css.urbanwheels.entities.State;

public class StateMapper {

    // Entity -> DTO
    public static StateDTO toDTO(State state) {
        if (state == null) return null;
        return new StateDTO(
            state.getId().longValue(),   // Convert Integer to Long
            state.getDescription()         // Map description
        );
    }

    // DTO -> Entity
    public static State toEntity(StateDTO dto) {
        if (dto == null) return null;
        State state = new State();
        state.setId(dto.id().intValue()); // Convert Long back to Integer
        state.setDescription(dto.description());
        return state;
    }
    

    // Entity -> DTO
    public static StateDTO getStateFromDesc(String desc) {
        if (desc == null) return null;
        return new StateDTO(
        		null,   // Convert Integer to Long
        		desc
        );
    }
}
