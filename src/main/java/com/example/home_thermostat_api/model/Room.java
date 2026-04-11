package com.example.home_thermostat_api.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JsonIgnore
    private Home home;

    @OneToMany(mappedBy = "room")
    private List<TemperatureReading> temperatureReadings = new ArrayList<>();

}