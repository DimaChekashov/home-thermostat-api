package com.example.home_thermostat_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "home_id")
    @JsonIgnore
    private Home home;

    @OneToOne(cascade = CascadeType.ALL)
    private Thermostat thermostat;

    public Room() {
    }

    public Room(Long id, String name, Home home, Thermostat thermostat) {
        this.id = id;
        this.name = name;
        this.home = home;
        this.thermostat = thermostat;
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

    public Home getHome() {
        return home;
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public Thermostat getThermostat() {
        return thermostat;
    }

    public void setThermostat(Thermostat thermostat) {
        this.thermostat = thermostat;
    }
}
