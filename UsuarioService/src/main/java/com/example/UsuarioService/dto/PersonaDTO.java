package com.example.UsuarioService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaDTO {
    private Long idPersona;
    private String nombre;
    private String apellido;
    private String rut;
    private String telefono;
    private String email;
    private Long idComuna;
    private String calle;
    private String numeroPuerta;
    private String username;
    private Long fechaRegistro;
    private String estado;
}
