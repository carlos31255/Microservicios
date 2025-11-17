package com.example.inventarioservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventario", 
       uniqueConstraints = @UniqueConstraint(columnNames = {"id_producto", "talla"}))
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inventario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_producto", nullable = false)
    private Long productoId;

    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false)
    private String talla;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(nullable = false)
    private Integer stockMinimo;

    @Column(name = "id_modelo", nullable = true)
    private Long modeloId;

    @Column(name = "id_talla", nullable = true)
    private Long tallaId;

}
