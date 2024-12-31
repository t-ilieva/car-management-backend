package spring.ms.cars.rest.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MaintenanceRequest {

    @NotNull(message = "Car ID cannot be null")
    @Min(value = 1, message = "Car ID must be a positive integer")
    private int carId;

    @NotBlank(message = "Service type is required")
    private String serviceType;

    @NotBlank(message = "Scheduled date is required")
    private String scheduledDate;

    @NotNull(message = "Garage ID cannot be null")
    @Min(value = 1, message = "Garage ID must be a positive integer")
    private int garageId;

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public int getGarageId() {
        return garageId;
    }

    public void setGarageId(int garageId) {
        this.garageId = garageId;
    }
}
