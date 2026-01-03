package pt.ul.fc.css.urbanwheels.services;


import java.time.LocalDateTime;
import java.util.Collections;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import pt.ul.fc.css.urbanwheels.dto.BikeDTO;
import pt.ul.fc.css.urbanwheels.dto.MaintenanceDTO;
import pt.ul.fc.css.urbanwheels.entities.Bike;
import pt.ul.fc.css.urbanwheels.entities.State;
import pt.ul.fc.css.urbanwheels.entities.Maintenance;
import pt.ul.fc.css.urbanwheels.entities.Station;
import pt.ul.fc.css.urbanwheels.entities.User;
import pt.ul.fc.css.urbanwheels.entities.Admin;
import pt.ul.fc.css.urbanwheels.mapper.BikeMapper;
import pt.ul.fc.css.urbanwheels.mapper.MaintenanceMapper;
import pt.ul.fc.css.urbanwheels.repository.BikeRepository;
import pt.ul.fc.css.urbanwheels.repository.MaintenanceRepository;
import pt.ul.fc.css.urbanwheels.repository.StateRepository;
import pt.ul.fc.css.urbanwheels.repository.StationRepository;
import pt.ul.fc.css.urbanwheels.repository.UserRepository;
import pt.ul.fc.css.urbanwheels.exceptions.*;

@Service
public class BikeService {
	private final BikeRepository bikeRepository;
	private final StationRepository stationRepository;
	private final StateRepository stateRepository;
	private final UserRepository userRepository;
	private final MaintenanceRepository maintenanceRepository;
	
	public BikeService(BikeRepository bikeRepository, StationRepository stationRepository, StateRepository stateRepository, UserRepository userRepository, MaintenanceRepository maintenanceRepository) {
		this.bikeRepository = bikeRepository;
		this.stationRepository = stationRepository;
		this.stateRepository = stateRepository;
		this.userRepository = userRepository;
		this.maintenanceRepository = maintenanceRepository;
	}
	
	public BikeDTO addBikeToStation(BikeDTO bikeDto) {
		Station station = stationExists(bikeDto.stationId());
		State state = stateExists(bikeDto.state().description());
		if (station.getBikes().size() >= station.getMaxDocks()) {
		    throw new IllegalArgumentException("No space in that station");
		}
		
		Bike bike = BikeMapper.toEntity(bikeDto, station, state);
		Bike newBike = bikeRepository.save(bike);
		return BikeMapper.toDTO(newBike);
	}

	public List<BikeDTO> getBikesByState(String bikeState) {
	    // Find the unique Estado
	    State state = stateExists(bikeState);

	    // Get all bikes for this Estado
	    List<Bike> bikes = bikeRepository.findByState(state);

		if(bikes.isEmpty()) {
            throw new ResourceNotFoundException("No bikes found.");
		}
	    // Convert to DTOs
	    return bikes.stream()
	                .map(BikeMapper::toDTO)
	                .collect(Collectors.toList());
	}

	public List<BikeDTO> getAllBikes() {
		List<Bike> bikes = bikeRepository.findAll();
		
		if(bikes.isEmpty()) {
            throw new ResourceNotFoundException("No bikes found.");
		}
        return bikeRepository.findAll()
                             .stream()
                             .map(BikeMapper::toDTO) 
                             .collect(Collectors.toList());
    }

	public BikeDTO changeBikeState(Long bikeId, String newStateDesc) {
	    Bike bike = bikeExists(bikeId);

	    State newState = stateExists(newStateDesc);

	    bike.setState(newState);

	    bikeRepository.save(bike);
	    return BikeMapper.toDTO(bike);
	}


	public MaintenanceDTO registerMaintenance(MaintenanceDTO maintenanceData) {

	    Bike bike = bikeExists(maintenanceData.bikeId());

	    User user = adminExists(maintenanceData.adminId());

	    if (!(user instanceof Admin admin)) {
	        throw new IllegalArgumentException(
	                "User with id " + maintenanceData.adminId() + " is not an admin"
	        );
	    }
	    
	    
	    Maintenance maintenance = MaintenanceMapper.toEntity(maintenanceData);

	    Maintenance savedMain = maintenanceRepository.save(maintenance);
	    return MaintenanceMapper.toDTO(savedMain);
	}
	
	private Bike bikeExists(Long bikeId) {
		return bikeRepository.findById(bikeId)
	            .orElseThrow(() -> new ResourceNotFoundException("Bike not found"));
	}
	
	private State stateExists(String description) {
		return stateRepository.findByDescription(description)
	            .orElseThrow(() -> new ResourceNotFoundException("State not found"));
	}
	
	private Station stationExists(Long stationId) {
		return stationRepository.findById(stationId).orElseThrow(() -> 
	    new ResourceNotFoundException("Station not found"));
	}
	
	private User adminExists(Long adminId) {
		return userRepository.findById(adminId)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

}
