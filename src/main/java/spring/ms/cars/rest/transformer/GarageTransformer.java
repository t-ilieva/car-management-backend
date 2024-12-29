package spring.ms.cars.rest.transformer;

import spring.ms.cars.entity.Garage;
import spring.ms.cars.rest.request.GarageRequest;
import spring.ms.cars.rest.response.GarageResponse;

public class GarageTransformer {

    public static GarageResponse toResponse(Garage garage){
        GarageResponse garageResponse = new GarageResponse();
        garageResponse.setId(garage.getId());
        garageResponse.setName(garage.getName());
        garageResponse.setLocation(garage.getLocation());
        garageResponse.setCity(garage.getCity());
        garageResponse.setCapacity(garage.getCapacity());

        return garageResponse;
    }

    public static Garage toEntity(GarageRequest garageRequest){
        Garage garage = new Garage();
        garage.setName(garageRequest.getName());
        garage.setLocation(garageRequest.getLocation());
        garage.setCity(garageRequest.getCity());
        garage.setCapacity(garageRequest.getCapacity());

        return garage;
    }

    public static Garage toEntity(GarageResponse garageResponse){
        Garage garage = new Garage();
        garage.setId(garageResponse.getId());
        garage.setName(garageResponse.getName());
        garage.setLocation(garageResponse.getLocation());
        garage.setCity(garageResponse.getCity());
        garage.setCapacity(garageResponse.getCapacity());

        return garage;
    }
}
