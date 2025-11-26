package com.example.VentasService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaReporteDTO {
    private String numeroBoleta;
    private long fecha; // epoch millis
    private String nombreCliente;
    private Integer montoTotal;
    private String estado;
}
