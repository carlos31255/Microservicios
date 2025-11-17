package com.example.entregasService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EntregaDTO {
    
    private Integer idEntrega;
    private Integer idBoleta;
    private Integer idTransportista;
    private String estadoEntrega;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaEntrega;
    private String observacion;
}
