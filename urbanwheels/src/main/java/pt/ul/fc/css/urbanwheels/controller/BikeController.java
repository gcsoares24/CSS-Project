package pt.ul.fc.css.urbanwheels.controller;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.urbanwheels.dto.AddBikeToFleetDTO;
import pt.ul.fc.css.urbanwheels.dto.BikeDTO;
import pt.ul.fc.css.urbanwheels.dto.MaintenanceDTO;
import pt.ul.fc.css.urbanwheels.dto.MaintenanceRequestDTO;
import pt.ul.fc.css.urbanwheels.mapper.BikeMapper;
import pt.ul.fc.css.urbanwheels.mapper.MaintenanceMapper;
import pt.ul.fc.css.urbanwheels.services.BikeService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bikes")
public class BikeController {
    
    private final BikeService bikeService;
    
    public BikeController(BikeService bikeService) {
        this.bikeService = bikeService;
    }
    
    // D. Add a new bike to the fleet (associate it with an initial station)
    @PostMapping("/create")
    @ApiOperation(value = "Add a new bike to the fleet", notes = "Adds a new bike and associates it with an initial station")
    public ResponseEntity<BikeDTO> createBike(@RequestBody AddBikeToFleetDTO req) {
    	BikeDTO dto = BikeMapper.ABTFtoDTO(req);

        BikeDTO newBike = bikeService.addBikeToStation(dto);
        return ResponseEntity.ok(newBike);
    }

    // E. List all bikes (with optional state filter)
    @GetMapping
    @ApiOperation(value = "List all bikes", notes = "Can filter by state: AVAILABLE, IN_USE, etc.")
    public ResponseEntity<List<BikeDTO>> listBikes(@RequestParam(required = false) String state) {
        List<BikeDTO> bikes;
        if (state != null) {
                bikes = bikeService.getBikesByState(state);
        } else {
            bikes = bikeService.getAllBikes();
        }
        return ResponseEntity.ok(bikes);
    }
    


    // F. Change bike state
    @PutMapping("/{bikeId}/state")
    @ApiOperation(value = "Change bike state", notes = "Change the state of a bike (e.g., report maintenance or mark maintenance completed)")
    public ResponseEntity<BikeDTO> changeBikeState(@PathVariable Long bikeId, @RequestParam String newState) {
            BikeDTO bike = bikeService.changeBikeState(bikeId, newState);
            return ResponseEntity.ok(bike);
        
    }

    // G. Register a new maintenance operation for a bike
    @PostMapping("/{bike_id}/maintenance")
    @ApiOperation(value = "Register maintenance operation", notes = "Register a new maintenance operation for a bike")
    public ResponseEntity<MaintenanceDTO> registerMaintenance(@PathVariable("bike_id") Long bikeId, @RequestBody MaintenanceRequestDTO req) {
    	MaintenanceDTO dto = MaintenanceMapper.toDTOFromMR(req, bikeId);
	    MaintenanceDTO saved = bikeService.registerMaintenance(dto);
	 
        return ResponseEntity.ok(saved);
        
    }
}
