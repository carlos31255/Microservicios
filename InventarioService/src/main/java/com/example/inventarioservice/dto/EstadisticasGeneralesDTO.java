package com.example.inventarioservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EstadisticasGeneralesDTO {
    public Long totalProductos;
    public Long totalMovimientos;
    public Integer stockTotal;
    public Long productosConStockBajo;
    public Long productosSinStock;
    public Double promedioStockPorProducto;
}
