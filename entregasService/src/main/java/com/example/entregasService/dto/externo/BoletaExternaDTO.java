package com.example.entregasService.dto.externo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoletaExternaDTO {
    // mapear los campos relevantes de la boleta externa(ventasService)
    private Integer id;
    private Integer clienteId;
    private Integer total;
    private String metodoPago;
}
