package com.example.ventasservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrearBoletaRequest {
    private Long clienteId;
    private String metodoPago;
    private String observaciones;
    private List<DetalleBoletaDTO> detalles;
}
