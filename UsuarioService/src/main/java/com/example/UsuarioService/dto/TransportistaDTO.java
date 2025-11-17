package com.example.UsuarioService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransportistaDTO {
    private Long idTransportista;
    private Long idPersona;
    private String patente;
    private String tipoVehiculo;
    private Boolean activo;
    private Long fechaRegistro;
}
