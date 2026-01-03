package pt.ul.fc.css.urbanwheels.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import pt.ul.fc.css.urbanwheels.entities.Bike;
import pt.ul.fc.css.urbanwheels.entities.Client;
import pt.ul.fc.css.urbanwheels.entities.State;
import pt.ul.fc.css.urbanwheels.entities.Station;
import pt.ul.fc.css.urbanwheels.entities.Subscription;
import pt.ul.fc.css.urbanwheels.entities.Trip;

@DataJpaTest
class TripRepositoryTest {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private BikeRepository bikeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private StateRepository stateRepository;

    private Bike createBike(String model, State state, Station station) {
        Bike bike = new Bike();
        bike.setModel(model);
        bike.setState(state);
        bike.setStation(station);
        return bikeRepository.save(bike);
    }

    private Client createClient(String email, String name, Subscription subscription) {
        Client client = new Client(email, name, subscription);
        return userRepository.save(client);
    }

    private Station createStation(String name) {
        Station station = new Station();
        station.setName(name);
        station.setLat(0.0);
        station.setLon(0.0);
        station.setMaxDocks(10);
        return stationRepository.save(station);
    }

    private State createState(String description) {
        State state = new State();
        state.setDescription(description);
        return stateRepository.save(state);
    }

    private Subscription createSubscription(String name) {
        Subscription sub = new Subscription();
        sub.setName(name);
        return subscriptionRepository.save(sub);
    }

    private Trip createTrip(Bike bike, Client client, Station startStation) {
        Trip trip = new Trip();
        trip.setBike(bike);
        trip.setUser(client);
        trip.setStartStation(startStation);
        trip.setStartTime(LocalDateTime.now());
        return tripRepository.save(trip);
    }

    @Test
    void testFindByBikeId() {
        State available = createState("AVAILABLE");
        Station station = createStation("Station A");
        Bike bike = createBike("Model X", available, station);

        Subscription subscription = createSubscription("Monthly");
        Client client = createClient("alice@example.com", "Alice", subscription);

        createTrip(bike, client, station);
        createTrip(bike, client, station);

        List<Trip> trips = tripRepository.findByBikeId(bike.getId());
        assertEquals(2, trips.size());
        assertTrue(trips.stream().allMatch(t -> t.getBike().getId().equals(bike.getId())));
    }

    @Test
    void testFindByUserId() {
        State available = createState("AVAILABLE");
        Station station = createStation("Station B");
        Bike bike = createBike("Model Y", available, station);

        Subscription subscription = createSubscription("Weekly");
        Client client = createClient("bob@example.com", "Bob", subscription);

        createTrip(bike, client, station);

        List<Trip> trips = tripRepository.findByUserId(client.getId());
        assertEquals(1, trips.size());
        assertEquals(client.getId(), trips.get(0).getUser().getId());
    }

    @Test
    void testFindByBikeIdAndUserId() {
        State available = createState("AVAILABLE");
        Station station = createStation("Station C");
        Bike bike = createBike("Model Z", available, station);

        Subscription subscription = createSubscription("Yearly");
        Client client = createClient("charlie@example.com", "Charlie", subscription);

        createTrip(bike, client, station);

        List<Trip> trips = tripRepository.findByBikeIdAndUserId(bike.getId(), client.getId());
        assertEquals(1, trips.size());
        assertEquals(bike.getId(), trips.get(0).getBike().getId());
        assertEquals(client.getId(), trips.get(0).getUser().getId());
    }
}
