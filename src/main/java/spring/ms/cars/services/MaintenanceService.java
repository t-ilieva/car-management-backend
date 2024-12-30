package spring.ms.cars.services;

import org.springframework.stereotype.Service;
import spring.ms.cars.entity.Car;
import spring.ms.cars.entity.Garage;
import spring.ms.cars.entity.Maintenance;
import spring.ms.cars.repository.CarRepository;
import spring.ms.cars.repository.GarageRepository;
import spring.ms.cars.repository.MaintenanceRepository;
import spring.ms.cars.rest.request.MaintenanceRequest;
import spring.ms.cars.rest.response.MaintenanceResponse;
import spring.ms.cars.rest.transformer.MaintenanceTransformer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MaintenanceService {

    private static final SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private final MaintenanceRepository maintenanceRepository;
    private final GarageRepository garageRepository;
    private final CarRepository carRepository;

    public MaintenanceService(MaintenanceRepository maintenanceRepository, GarageRepository garageRepository, CarRepository carRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.garageRepository = garageRepository;
        this.carRepository = carRepository;
    }

    public List<MaintenanceResponse> getFiltered(Integer carId, Integer garageId, String startDate, String endDate) {
        List<Maintenance> maintenances = maintenanceRepository.findAll();

        if (startDate != null && !startDate.isEmpty()) {
            try {
                Date start = DATE_TIME_FORMATTER.parse(startDate);
                maintenances = maintenances.stream()
                        .filter(maintenance -> maintenance.getScheduledDate().after(start))
                        .collect(Collectors.toList());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        if (endDate != null && !endDate.isEmpty()) {
            try {
                Date end = DATE_TIME_FORMATTER.parse(endDate);
                maintenances = maintenances.stream()
                        .filter(maintenance -> maintenance.getScheduledDate().before(end))
                        .collect(Collectors.toList());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        if (carId != null) {
            Car car = carRepository.findById(carId).orElse(null);
            if (car != null) {
                maintenances = maintenances.stream()
                        .filter(maintenance -> maintenance.getCar().equals(car))
                        .collect(Collectors.toList());
            } else {
                return List.of();
            }
        }

        if (garageId != null) {
            Garage garage = garageRepository.findById(garageId).orElse(null);
            if (garage != null) {
                maintenances = maintenances.stream()
                        .filter(maintenance -> maintenance.getGarage().equals(garage))
                        .collect(Collectors.toList());
            } else {
                return List.of();
            }
        }

        return maintenances.stream()
                .map(MaintenanceTransformer::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<MaintenanceResponse> getById(int id) {
        return maintenanceRepository.findById(id)
                .map(MaintenanceTransformer::toResponse);
    }

    public int create(MaintenanceRequest maintenanceRequest) {
        Maintenance maintenance = MaintenanceTransformer.toEntity(maintenanceRequest);

        Garage garage = garageRepository.findById(maintenanceRequest.getGarageId())
                .orElseThrow(() -> new RuntimeException("Garage with ID " + maintenanceRequest.getGarageId() + " not found"));

        maintenance.setGarage(garage);

        Car car = carRepository.findById(maintenanceRequest.getCarId())
                .orElseThrow(() -> new RuntimeException("Garage with ID " + maintenanceRequest.getCarId() + " not found"));

        maintenance.setCar(car);

        return maintenanceRepository.save(maintenance).getId();
    }

    public void update(int id, MaintenanceRequest maintenanceRequest){
        Maintenance maintenance = MaintenanceTransformer.toEntity(maintenanceRequest);
        maintenance.setId(id);

        Garage garage = garageRepository.findById(maintenanceRequest.getGarageId())
                .orElseThrow(() -> new RuntimeException("Garage with ID " + maintenanceRequest.getGarageId() + " not found"));

        maintenance.setGarage(garage);

        Car car = carRepository.findById(maintenanceRequest.getCarId())
                .orElseThrow(() -> new RuntimeException("Garage with ID " + maintenanceRequest.getCarId() + " not found"));

        maintenance.setCar(car);

        maintenanceRepository.save(maintenance);
    }

    public void delete(int id){
        maintenanceRepository.deleteById(id);
    }
}
