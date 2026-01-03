package pt.ul.fc.css.urbanwheels.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.urbanwheels.dto.StationDTO;
import pt.ul.fc.css.urbanwheels.dto.StationRequestDTO;
import pt.ul.fc.css.urbanwheels.dto.WeatherConditionDTO;
import pt.ul.fc.css.urbanwheels.services.StationService;
import pt.ul.fc.css.urbanwheels.services.WeatherService;
import pt.ul.fc.css.urbanwheels.entities.Station;
import pt.ul.fc.css.urbanwheels.mapper.StationMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/stations")
public class StationController {

    private final StationService stationService;
    private final WeatherService weatherService;
;

    public StationController(StationService stationService, WeatherService weatherService) {
        this.stationService = stationService;
        this.weatherService = weatherService;
    }

    @GetMapping
    @ApiOperation(value = "List all stations", notes = "Returns a complete list of all stations registered in the system.")
    public ResponseEntity<List<StationDTO>> getAllStations(
            @RequestParam(value = "date", required = false) String date // optional query param
    ) {
        List<Station> stations = stationService.listAllStations();

        // If no date is provided, use today
        if (date == null || date.isBlank()) {
            date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        }

        // Pass the date to the WeatherService
        List<WeatherConditionDTO> wc = weatherService.getWeatherForStations(stations, date);
        System.out.println(wc);

        return ResponseEntity.ok(StationMapper.toDTOList(stations, wc));
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Get station details", notes = "Retrieves a station by its unique ID. Returns 404 if the station is not found.")
    public ResponseEntity<StationDTO> getStationById(@PathVariable Long id) {
        Station station = stationService.getStationDetails(id);
        
        return ResponseEntity.ok(StationMapper.toDTO(station));
    }

    @PostMapping
    @ApiOperation(value = "Register a new station", notes = "Creates a new station based on the provided name, coordinates, and capacity.")
    public ResponseEntity<StationDTO> createStation(@RequestBody StationRequestDTO dto) {
        
        Station stationEntity = StationMapper.toEntity(dto);
        
        Station saved = stationService.createStation(stationEntity);

        StationDTO resultDTO = StationMapper.toDTO(saved);
        
        return new ResponseEntity<>(resultDTO, HttpStatus.CREATED);    
    }
}
