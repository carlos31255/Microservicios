package com.example.entregasService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarEstadoDTO {
    
    @NotBlank(message = "El estado de entrega es obligatorio")
    @Pattern(regexp = "pendiente|en_camino|entregada|cancelada", 
             message = "Estado inválido. Debe ser: pendiente, en_camino, entregada o cancelada")
    private String estadoEntrega;
    
    private String observacion;
}
