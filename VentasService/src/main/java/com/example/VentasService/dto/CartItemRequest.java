package com.example.ventasservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequest {
    private Long id;
    private Long clienteId;
    private Long modeloId;
    private Long tallaId;
    private String talla; // optional legacy label
    private Integer cantidad;
    private Integer precioUnitario;
    private String nombreProducto;

  
}
