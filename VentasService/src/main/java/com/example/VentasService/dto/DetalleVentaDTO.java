package com.example.VentasService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaDTO {
    private String numeroBoleta;
    private Long fecha; // epoch millis
    private String nombreCliente;
    private Integer montoTotal;
    private String estado;
}
