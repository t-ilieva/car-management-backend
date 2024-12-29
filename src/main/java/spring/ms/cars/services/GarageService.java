package spring.ms.cars.services;

import org.springframework.stereotype.Service;
import spring.ms.cars.entity.Garage;
import spring.ms.cars.repository.GarageRepository;
import spring.ms.cars.rest.request.GarageRequest;
import spring.ms.cars.rest.response.GarageResponse;
import spring.ms.cars.rest.transformer.GarageTransformer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GarageService {

    private final GarageRepository garageRepository;

    public GarageService(GarageRepository garageRepository) {
        this.garageRepository = garageRepository;
    }

    public List<GarageResponse> getAll(){
        return garageRepository
                .findAll()
                .stream()
                .map(GarageTransformer::toResponse)
                .collect(Collectors.toList());
    }

    public List<GarageResponse> getByCity(String city){
        return garageRepository
                .findByCity(city)
                .stream()
                .map(GarageTransformer::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<GarageResponse> getById(int id){
        return garageRepository
                .findById(id)
                .map(GarageTransformer::toResponse);
    }

    public int create(GarageRequest garageRequest){
        Garage garage = GarageTransformer
                .toEntity(garageRequest);
            return garageRepository.save(garage).getId();
    }

    public void update(int id, GarageRequest garageRequest){
        Garage garage = garageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Garage with ID " + id + " not found"));

        garage.setName(garageRequest.getName());
        garage.setLocation(garageRequest.getLocation());
        garage.setCity(garageRequest.getCity());
        garage.setCapacity(garageRequest.getCapacity());

        garageRepository.save(garage);
    }

    public void delete(int id){
        garageRepository.deleteById(id);
    }
}
