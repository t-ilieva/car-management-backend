package spring.ms.cars.rest.transformer;

import spring.ms.cars.entity.Car;
import spring.ms.cars.rest.request.CarRequest;
import spring.ms.cars.rest.response.CarResponse;

import java.util.stream.Collectors;

public class CarTransformer {

    public static CarResponse toResponse(Car car) {
        CarResponse carResponse = new CarResponse();
        carResponse.setId(car.getId());
        carResponse.setMake(car.getMake());
        carResponse.setModel(car.getModel());
        carResponse.setProductionYear(car.getProductionYear());
        carResponse.setLicensePlate(car.getLicensePlate());
        carResponse.setGarages(
                car.getGarages().stream()
                        .map(GarageTransformer::toResponse)
                        .collect(Collectors.toList())
        );

        return carResponse;
    }

    public static Car toEntity(CarRequest request) {
        Car car = new Car();
        car.setMake(request.getMake());
        car.setModel(request.getModel());
        car.setProductionYear(request.getProductionYear());
        car.setLicensePlate(request.getLicensePlate());
        return car;
    }
}
