package pt.ul.fc.css.urbanwheels.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import pt.ul.fc.css.urbanwheels.dto.BikeDTO;
import pt.ul.fc.css.urbanwheels.dto.EndTripDTO;
import pt.ul.fc.css.urbanwheels.dto.TripDTO;
import pt.ul.fc.css.urbanwheels.dto.TripModDTO;
import pt.ul.fc.css.urbanwheels.dto.TripReserved;
import pt.ul.fc.css.urbanwheels.services.TripService;
import pt.ul.fc.css.urbanwheels.mapper.TripMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    // A. Start a new trip
    @PostMapping("/start")
    @ApiOperation(value = "Start a new trip",notes = "User starts a trip with a bike from a station")
    public ResponseEntity<TripDTO> startTrip(@RequestBody TripModDTO req) {
        TripDTO started = tripService.startTrip(req);
        return ResponseEntity.ok(started);
    }

 // Phase 3: List reserved and active trips filtered by state and user
    @GetMapping("/{state}/{userId}")
    @ApiOperation(value = "List all reserved trips by user", 
                  notes = "Can filter by state: AVAILABLE, IN_USE, etc.")
    public ResponseEntity<List<TripReserved>> getReserved(
            @PathVariable String state,
            @PathVariable Long userId) {
    	System.out.println("USER:" +  userId);
    	System.out.println("STATE:" +  state);
        List<TripReserved> bikes = tripService.getReservedTripsByUserAndState(userId, state);
        return ResponseEntity.ok(bikes);
    }

    // for use case M in phase 3
    @PostMapping("/reserve/{date}")
    @ApiOperation(value = "Reserve a bike", notes = "User reserves a bike for a future date")
    public ResponseEntity<TripDTO> reserve(@RequestBody TripModDTO req, @PathVariable String date) {
        LocalDateTime reservationDate;

        try {
            // Try ISO format first
            reservationDate = LocalDateTime.parse(date); // "2025-12-16T10:00:00"
        } catch (Exception e1) {
            try {
                // Try custom dd-MM-yyyy format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                reservationDate = LocalDate.parse(date, formatter).atStartOfDay(); // 00:00 as default
            } catch (Exception e2) {
                throw new IllegalArgumentException("Invalid date format. Use ISO or dd-MM-yyyy");
            }
        }

        TripDTO reserved = tripService.reserve(req, reservationDate);
        return ResponseEntity.ok(reserved);
    }


    // For use case M: Cancel a specific bike reservation by tripId
       @PostMapping("/cancel/{tripId}")
       @ApiOperation(value = "Cancel a bike reservation", notes = "User cancels a previously reserved bike by trip ID")
       public ResponseEntity<TripDTO> cancelReservation(@PathVariable Long tripId) {

           // Call service to cancel the trip
           TripDTO canceledTrip = tripService.cancelReservation(tripId);

           return ResponseEntity.ok(canceledTrip);
       }




    @PostMapping("/{tripId}/end")
    @ApiOperation(value = "End an ongoing trip",notes = "User returns a bike to a station and ends the trip")
    public ResponseEntity<TripDTO> endTrip(@PathVariable Long tripId, @RequestBody EndTripDTO req) {
        TripDTO endedTrip = tripService.endTrip(tripId, req);
        return ResponseEntity.ok(endedTrip);
    }

    @GetMapping
    @ApiOperation(value = "List trips", notes = "Optionally filter by userId, bikeId, or both")
    public ResponseEntity<List<TripDTO>> listTrips(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bikeId) {

        List<TripDTO> trips;

        if (userId != null && bikeId != null) {
            // Both parameters → filter by bike AND user
            trips = tripService.getTripsByBikeAndUser(bikeId, userId);
        } else if (userId != null) {
            // Only user
            trips = tripService.getTripsByUser(userId);
        } else if (bikeId != null) {
            // Only bike
            trips = tripService.getTripsByBike(bikeId);
        } else {
            // Neither → return all trips
            trips = tripService.getAllTrips();
        }

        return ResponseEntity.ok(trips);
    }


}
