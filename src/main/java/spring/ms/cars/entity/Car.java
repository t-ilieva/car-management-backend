package spring.ms.cars.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "car")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

}
