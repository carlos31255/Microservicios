package com.example.entregasService.dto.externo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransportistaExternoDTO {
    private Long idTransportista;
    private Long idPersona;
    private String patente;
    private String tipoVehiculo;
    private Boolean activo;
    private String licencia;
}
