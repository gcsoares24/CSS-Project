package pt.ul.fc.css.urbanwheels.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.urbanwheels.entities.Bike;
import pt.ul.fc.css.urbanwheels.entities.State;

public interface BikeRepository extends JpaRepository<Bike, Long> {
    List<Bike> findByState(State state); // fetch all bikes for a single Estado
}
