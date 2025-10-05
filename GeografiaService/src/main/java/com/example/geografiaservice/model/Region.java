package com.example.geografiaservice.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "regiones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Region {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 10)
    private String codigo; // RM, etc.

    @Column(name = "orden")
    private Integer orden;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    private List<Ciudad> ciudades;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL)
    private List<Comuna> comunas;
}
