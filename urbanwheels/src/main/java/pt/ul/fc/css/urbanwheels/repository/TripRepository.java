package pt.ul.fc.css.urbanwheels.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.urbanwheels.entities.Trip;
import pt.ul.fc.css.urbanwheels.entities.User;

public interface TripRepository extends JpaRepository<Trip, Long> {


    List<Trip> findByBikeId(Long bikeId);

    List<Trip> findByUserId(Long userId);

    List<Trip> findByBikeIdAndUserId(Long bikeId, Long userId);

}
