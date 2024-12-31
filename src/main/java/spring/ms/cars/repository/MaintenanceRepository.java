package spring.ms.cars.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.ms.cars.entity.Maintenance;

import java.util.Date;
import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Integer> {

    @Query("SELECT m FROM Maintenance m " +
            "WHERE m.garage.id = :garageId " +
            "AND m.scheduledDate = :scheduledDate")
    List<Maintenance> findAllScheduledForGarageOnDate(@Param("garageId") Integer garageId,
                                                      @Param("scheduledDate") Date scheduledDate);


    @Query("SELECT m.scheduledDate FROM Maintenance m " +
            "WHERE m.garage.id = :garageId " +
            "AND m.scheduledDate BETWEEN :startDate AND :endDate")
    List<Date> findAllScheduledDates(@Param("garageId") Integer garageId,
                                     @Param("startDate") Date startDate,
                                     @Param("endDate") Date endDate);

    @Query("SELECT m FROM Maintenance m " +
            "WHERE m.garage.id = :garageId " +
            "AND m.scheduledDate BETWEEN :startDate AND :endDate")
    List<Maintenance> findAllScheduledForGarage(@Param("garageId") Integer garageId,
                                                @Param("startDate") Date startDate,
                                                @Param("endDate") Date endDate);
}

