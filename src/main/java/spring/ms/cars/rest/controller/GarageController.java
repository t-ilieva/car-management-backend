package spring.ms.cars.rest.controller;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.ms.cars.rest.request.GarageRequest;
import spring.ms.cars.rest.response.GarageResponse;
import spring.ms.cars.services.GarageService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/garages")
public class GarageController {

    private final GarageService garageService;

    public GarageController(GarageService garageService) {
        this.garageService = garageService;
    }

    @GetMapping
    public List<GarageResponse> getAllGarages(@RequestParam(required = false) String city) {
        if (city != null && !city.isEmpty()) {
            return garageService.getByCity(city);
        } else {
            return garageService.getAll();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GarageResponse> getById(@PathVariable("id") int id) {
        Optional<GarageResponse> garageResponse = garageService.getById(id);
        if (garageResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(garageResponse.get());
        }
    }

    @PostMapping
    public void save(@RequestBody GarageRequest garageRequest) {
        int id = garageService.create(garageRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GarageResponse> updateGarage(
            @PathVariable int id,
            @RequestBody GarageRequest garageRequest) {
        Optional<GarageResponse> existingGarage = garageService.getById(id);

        if (existingGarage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        garageService.update(id, garageRequest);
        return ResponseEntity.ok(garageService.getById(id).orElseThrow());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GarageResponse> delete(@PathVariable("id") int id){
        garageService.delete(id);

        return ResponseEntity.
                noContent().
                build();
    }

}
