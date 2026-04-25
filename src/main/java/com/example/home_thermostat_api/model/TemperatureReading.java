package com.example.home_thermostat_api.model;

import java.time.LocalDateTime;

import com.example.home_thermostat_api.enums.TemperatureReadingSources;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class TemperatureReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double value;
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private TemperatureReadingSources source;

    @ManyToOne
    @JoinColumn(name = "thermostat_id")
    @JsonIgnore
    private Thermostat thermostat;

    public TemperatureReading() {
    }

    public TemperatureReading(
            Long id,
            Double value,
            LocalDateTime timestamp,
            TemperatureReadingSources source,
            Thermostat thermostat) {
        this.id = id;
        this.value = value;
        this.timestamp = timestamp;
        this.source = source;
        this.thermostat = thermostat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getValue() {
        return value;
    }

    public void setTemperature(Double value) {
        this.value = value;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Thermostat getThermostat() {
        return thermostat;
    }

    public void setThermostat(Thermostat thermostat) {
        this.thermostat = thermostat;
    }
}