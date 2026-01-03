package pt.ul.fc.css.urbanwheels.services;

import org.springframework.stereotype.Service;
import pt.ul.fc.css.urbanwheels.dto.StateDTO;
import pt.ul.fc.css.urbanwheels.entities.State;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.urbanwheels.mapper.StateMapper;
import pt.ul.fc.css.urbanwheels.repository.StateRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StateService {

    private final StateRepository stateRepository;

    public StateService(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    /**
     * Get all bike states as DTOs
     */
    public List<StateDTO> getAllStates() {
    	List<State> states = stateRepository.findAll();
    	
    	if(states.isEmpty()) {
            throw new ResourceNotFoundException("No State Found");
    		
    	}
        return states
                .stream()
                .map(StateMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create a new state with the given description
     */
    public StateDTO createState(String description) {
        // Validate input
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be empty");
        }

        // Check if state already exists
        State state = stateRepository.findByDescription(description)
                .orElseGet(() -> {
                    State newState = new State();
                    newState.setDescription(description);
                    return stateRepository.save(newState);
                });

        return StateMapper.toDTO(state);
    }
}
