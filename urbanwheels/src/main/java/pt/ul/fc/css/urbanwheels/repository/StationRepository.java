package pt.ul.fc.css.urbanwheels.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ul.fc.css.urbanwheels.entities.Station;

public interface StationRepository extends JpaRepository<Station, Long> {
	Optional<Station> findByLatAndLon(double lat, double lon);
}
