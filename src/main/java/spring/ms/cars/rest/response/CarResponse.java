package spring.ms.cars.rest.response;

import java.util.List;

public class CarResponse {

    private int id;
    private String make;
    private String model;
    private int productionYear;
    private String licensePlate;
    private List<GarageResponse> garages;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public List<GarageResponse> getGarages() {
        return garages;
    }

    public void setGarages(List<GarageResponse> garages) {
        this.garages = garages;
    }
}
