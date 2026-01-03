package pt.ul.fc.css.urbanwheels.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.urbanwheels.entities.State;
import pt.ul.fc.css.urbanwheels.entities.User;

import java.util.Optional;

public interface StateRepository extends JpaRepository<State, Long> {
    Optional<State> findByDescription(String description); // only one result expected


}