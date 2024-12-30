package spring.ms.cars.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "garage")
public class Garage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String location;
    private String city;
    private int capacity;

    @ManyToMany(mappedBy = "garages")  // Мапва връзката, дефинирана в Car
    private Set<Car> cars = new HashSet<>();

    @OneToMany(mappedBy = "garage", cascade = CascadeType.ALL)
    private Set<Maintenance> maintenances;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    public Set<Maintenance> getMaintenances() {
        return maintenances;
    }

    public void setMaintenances(Set<Maintenance> maintenances) {
        this.maintenances = maintenances;
    }
}
