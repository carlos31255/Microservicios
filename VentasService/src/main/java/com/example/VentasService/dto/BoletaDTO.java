package com.example.ventasservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoletaDTO {
    private Integer id;
    private Integer clienteId;
    private LocalDateTime fechaVenta;
    private Integer total;
    private String estado;
    private String metodoPago;
    private String observaciones;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Lista de detalles (productos)
    private List<DetalleBoletaDTO> detalles;
}
