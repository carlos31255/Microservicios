package com.example.inventarioservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoEstadisticasDTO {
    public Long productoId;
    public String nombreProducto;
    public Integer totalTallas;
    public Integer stockTotal;
    public List<TallaDetalleDTO> detallePorTalla;
    public Long totalMovimientos;
}
