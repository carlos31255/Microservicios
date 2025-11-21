package com.example.entregasService.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsignarTransportistaDTO {
    
    @NotNull(message = "El ID del transportista es obligatorio")
    private Long idTransportista;
    
    private String observacion;
}
