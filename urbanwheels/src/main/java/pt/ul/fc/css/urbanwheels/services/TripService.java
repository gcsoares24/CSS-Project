package pt.ul.fc.css.urbanwheels.services;

import org.springframework.stereotype.Service;import java.time.format.DateTimeFormatter;

import org.springframework.transaction.annotation.Transactional;

import pt.ul.fc.css.urbanwheels.dto.BikeDTO;
import pt.ul.fc.css.urbanwheels.dto.EndTripDTO;
import pt.ul.fc.css.urbanwheels.dto.TripDTO;
import pt.ul.fc.css.urbanwheels.dto.TripModDTO;
import pt.ul.fc.css.urbanwheels.dto.TripReserved;
import pt.ul.fc.css.urbanwheels.entities.*;
import pt.ul.fc.css.urbanwheels.exceptions.ResourceNotFoundException;
import pt.ul.fc.css.urbanwheels.exceptions.GlobalExceptionHandler;
import pt.ul.fc.css.urbanwheels.mapper.BikeMapper;
import pt.ul.fc.css.urbanwheels.mapper.TripMapper;
import pt.ul.fc.css.urbanwheels.repository.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TripService {

DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

	public enum Bike_State {
	    AVAILABLE,
	    IN_USE,
	    MAINTENANCE
	}

    private final TripRepository tripRepo;
    private final UserRepository userRepo;
    private final BikeRepository bikeRepo;
    private final StationRepository stationRepo;
    private final StateRepository stateRepo;

    public TripService(
            TripRepository tripRepo,
            UserRepository userRepo,
            BikeRepository bikeRepo,
            StateRepository stateRepo,
            StationRepository stationRepo
    ) {
        this.tripRepo = tripRepo;
        this.userRepo = userRepo;
        this.bikeRepo = bikeRepo;
        this.stationRepo = stationRepo;
		this.stateRepo = stateRepo;
    }

    // ------------------------------------------------------
    // START TRIP
    // ------------------------------------------------------
    @Transactional
    public TripDTO startTrip(TripModDTO dto) {

        User user = userRepo.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!(user instanceof Client)) {
            throw new IllegalArgumentException("Only clients can start trips");
        }

        Bike bike = bikeRepo.findById(dto.bikeId())
                .orElseThrow(() -> new ResourceNotFoundException("Bike not found"));

        
        // Fetch AVAILABLE state from DB
        State availableState = stateRepo.findByDescription("AVAILABLE")
                .orElseThrow(() -> new ResourceNotFoundException("State AVAILABLE not found"));

        if (!bike.getState().getId().equals(availableState.getId())) {
            throw new IllegalArgumentException("Bike is not available");
        }
        Station station = stationRepo.findById(bike.getStation().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Station not found"));

        Trip trip = new Trip();
        trip.setUser(user);
        trip.setBike(bike);
        trip.setStartStation(station);
        trip.setStartTime(LocalDateTime.now());

        // Fetch IN_USE state from DB
        State inUseState = stateRepo.findByDescription("IN_USE")
                .orElseThrow(() -> new ResourceNotFoundException("State IN_USE not found"));

        bike.setState(inUseState);
        bike.setStation(null);
        Trip saved = tripRepo.save(trip);
        return TripMapper.toDTO(saved);
    }


    // ------------------------------------------------------
    // END TRIP 
    // ------------------------------------------------------

    @Transactional
    public TripDTO endTrip(Long tripId, EndTripDTO dto) {
        Trip trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));

        // in case the trip already ended
        if (trip.getEndTime() != null) {
            throw new IllegalArgumentException("This trip has already ended.");
        }
        

        Station endStation = stationRepo.findById(dto.endStationId())
                .orElseThrow(() -> new ResourceNotFoundException("Destination Station not found"));

        // in case the station is full
        if (endStation.getBikes().size() >= endStation.getMaxDocks()) {
            throw new IllegalArgumentException("Station is full.");
            }

        // updates the trip (closes the cicle)
        trip.setEndTime(LocalDateTime.now());
        trip.setEndStation(endStation);

        // updates the bike (changes localization and state)
        Bike bike = trip.getBike();
        bike.setStation(endStation);

        State availableState = stateRepo.findByDescription(Bike_State.AVAILABLE.name())
                .orElseThrow(() -> new ResourceNotFoundException("State " + Bike_State.AVAILABLE.name() + " not found"));

        bike.setState(availableState);

        bikeRepo.save(bike);
        Trip saved = tripRepo.save(trip);

        return TripMapper.toDTO(saved);
    }


    // ------------------------------------------------------
    //  LIST TRIPS
    // ------------------------------------------------------
    public List<TripDTO> getTripsByUser(Long userId) {
        List<Trip> trips = tripRepo.findByUserId(userId);
        if (trips.isEmpty()) {
            throw new ResourceNotFoundException("No trips found for that user");
        }
        return trips.stream()
                    .map(TripMapper::toDTO)
                    .collect(Collectors.toList());
    }

    public List<TripDTO> getTripsByBike(Long bikeId) {
        List<Trip> trips = tripRepo.findByBikeId(bikeId);
        if (trips.isEmpty()) {
            throw new ResourceNotFoundException("No trips found for that bike");
        }
        return trips.stream()
                    .map(TripMapper::toDTO)
                    .collect(Collectors.toList());
    }
    public List<TripDTO> getTripsByBikeAndUser(Long bikeId, Long userId) {
        List<Trip> trips = tripRepo.findByBikeIdAndUserId(bikeId, userId);
        if (trips.isEmpty()) {
            throw new ResourceNotFoundException("No trips found for that bike and user combination");
        }
        return trips.stream()
                    .map(TripMapper::toDTO)
                    .collect(Collectors.toList());
    }

    public List<TripDTO> getAllTrips() {
    	List<Trip> trips = tripRepo.findAll();
    	if (trips.isEmpty()) {
            throw new ResourceNotFoundException("No trips found");
        }
        return trips.stream()
                .map(TripMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public TripDTO reserve(TripModDTO dto, LocalDateTime date) {

        User user = userRepo.findById(dto.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!(user instanceof Client)) {
            throw new IllegalArgumentException("Only clients can reserve bikes");
        }

        Bike bike = bikeRepo.findById(dto.bikeId())
                .orElseThrow(() -> new ResourceNotFoundException("Bike not found"));

        // Fetch AVAILABLE state from DB
        State availableState = stateRepo.findByDescription("AVAILABLE")
                .orElseThrow(() -> new ResourceNotFoundException("State AVAILABLE not found"));

        if (!bike.getState().getId().equals(availableState.getId())) {
            throw new IllegalArgumentException("Bike is not available for reservation");
        }

        Station station = stationRepo.findById(bike.getStation().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Station not found"));

        // Create a Trip with the provided date as startTime
        Trip trip = new Trip();
        trip.setUser(user);
        trip.setBike(bike);
        trip.setStartStation(station);
        trip.setStartTime(date); // set the reservation date

        // Fetch RESERVED state from DB
        State reservedState = stateRepo.findByDescription("RESERVED")
                .orElseThrow(() -> new ResourceNotFoundException("State RESERVED not found"));

        bike.setState(reservedState);
        bike.setStation(station); // still at the station

        Trip saved = tripRepo.save(trip);
        bikeRepo.save(bike);

        return TripMapper.toDTO(saved);
    }

	public List<TripReserved> getReservedTripsByUserAndState(Long userId, String state) {
        List<Trip> trips = tripRepo.findByUserId(userId).stream()
            .filter(trip ->
                trip.getEndTime() == null &&
                trip.getBike().getState().getDescription().equals(state)
            )
            .collect(Collectors.toList());
        
        System.out.println(trips);
        if (trips.isEmpty()) {
            return Collections.emptyList();
        }

        return trips.stream()
                .map(trip -> new TripReserved(
                        trip.getBike().getModel(),
                        trip.getStartTime().format(formatter), // or use formatter for custom format
                        trip.getId()
                ))
                .collect(Collectors.toList());
    }



    
    @Transactional
    public TripDTO cancelReservation(Long tripId) {
        Trip trip = tripRepo.findById(tripId)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found with id: " + tripId));

        Bike bike = trip.getBike();

        // Ensure the trip is a reservation and hasn't started yet
        State reservedState = stateRepo.findByDescription("RESERVED")
                .orElseThrow(() -> new ResourceNotFoundException("State RESERVED not found"));

        if (!bike.getState().getId().equals(reservedState.getId())) {
            throw new IllegalArgumentException("Only reserved trips can be cancelled");
        }

        // Fetch AVAILABLE state
        State availableState = stateRepo.findByDescription("AVAILABLE")
                .orElseThrow(() -> new ResourceNotFoundException("State AVAILABLE not found"));

        // Update bike state
        bike.setState(availableState);
        bike.setStation(trip.getStartStation());

        bikeRepo.save(bike);

        // Optionally, remove the trip or keep it with a cancelled flag
        tripRepo.delete(trip);

        return TripMapper.toDTO(trip);
    }


}
