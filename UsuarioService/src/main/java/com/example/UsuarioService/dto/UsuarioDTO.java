package com.example.UsuarioService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    private Long idPersona;
    private Long idRol;
    private Boolean activo;
    private String nombreCompleto; 
    private String username;
    private String nombreRol;
}
