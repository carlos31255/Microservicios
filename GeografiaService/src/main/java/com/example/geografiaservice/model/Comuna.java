package com.example.geografiaservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comunas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comuna {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;


    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false)
    @JsonIgnoreProperties({"ciudades", "comunas"})
    private Region region;

    @ManyToOne
    @JoinColumn(name = "ciudad_id", nullable = false)
    @JsonIgnoreProperties({"region", "comuna"})
    private Ciudad ciudad;
}
