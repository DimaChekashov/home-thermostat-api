package com.example.home_thermostat_api.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double targetTemperature;

    @ManyToOne
    @JoinColumn(name = "home_id")
    private Home home;

    @OneToMany(mappedBy = "room")
    private List<TemperatureReading> temperatureReadings;

}