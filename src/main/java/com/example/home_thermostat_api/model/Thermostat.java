package com.example.home_thermostat_api.model;

import java.util.ArrayList;
import java.util.List;

import com.example.home_thermostat_api.enums.ThermostatModes;
import com.example.home_thermostat_api.enums.ThermostatStatuses;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Thermostat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double targetTemperature = 22.00;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ThermostatModes mode = ThermostatModes.OFF;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ThermostatStatuses status = ThermostatStatuses.ACTIVE;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id", nullable = false, unique = true)
    @JsonIgnore
    private Room room;

    @OneToMany(mappedBy = "thermostat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<TemperatureReading> temperatureReadings = new ArrayList<>();

    public Thermostat() {
    }

    public Thermostat(Long id, Double targetTemperature, ThermostatModes mode, ThermostatStatuses status, Room room,
            List<TemperatureReading> temperatureReadings) {
        this.id = id;
        this.targetTemperature = targetTemperature;
        this.mode = mode;
        this.status = status;
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

    public ThermostatStatuses getSatus() {
        return status;
    }

    public void setStatus(ThermostatStatuses status) {
        this.status = status;
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
