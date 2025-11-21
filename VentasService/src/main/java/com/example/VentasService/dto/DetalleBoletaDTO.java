package com.example.ventasservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleBoletaDTO {
    private Long id;
    private Long boletaId;
    private Long inventarioId;
    private String nombreProducto;
    private String talla;
    private Integer cantidad;
    private Integer precioUnitario;
    private Integer subtotal;
}
