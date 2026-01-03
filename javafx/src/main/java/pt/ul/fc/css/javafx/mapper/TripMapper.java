package pt.ul.fc.css.javafx.mapper;

import pt.ul.fc.css.javafx.dto.TripDTO;
import pt.ul.fc.css.javafx.model.Trip;

import java.time.format.DateTimeFormatter;

public class TripMapper {

    public static TripDTO toDTO(Trip trip) {
        if (trip == null) return null;

        return new TripDTO(
                trip.getId(),
                trip.getBikeModel(),
                trip.getUserId(),
                trip.getStartStation(),
                trip.getEndStation(),
                trip.getStartTime(),
                trip.getEndTime()
        );
    }
    
    public static String toString(TripDTO trip, int position) {
        if (trip == null) return "";

        String endStation = trip.endStation() != null ? trip.endStation() : "Not finished";

        return String.format(
                "Trip #%d%n" +
                "Bike: %s%n" +
                "Stations: %s → %s%n" +
                "Start: %s%n" +
                "End: %s",
                position,
                trip.bikeModel(),
                trip.startStation() != null ? trip.startStation() : "Unknown",
                endStation,
                trip.startTime() != null ? trip.startTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "Unknown",
                trip.endTime() != null ? trip.endTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "Not finished"
        );
    }

}
