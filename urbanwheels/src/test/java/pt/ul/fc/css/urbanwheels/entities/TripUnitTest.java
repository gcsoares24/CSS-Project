package pt.ul.fc.css.urbanwheels.entities;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class TripUnitTest {

    @Test
    void testDefaultConstructorAndGettersSetters() {
        Trip trip = new Trip();

        // Inicialmente, tudo deve ser null
        assertNull(trip.getId());
        assertNull(trip.getBike());
        assertNull(trip.getUser());
        assertNull(trip.getStartStation());
        assertNull(trip.getEndStation());
        assertNull(trip.getStartTime());
        assertNull(trip.getEndTime());

        // Criar objetos auxiliares
        Bike bike = new Bike();
        bike.setId(1L);
        User user = new User() {}; // instância anónima já que User é abstract
        Station startStation = new Station();
        startStation.setId(2L);
        Station endStation = new Station();
        endStation.setId(3L);
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusHours(1);

        // Setters
        trip.setId(10L);
        trip.setBike(bike);
        trip.setUser(user);
        trip.setStartStation(startStation);
        trip.setEndStation(endStation);
        trip.setStartTime(startTime);
        trip.setEndTime(endTime);

        // Verifica se getters retornam os valores corretos
        assertAll("Check all Trip fields",
            () -> assertEquals(10L, trip.getId()),
            () -> assertEquals(bike, trip.getBike()),
            () -> assertEquals(user, trip.getUser()),
            () -> assertEquals(startStation, trip.getStartStation()),
            () -> assertEquals(endStation, trip.getEndStation()),
            () -> assertEquals(startTime, trip.getStartTime()),
            () -> assertEquals(endTime, trip.getEndTime())
        );
    }

    @Test
    void testNullBikeOrUser() {
        Trip trip = new Trip();

        // Podemos setar Bike e User para null sem problemas (não viola código da entidade)
        trip.setBike(null);
        trip.setUser(null);

        assertNull(trip.getBike());
        assertNull(trip.getUser());
    }
}
