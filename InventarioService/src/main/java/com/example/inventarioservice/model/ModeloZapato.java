package com.example.inventarioservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "modelozapato")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModeloZapato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "id_marca", nullable = false)
    private Long marcaId;

    @Column(length = 255)
    private String descripcion;

}
