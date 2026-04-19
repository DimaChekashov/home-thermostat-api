package com.example.home_thermostat_api.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double targetTemperature;

    @ManyToOne
    @JoinColumn(name = "home_id")
    @JsonIgnore
    private Home home;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<TemperatureReading> temperatureReadings = new ArrayList<>();

    public Room() {
    }

    public Room(Long id, String name, Double targetTemperature, Home home,
            List<TemperatureReading> temperatureReadings) {
        this.id = id;
        this.name = name;
        this.targetTemperature = targetTemperature;
        this.home = home;
        this.temperatureReadings = temperatureReadings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(Double targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public List<TemperatureReading> getTemperatureReadings() {
        return temperatureReadings;
    }

    public void setTemperatureReadings(List<TemperatureReading> temperatureReadings) {
        this.temperatureReadings = temperatureReadings;
    }
}