package com.example.UsuarioService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
    private Long idPersona;
    private String categoria;
    private String nombreCompleto;
    private String email;
    private String telefono;
}
