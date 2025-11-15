package com.example.entregasService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntregaDTO {
    
    private Long idEntrega;
    private Integer idBoleta;
    private Long idTransportista;
    private String estadoEntrega;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaEntrega;
    private String observacion;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
}
