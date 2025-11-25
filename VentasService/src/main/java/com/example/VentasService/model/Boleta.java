package com.example.VentasService.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "boletas")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Boleta")
public class Boleta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "id_cliente", nullable = false)
    private Long clienteId;
    
    @Column(name = "fecha_venta", nullable = false)
    private LocalDateTime fechaVenta;
    
    @Column(nullable = false)
    private Integer total = 0;
    
    @Column(nullable = false, length = 50)
    private String estado; // "pendiente", "confirmada", "cancelada", "completada"
    
    @Column(name = "metodo_pago", length = 50)
    private String metodoPago; // "efectivo", "transferencia", "tarjeta"
    
    @Column(length = 500)
    private String observaciones;
    
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (estado == null) {
            estado = "pendiente";
        }
        if (fechaVenta == null) {
            fechaVenta = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
