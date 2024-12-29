package spring.ms.cars.services;

import org.springframework.stereotype.Service;
import spring.ms.cars.entity.Car;
import spring.ms.cars.entity.Garage;
import spring.ms.cars.repository.CarRepository;
import spring.ms.cars.repository.GarageRepository;
import spring.ms.cars.rest.request.CarRequest;
import spring.ms.cars.rest.response.CarResponse;
import spring.ms.cars.rest.transformer.CarTransformer;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final GarageRepository garageRepository;


    public CarService(CarRepository carRepository, GarageRepository garageRepository) {
        this.carRepository = carRepository;
        this.garageRepository = garageRepository;
    }

    public List<CarResponse> getAll() {
        return carRepository.findAll()
                .stream()
                .map(CarTransformer::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<CarResponse> getById(int id) {
        return carRepository.findById(id)
                .map(CarTransformer::toResponse);
    }

    public List<CarResponse> getFiltered(String carMake, Integer fromYear, Integer toYear, Integer garageId) {
        List<Car> cars = carRepository.findAll();

        if (carMake != null && !carMake.isEmpty()) {
            cars = cars.stream()
                    .filter(car -> car.getMake().equalsIgnoreCase(carMake))
                    .collect(Collectors.toList());
        }

        if (fromYear != null) {
            cars = cars.stream()
                    .filter(car -> car.getProductionYear() >= fromYear)
                    .collect(Collectors.toList());
        }
        if (toYear != null) {
            cars = cars.stream()
                    .filter(car -> car.getProductionYear() <= toYear)
                    .collect(Collectors.toList());
        }

        if (garageId != null) {
            Garage garage = garageRepository.findById(garageId).orElse(null);
            if (garage != null) {
                cars = cars.stream()
                        .filter(car -> car.getGarages().contains(garage))
                        .collect(Collectors.toList());
            } else {
                return List.of();
            }
        }

        return cars.stream()
                .map(CarTransformer::toResponse)
                .collect(Collectors.toList());
    }

    public int create(CarRequest carRequest) {
        Car car = CarTransformer.toEntity(carRequest);

        Set<Garage> garages = garageRepository
                .findAllById(carRequest
                .getGarageIds())
                .stream()
                .collect(Collectors.toSet());

        car.setGarages(garages);

        return carRepository.save(car).getId();
    }

    public void update(int id, CarRequest carRequest){
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Garage with ID " + id + " not found"));

        car.setMake(carRequest.getMake());
        car.setModel(carRequest.getModel());
        car.setProductionYear(carRequest.getProductionYear());
        car.setLicensePlate(carRequest.getLicensePlate());

        if (carRequest.getGarageIds() != null && !carRequest.getGarageIds().isEmpty()) {
            Set<Garage> garages = carRequest.getGarageIds().stream()
                    .map(garageRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toSet());

            car.setGarages(garages);
        }

        carRepository.save(car);
    }

    public void delete(int id){
        carRepository.deleteById(id);
    }
}
