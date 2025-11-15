package com.example.entregasService.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "entregas", indexes = {
    @Index(name = "idx_id_boleta", columnList = "id_boleta"),
    @Index(name = "idx_id_transportista", columnList = "id_transportista"),
    @Index(name = "idx_estado_entrega", columnList = "estado_entrega")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrega")
    private Long idEntrega;

    @Column(name = "id_boleta", nullable = false)
    private Integer idBoleta;

    @Column(name = "id_transportista")
    private Long idTransportista;

    @Column(name = "estado_entrega", nullable = false, length = 50)
    private String estadoEntrega = "pendiente";

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDateTime fechaAsignacion;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaActualizacion = LocalDateTime.now();
        if (fechaAsignacion == null) {
            fechaAsignacion = LocalDateTime.now();
        }
        if (estadoEntrega == null || estadoEntrega.trim().isEmpty()) {
            estadoEntrega = "pendiente";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        fechaActualizacion = LocalDateTime.now();
    }
}
