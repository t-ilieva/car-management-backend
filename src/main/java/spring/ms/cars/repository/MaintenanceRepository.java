package spring.ms.cars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.ms.cars.entity.Maintenance;

import java.util.Date;
import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {

    @Query("SELECT m.scheduledDate FROM Maintenance m " +
            "WHERE (:garageId IS NULL OR m.garage.id = :garageId) " +
            "AND m.scheduledDate BETWEEN :startDate AND :endDate")
    List<Date> findAllScheduledDates(@Param("garageId") Integer garageId,
                                     @Param("startDate") Date startDate,
                                     @Param("endDate") Date endDate);
}

