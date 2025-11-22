package com.example.UsuarioService.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private Long idPersona;
    private String username;
    private String nombreCompleto;
    private String rol;
    private String message;
    private Long idRol;
    private Boolean activo;
    private String email;
    private String telefono;
}
