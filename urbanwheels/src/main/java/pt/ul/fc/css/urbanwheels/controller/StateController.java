package pt.ul.fc.css.urbanwheels.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.urbanwheels.dto.StateDTO;
import pt.ul.fc.css.urbanwheels.services.StateService;

import java.util.List;

@RestController
@RequestMapping("/bike-states")
@Api(value = "BikeState API", tags = "Bike States")
public class StateController {

    private final StateService bikeStateService;

    public StateController(StateService bikeStateService) {
        this.bikeStateService = bikeStateService;
    }

    // GET /bike-states -> Lista todos os estados
    @GetMapping
    @ApiOperation(value = "List all bike-states")
    public ResponseEntity<List<StateDTO>> getAllBikeStates() {
        List<StateDTO> states = bikeStateService.getAllStates();
        return ResponseEntity.ok(states);
    }

    // POST /bike-states -> Cria um novo estado
    @PostMapping
    @ApiOperation(value = "Create a new bike-state")
    public ResponseEntity<StateDTO> createBikeState(@RequestParam String description) {
        StateDTO state = bikeStateService.createState(description);
        return new ResponseEntity<>(state, HttpStatus.CREATED);
    }

}
