package spring.ms.cars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.ms.cars.entity.Garage;

import java.util.List;

public interface GarageRepository extends JpaRepository<Garage, Integer> {

    List<Garage> findByCity(String city);
}
