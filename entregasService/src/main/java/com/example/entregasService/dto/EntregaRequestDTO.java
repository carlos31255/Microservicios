package com.example.entregasService.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntregaRequestDTO {
    
    @NotNull(message = "El ID de la boleta es obligatorio")
    private Integer idBoleta;
    
    private Long idTransportista;
    
    private String estadoEntrega;
    
    private String observacion;
}
