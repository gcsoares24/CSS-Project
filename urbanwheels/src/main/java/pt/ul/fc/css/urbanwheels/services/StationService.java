package pt.ul.fc.css.urbanwheels.services;

import org.springframework.stereotype.Service;
import pt.ul.fc.css.urbanwheels.dto.StationDTO;
import pt.ul.fc.css.urbanwheels.dto.StationRequestDTO;
import pt.ul.fc.css.urbanwheels.entities.Station;
import pt.ul.fc.css.urbanwheels.entities.Trip;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.urbanwheels.mapper.StationMapper;
import pt.ul.fc.css.urbanwheels.repository.StationRepository;

import java.util.List;

@Service
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station createStation(Station station) {
        // Validate latitude and longitude
        if (station.getLat() < -90 || station.getLat() > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90");
        }
        if (station.getLon() < -180 || station.getLon() > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180");
        }
        // Check if a station with the same lat and lon already exists
        if (stationRepository.findByLatAndLon(station.getLat(), station.getLon()).isPresent()) {
            // Optionally, you could throw an exception here if you want to prevent duplicates
            throw new IllegalArgumentException("A station with the same latitude and longitude already exists.");
        }
        
        // If no station with the same lat and lon exists, save the new station
        return stationRepository.save(station);
    }


    public List<Station> listAllStations() {
    	List<Station> station = stationRepository.findAll();
    	if(station.isEmpty()) {
    		throw new ResourceNotFoundException("No stations found");
    	}
        return station;
    }

    public Station getStationDetails(Long id) {
    	
        return stationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station with the id: " + id + " not found"));
    }

	public Long getAmountStations() {
		return stationRepository.count();
	}
}
