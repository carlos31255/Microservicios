package com.example.inventarioservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioDTO {
    private Long id;
    private Long productoId;
    private String nombre;
    private String talla;
    private Integer cantidad;
    private Integer stockMinimo;
}
