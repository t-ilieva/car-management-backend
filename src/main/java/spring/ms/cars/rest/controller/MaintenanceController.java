package spring.ms.cars.rest.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.ms.cars.rest.request.MaintenanceRequest;
import spring.ms.cars.rest.response.MaintenanceResponse;
import spring.ms.cars.rest.response.MonthlyRequestsReportDTO;
import spring.ms.cars.services.MaintenanceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {

    @Autowired
    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @GetMapping("/monthlyRequestsReport")
    public ResponseEntity<List<MonthlyRequestsReportDTO>> getMonthlyRequestsReport(@RequestParam(required = false) Integer garageId,
                                                                                   @RequestParam String startMonth,
                                                                                   @RequestParam String endMonth) {

        List<MonthlyRequestsReportDTO> report = maintenanceService.generateMonthlyReport(garageId, startMonth, endMonth);
        return ResponseEntity.ok(report);
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceResponse>> getAll (@RequestParam(required = false) Integer carId,
                                                     @RequestParam(required = false) Integer garageId,
                                                     @RequestParam(required = false) String startDate,
                                                     @RequestParam(required = false) String endDate) {

        List<MaintenanceResponse> maintenances = maintenanceService.getFiltered(carId, garageId, startDate, endDate);

        return ResponseEntity.ok(maintenances);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceResponse> getById(@PathVariable("id") int id) {
        Optional<MaintenanceResponse> maintenanceResponse = maintenanceService.getById(id);
        if (maintenanceResponse.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(maintenanceResponse.get());
        }
    }

    @PostMapping
    public ResponseEntity<String> create(@Valid @RequestBody MaintenanceRequest maintenanceRequest) {
        int createdId = maintenanceService.create(maintenanceRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Maintenance created with ID " + createdId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceResponse> update(@PathVariable int id, @Valid @RequestBody MaintenanceRequest maintenanceRequest) {
        Optional<MaintenanceResponse> existingMaintenance = maintenanceService.getById(id);

        if (existingMaintenance.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        maintenanceService.update(id, maintenanceRequest);
        return ResponseEntity.ok(maintenanceService.getById(id).orElseThrow());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MaintenanceResponse> delete(@PathVariable("id") int id){
        Optional<MaintenanceResponse> existingMaintenance = maintenanceService.getById(id);

        if (existingMaintenance.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        maintenanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
