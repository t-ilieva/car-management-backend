package spring.ms.cars.rest.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.ms.cars.rest.request.GarageRequest;
import spring.ms.cars.rest.response.GarageDailyAvailabilityReportDTO;
import spring.ms.cars.rest.response.GarageResponse;
import spring.ms.cars.services.GarageService;
import spring.ms.cars.services.MaintenanceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/garages")
public class GarageController {

    @Autowired
    private final GarageService garageService;
    private final MaintenanceService maintenanceService;

    public GarageController(GarageService garageService, MaintenanceService maintenanceService) {
        this.garageService = garageService;
        this.maintenanceService = maintenanceService;
    }

    @GetMapping("/dailyAvailabilityReport")
    public ResponseEntity<List<GarageDailyAvailabilityReportDTO>> getDailyAvailabilityReport(
            @RequestParam Integer garageId,
            @RequestParam String startDate,
            @RequestParam String endDate) {

        List<GarageDailyAvailabilityReportDTO> report = maintenanceService.generateDailyAvailabilityReport(garageId, startDate, endDate);
        return ResponseEntity.ok(report);
    }

    @GetMapping
    public  ResponseEntity<List<GarageResponse>> getAll(@RequestParam(required = false) String city) {
        List<GarageResponse> garages;
        if (city != null && !city.isEmpty()) {
            garages = garageService.getByCity(city);
        } else {
            garages = garageService.getAll();
        }

        return ResponseEntity.ok(garages);
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
    public ResponseEntity<String> create(@Valid @RequestBody GarageRequest garageRequest) {
        int createdId = garageService.create(garageRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Garage created with ID " + createdId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GarageResponse> update(@PathVariable int id, @Valid @RequestBody GarageRequest garageRequest) {
        Optional<GarageResponse> existingGarage = garageService.getById(id);

        if (existingGarage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        garageService.update(id, garageRequest);
        return ResponseEntity.ok(garageService.getById(id).orElseThrow());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GarageResponse> delete(@PathVariable("id") int id){
        Optional<GarageResponse> existingGarage = garageService.getById(id);

        if (existingGarage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        garageService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
