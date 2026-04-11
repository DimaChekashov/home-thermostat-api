package com.example.home_thermostat_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "temperatures")
public class Temperature {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Double temperature;
    
}
