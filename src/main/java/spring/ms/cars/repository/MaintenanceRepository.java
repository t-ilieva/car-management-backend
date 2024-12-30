package spring.ms.cars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.ms.cars.entity.Maintenance;

public interface MaintenanceRepository  extends JpaRepository<Maintenance, Integer> {
}
