package spring.ms.cars.rest.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.ms.cars.rest.request.CarRequest;
import spring.ms.cars.rest.response.CarResponse;
import spring.ms.cars.services.CarService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class CarController {

    @Autowired
    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ResponseEntity<List<CarResponse>> getAll ( @RequestParam(required = false) String carMake,
                                                      @RequestParam(required = false) Integer fromYear,
                                                      @RequestParam(required = false) Integer toYear,
                                                      @RequestParam(required = false) Integer garageId) {

        List<CarResponse> cars = carService.getFiltered(carMake, fromYear, toYear, garageId);

        return ResponseEntity.ok(cars);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getById(@PathVariable("id") int id) {
        Optional<CarResponse> carResponse = carService.getById(id);

        if (carResponse.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok(carResponse.get());
        }
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody CarRequest carRequest) {
        int createdId = carService.create(carRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Car created with ID " + createdId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> update(@PathVariable int id, @Valid @RequestBody CarRequest carRequest) {
        Optional<CarResponse> existingCar = carService.getById(id);

        if (existingCar.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        carService.update(id, carRequest);
        return ResponseEntity.ok(carService.getById(id).orElseThrow());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CarResponse> delete(@PathVariable("id") int id){
        Optional<CarResponse> existingCar = carService.getById(id);

        if (existingCar.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        carService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
