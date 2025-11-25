package com.example.inventarioservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientosEstadisticasDTO {
    public Long totalMovimientos;
    public Long movimientosEntrada;
    public Long movimientosSalida;
    public Integer cantidadTotalEntradas;
    public Integer cantidadTotalSalidas;
    public Integer saldoMovimientos;
}
