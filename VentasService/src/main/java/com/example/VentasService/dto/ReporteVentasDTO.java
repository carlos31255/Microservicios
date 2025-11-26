package com.example.VentasService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteVentasDTO {
    private int numeroVentasRealizadas;
    private int numeroVentasCanceladas;
    private int ingresosTotal;
    private List<DetalleVentaReporteDTO> detallesVentas;
}
