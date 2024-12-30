package spring.ms.cars.rest.transformer;

import spring.ms.cars.entity.Maintenance;
import spring.ms.cars.rest.request.MaintenanceRequest;
import spring.ms.cars.rest.response.MaintenanceResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MaintenanceTransformer {

    private static final SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    public static MaintenanceResponse toResponse(Maintenance maintenance){
        MaintenanceResponse maintenanceResponse = new MaintenanceResponse();
        maintenanceResponse.setId(maintenance.getId());
        maintenanceResponse.setCarId(maintenance.getCar().getId());
        maintenanceResponse.setCarName(maintenance.getCar().getMake() + " " + maintenance.getCar().getModel());
        maintenanceResponse.setServiceType(maintenance.getServiceType());
        maintenanceResponse.setScheduledDate(DATE_TIME_FORMATTER.format(maintenance.getScheduledDate()));
        maintenanceResponse.setGarageId(maintenance.getGarage().getId());
        maintenanceResponse.setGarageName(maintenance.getGarage().getName());

        return maintenanceResponse;
    }

    public static Maintenance toEntity(MaintenanceRequest maintenanceRequest){
        Maintenance maintenance = new Maintenance();
        maintenance.setServiceType(maintenanceRequest.getServiceType());

        try {
            Date scheduledDate = DATE_TIME_FORMATTER.parse(maintenanceRequest.getScheduledDate());
            maintenance.setScheduledDate(scheduledDate);
        } catch (ParseException e) {
            throw new RuntimeException("Invalid date format, expected yyyy-MM-dd", e);
        }

        return maintenance;
    }
}
