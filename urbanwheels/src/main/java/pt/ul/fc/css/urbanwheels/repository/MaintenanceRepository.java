package pt.ul.fc.css.urbanwheels.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pt.ul.fc.css.urbanwheels.entities.Maintenance;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
}
