package com.example.inventarioservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoInventarioDTO {
    private Long id;
    private Long inventarioId;
    private String tipo;
    private Integer cantidad;
    private String motivo;
    private Long usuarioId;
}
