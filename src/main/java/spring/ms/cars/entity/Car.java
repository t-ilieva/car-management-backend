package spring.ms.cars.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String make;
    private String model;
    private int productionYear;
    private String licensePlate;

    @ManyToMany
    @JoinTable(
            name = "cars_garages",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "garage_id")
    )
    private Set<Garage> garages = new HashSet<>();

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private Set<Maintenance> maintenances;

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

    public Set<Garage> getGarages() {
        return garages;
    }

    public void setGarages(Set<Garage> garages) {
        this.garages = garages;
    }

    public Set<Maintenance> getMaintenances() {
        return maintenances;
    }

    public void setMaintenances(Set<Maintenance> maintenances) {
        this.maintenances = maintenances;
    }
}
