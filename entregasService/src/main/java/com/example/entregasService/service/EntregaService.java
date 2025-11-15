package com.example.entregasService.service;

import com.example.entregasService.dto.ActualizarEstadoDTO;
import com.example.entregasService.dto.AsignarTransportistaDTO;
import com.example.entregasService.dto.EntregaDTO;
import com.example.entregasService.dto.EntregaRequestDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface EntregaService {

    // CRUD básico
    EntregaDTO crearEntrega(EntregaRequestDTO requestDTO);
    EntregaDTO obtenerEntregaPorId(Long id);
    List<EntregaDTO> obtenerTodasLasEntregas();
    EntregaDTO actualizarEntrega(Long id, EntregaRequestDTO requestDTO);
    void eliminarEntrega(Long id);

    // Consultas especializadas
    EntregaDTO obtenerEntregaPorBoleta(Integer idBoleta);
    List<EntregaDTO> obtenerEntregasPorTransportista(Long idTransportista);
    List<EntregaDTO> obtenerEntregasPorEstado(String estado);
    List<EntregaDTO> obtenerEntregasPendientesDeAsignacion();
    List<EntregaDTO> obtenerEntregasActivas();

    // Operaciones de negocio
    EntregaDTO asignarTransportista(Long idEntrega, AsignarTransportistaDTO asignarDTO);
    EntregaDTO actualizarEstado(Long idEntrega, ActualizarEstadoDTO estadoDTO);
    EntregaDTO completarEntrega(Long idEntrega, String observacion);
    EntregaDTO cancelarEntrega(Long idEntrega, String observacion);

    // Estadísticas
    Long contarEntregasPorEstado(String estado);
    Long contarEntregasPorTransportista(Long idTransportista);

    // Búsquedas por fecha
    List<EntregaDTO> buscarPorFechaAsignacion(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    List<EntregaDTO> buscarPorFechaEntrega(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
