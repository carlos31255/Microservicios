package com.example.UsuarioService.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioCompleto {
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
    private Long idRol;
    private String nombreRol;
    private Boolean activo = true;


    @Override
    public String toString() {
        return "UsuarioCompleto{" +
                "idPersona=" + idPersona +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", rut='" + rut + '\'' +
                ", telefono='" + telefono + '\'' +
                ", email='" + email + '\'' +
                ", idComuna=" + idComuna +
                ", calle='" + calle + '\'' +
                ", numeroPuerta='" + numeroPuerta + '\'' +
                ", username='" + username + '\'' +
                ", fechaRegistro=" + fechaRegistro +
                ", estado='" + estado + '\'' +
                ", idRol=" + idRol +
                ", nombreRol='" + nombreRol + '\'' +
                ", activo=" + activo +
                '}';
    }
}
