package com.example.entregasService.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "entrega")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Entrega")
public class Entrega {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrega")
    private Long idEntrega;
    
    @NotNull(message = "El ID de la boleta es obligatorio")
    @Column(name = "id_boleta", nullable = false)
    private Long idBoleta;
    
    @Column(name = "id_transportista")
    private Long idTransportista;
    
    @Column(name = "estado_entrega", length = 50, nullable = false)
    private String estadoEntrega = "pendiente"; // pendiente, asignada, en_camino, entregada, cancelada
    
    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion = LocalDateTime.now();
    
    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;
    
    @Column(name = "observacion", columnDefinition = "TEXT")
    private String observacion;
    
    @Column(name = "direccion_entrega", columnDefinition = "TEXT")
    private String direccionEntrega;
    
    @Column(name = "id_comuna")
    private Long idComuna;
}
