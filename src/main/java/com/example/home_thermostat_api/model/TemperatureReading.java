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

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TemperatureReadingSources source = TemperatureReadingSources.SENSOR;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thermostat_id", nullable = false)
    @JsonIgnore
    private Thermostat thermostat;

    public TemperatureReading() {
        this.timestamp = LocalDateTime.now();
    }

    public TemperatureReading(
            Double value,
            TemperatureReadingSources source,
            Thermostat thermostat) {
        this.value = value;
        this.source = source;
        this.thermostat = thermostat;
        this.timestamp = LocalDateTime.now();
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

    public TemperatureReadingSources getSource() {
        return source;
    }

    public void setSource(TemperatureReadingSources source) {
        this.source = source;
    }

    public Thermostat getThermostat() {
        return thermostat;
    }

    public void setThermostat(Thermostat thermostat) {
        this.thermostat = thermostat;
    }
}