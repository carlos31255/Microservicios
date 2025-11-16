package com.example.entregasService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContarEstadoDTO {
    private String estado;

    private Long cantidad;
}
