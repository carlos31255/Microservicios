package com.example.ventasservice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private Long id;
    private Long clienteId;
    private Long modeloId;
    private Long tallaId;
    private Integer cantidad;
    private Integer precioUnitario;
    private String nombreProducto;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;


}
