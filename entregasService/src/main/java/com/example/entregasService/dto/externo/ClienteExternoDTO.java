package com.example.entregasService.dto.externo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteExternoDTO {
    private Long idPersona;
    private String nombreCompleto;
    private String email;
    private String telefono;
}
