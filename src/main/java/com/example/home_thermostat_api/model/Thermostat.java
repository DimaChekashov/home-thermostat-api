package com.example.home_thermostat_api.model;

import java.util.ArrayList;
import java.util.List;

import com.example.home_thermostat_api.enums.ThermostatModes;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Thermostat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double targetTemperature;

    @Enumerated(EnumType.STRING)
    private ThermostatModes mode;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    @JsonIgnore
    private Room room;

    @OneToMany(mappedBy = "thermostat", cascade = CascadeType.ALL)
    private List<TemperatureReading> temperatureReadings = new ArrayList<>();

    public Thermostat() {
    }

    public Thermostat(Long id, Double targetTemperature, ThermostatModes mode, Room room,
            List<TemperatureReading> temperatureReadings) {
        this.id = id;
        this.targetTemperature = targetTemperature;
        this.mode = mode;
        this.room = room;
        this.temperatureReadings = temperatureReadings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTargetTemperature() {
        return targetTemperature;
    }

    public void setTargetTemperature(Double targetTemperature) {
        this.targetTemperature = targetTemperature;
    }

    public ThermostatModes getMode() {
        return mode;
    }

    public void setMode(ThermostatModes mode) {
        this.mode = mode;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public List<TemperatureReading> getTemperatureReadings() {
        return temperatureReadings;
    }

    public void setTemperatureReadings(List<TemperatureReading> temperatureReadings) {
        this.temperatureReadings = temperatureReadings;
    }
}
