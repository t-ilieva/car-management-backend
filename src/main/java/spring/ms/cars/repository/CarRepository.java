package spring.ms.cars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.ms.cars.entity.Car;

public interface CarRepository extends JpaRepository<Car, Integer> {
}
