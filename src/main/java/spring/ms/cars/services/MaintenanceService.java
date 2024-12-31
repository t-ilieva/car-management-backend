package spring.ms.cars.services;

import org.springframework.stereotype.Service;
import spring.ms.cars.entity.Car;
import spring.ms.cars.entity.Garage;
import spring.ms.cars.entity.Maintenance;
import spring.ms.cars.repository.CarRepository;
import spring.ms.cars.repository.GarageRepository;
import spring.ms.cars.repository.MaintenanceRepository;
import spring.ms.cars.rest.request.MaintenanceRequest;
import spring.ms.cars.rest.response.GarageDailyAvailabilityReportDTO;
import spring.ms.cars.rest.response.MaintenanceResponse;
import spring.ms.cars.rest.response.MonthlyRequestsReportDTO;
import spring.ms.cars.rest.transformer.MaintenanceTransformer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
        Integer garageId = maintenanceRequest.getGarageId();
        Date scheduledDate = null;

        try {
            scheduledDate = DATE_TIME_FORMATTER.parse(maintenanceRequest.getScheduledDate());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + maintenanceRequest.getScheduledDate());
        }

        if (!isGarageAvailable(garageId, scheduledDate)) {
            throw new RuntimeException("No available capacity in the garage for the selected date.");
        }

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
        Integer garageId = maintenanceRequest.getGarageId();
        Date scheduledDate = null;

        try {
            scheduledDate = DATE_TIME_FORMATTER.parse(maintenanceRequest.getScheduledDate());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        if (!isGarageAvailable(garageId, scheduledDate)) {
            throw new RuntimeException("No available capacity in the garage for the selected date.");
        }

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

    public boolean isGarageAvailable(Integer garageId, Date scheduledDate) {
        List<Maintenance> maintenances = maintenanceRepository.findAllScheduledForGarageOnDate(garageId, scheduledDate);

        int garageCapacity = garageRepository.findById(garageId)
                .map(Garage::getCapacity)
                .orElseThrow(() -> new RuntimeException("Garage with ID " + garageId + " not found"));

        return maintenances.size() < garageCapacity;
    }

    public void delete(int id){
        maintenanceRepository.deleteById(id);
    }

    // Генериране на справката за заявките по месеци
    public List<MonthlyRequestsReportDTO> generateMonthlyReport(Integer garageId, String startDateStr, String endDateStr) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date startDate = null;
        try {
            startDate = sdf.parse(startDateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.set(Calendar.DAY_OF_MONTH, 1); // първи ден на месеца
            startDate = calendar.getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Date endDate = null;
        try {
            endDate = sdf.parse(endDateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endDate);
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)); // последен ден на месеца
            endDate = calendar.getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        List<Date> scheduledDates = maintenanceRepository.findAllScheduledDates(garageId, startDate, endDate);

        List<String> allMonths = generateAllMonths(startDate, endDate);

        Map<String, Integer> monthRequestCount = new HashMap<>();

        for (Date date : scheduledDates) {
            String yearMonth = sdf.format(date);
            System.out.println("Formatted Year-Month: " + yearMonth);
            monthRequestCount.put(yearMonth, monthRequestCount.getOrDefault(yearMonth, 0) + 1);
        }

//        System.out.println("Month Request Count:");
//        for (Map.Entry<String, Integer> entry : monthRequestCount.entrySet()) {
//            System.out.println("Month: " + entry.getKey() + ", Requests: " + entry.getValue());
//        }

        List<MonthlyRequestsReportDTO> finalReport = new ArrayList<>();
        for (String month : allMonths) {
            Integer requestCount = monthRequestCount.getOrDefault(month, 0);
            finalReport.add(new MonthlyRequestsReportDTO(month, requestCount));
        }

        return finalReport;
    }

    private List<String> generateAllMonths(Date startDate, Date endDate) {
        List<String> months = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(startDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(endDate);

        while (startCalendar.before(endCalendar) || startCalendar.equals(endCalendar)) {
            months.add(sdf.format(startCalendar.getTime()));
            startCalendar.add(Calendar.MONTH, 1);
        }

        return months;
    }

    // Генериране на справката за брой на заявките по дати и свободен капацитет
    public List<GarageDailyAvailabilityReportDTO> generateDailyAvailabilityReport(Integer garageId, String startDateStr, String endDateStr) {
        Date startDate = null;
        Date endDate = null;

        try {
            startDate = DATE_TIME_FORMATTER.parse(startDateStr);
            endDate = DATE_TIME_FORMATTER.parse(endDateStr);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format", e);
        }

        List<Maintenance> maintenances = maintenanceRepository.findAllScheduledForGarage(garageId, startDate, endDate);

        Map<String, Integer> requestCountPerDate = new HashMap<>();
        for (Maintenance maintenance : maintenances) {
            String date = DATE_TIME_FORMATTER.format(maintenance.getScheduledDate());
            requestCountPerDate.put(date, requestCountPerDate.getOrDefault(date, 0) + 1);
        }

        List<GarageDailyAvailabilityReportDTO> report = new ArrayList<>();
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(startDate);

        while (!currentDate.getTime().after(endDate)) {
            String dateStr = DATE_TIME_FORMATTER.format(currentDate.getTime());
            int requestCount = requestCountPerDate.getOrDefault(dateStr, 0);
            int availableCapacity = garageRepository.findById(garageId)
                    .map(Garage::getCapacity)
                    .orElse(0) - requestCount;

            report.add(new GarageDailyAvailabilityReportDTO(dateStr, requestCount, availableCapacity));

            currentDate.add(Calendar.DAY_OF_MONTH, 1);
        }
        return report;
    }
}
