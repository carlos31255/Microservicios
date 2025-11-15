package com.example.entregasService.mapper;

import com.example.entregasService.dto.EntregaDTO;
import com.example.entregasService.dto.EntregaRequestDTO;
import com.example.entregasService.entity.Entrega;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class EntregaMapper {

    public EntregaDTO toDTO(Entrega entrega) {
        if (entrega == null) {
            return null;
        }
        
        EntregaDTO dto = new EntregaDTO();
        dto.setIdEntrega(entrega.getIdEntrega());
        dto.setIdBoleta(entrega.getIdBoleta());
        dto.setIdTransportista(entrega.getIdTransportista());
        dto.setEstadoEntrega(entrega.getEstadoEntrega());
        dto.setFechaAsignacion(entrega.getFechaAsignacion());
        dto.setFechaEntrega(entrega.getFechaEntrega());
        dto.setObservacion(entrega.getObservacion());
        dto.setFechaCreacion(entrega.getFechaCreacion());
        dto.setFechaActualizacion(entrega.getFechaActualizacion());
        
        return dto;
    }

    public Entrega toEntity(EntregaRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        
        Entrega entrega = new Entrega();
        entrega.setIdBoleta(requestDTO.getIdBoleta());
        entrega.setIdTransportista(requestDTO.getIdTransportista());
        entrega.setEstadoEntrega(
            requestDTO.getEstadoEntrega() != null && !requestDTO.getEstadoEntrega().isEmpty() 
                ? requestDTO.getEstadoEntrega() 
                : "pendiente"
        );
        entrega.setObservacion(requestDTO.getObservacion());
        entrega.setFechaAsignacion(LocalDateTime.now());
        
        return entrega;
    }

    public void updateEntityFromDTO(EntregaRequestDTO requestDTO, Entrega entrega) {
        if (requestDTO == null || entrega == null) {
            return;
        }
        
        if (requestDTO.getIdTransportista() != null) {
            entrega.setIdTransportista(requestDTO.getIdTransportista());
        }
        
        if (requestDTO.getEstadoEntrega() != null && !requestDTO.getEstadoEntrega().isEmpty()) {
            entrega.setEstadoEntrega(requestDTO.getEstadoEntrega());
        }
        
        if (requestDTO.getObservacion() != null) {
            entrega.setObservacion(requestDTO.getObservacion());
        }
    }
}
