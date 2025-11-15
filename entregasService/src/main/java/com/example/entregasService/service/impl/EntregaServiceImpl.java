package com.example.entregasService.service.impl;

import com.example.entregasService.dto.ActualizarEstadoDTO;
import com.example.entregasService.dto.AsignarTransportistaDTO;
import com.example.entregasService.dto.EntregaDTO;
import com.example.entregasService.dto.EntregaRequestDTO;
import com.example.entregasService.entity.Entrega;
import com.example.entregasService.exception.EntregaNotFoundException;
import com.example.entregasService.mapper.EntregaMapper;
import com.example.entregasService.repository.EntregaRepository;
import com.example.entregasService.service.EntregaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EntregaServiceImpl implements EntregaService {

    private final EntregaRepository entregaRepository;
    private final EntregaMapper entregaMapper;

    @Override
    public EntregaDTO crearEntrega(EntregaRequestDTO requestDTO) {
        log.info("Creando nueva entrega para boleta: {}", requestDTO.getIdBoleta());
        
        // Verificar si ya existe una entrega para esta boleta
        if (entregaRepository.existsByIdBoleta(requestDTO.getIdBoleta())) {
            throw new IllegalArgumentException("Ya existe una entrega para la boleta: " + requestDTO.getIdBoleta());
        }
        
        Entrega entrega = entregaMapper.toEntity(requestDTO);
        Entrega entregaGuardada = entregaRepository.save(entrega);
        
        log.info("Entrega creada exitosamente con ID: {}", entregaGuardada.getIdEntrega());
        return entregaMapper.toDTO(entregaGuardada);
    }

    @Override
    @Transactional(readOnly = true)
    public EntregaDTO obtenerEntregaPorId(Long id) {
        log.info("Buscando entrega con ID: {}", id);
        Entrega entrega = entregaRepository.findById(id)
                .orElseThrow(() -> new EntregaNotFoundException("Entrega no encontrada con ID: " + id));
        return entregaMapper.toDTO(entrega);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntregaDTO> obtenerTodasLasEntregas() {
        log.info("Obteniendo todas las entregas");
        return entregaRepository.findAll().stream()
                .map(entregaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EntregaDTO actualizarEntrega(Long id, EntregaRequestDTO requestDTO) {
        log.info("Actualizando entrega con ID: {}", id);
        
        Entrega entrega = entregaRepository.findById(id)
                .orElseThrow(() -> new EntregaNotFoundException("Entrega no encontrada con ID: " + id));
        
        entregaMapper.updateEntityFromDTO(requestDTO, entrega);
        Entrega entregaActualizada = entregaRepository.save(entrega);
        
        log.info("Entrega actualizada exitosamente con ID: {}", id);
        return entregaMapper.toDTO(entregaActualizada);
    }

    @Override
    public void eliminarEntrega(Long id) {
        log.info("Eliminando entrega con ID: {}", id);
        
        if (!entregaRepository.existsById(id)) {
            throw new EntregaNotFoundException("Entrega no encontrada con ID: " + id);
        }
        
        entregaRepository.deleteById(id);
        log.info("Entrega eliminada exitosamente con ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public EntregaDTO obtenerEntregaPorBoleta(Integer idBoleta) {
        log.info("Buscando entrega para boleta: {}", idBoleta);
        Entrega entrega = entregaRepository.findByIdBoleta(idBoleta)
                .orElseThrow(() -> new EntregaNotFoundException("No se encontró entrega para la boleta: " + idBoleta));
        return entregaMapper.toDTO(entrega);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntregaDTO> obtenerEntregasPorTransportista(Long idTransportista) {
        log.info("Obteniendo entregas del transportista: {}", idTransportista);
        return entregaRepository.findByIdTransportista(idTransportista).stream()
                .map(entregaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntregaDTO> obtenerEntregasPorEstado(String estado) {
        log.info("Obteniendo entregas con estado: {}", estado);
        return entregaRepository.findByEstadoEntrega(estado).stream()
                .map(entregaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntregaDTO> obtenerEntregasPendientesDeAsignacion() {
        log.info("Obteniendo entregas pendientes de asignación");
        return entregaRepository.findEntregasPendientesDeAsignacion().stream()
                .map(entregaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntregaDTO> obtenerEntregasActivas() {
        log.info("Obteniendo entregas activas");
        return entregaRepository.findEntregasActivas().stream()
                .map(entregaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EntregaDTO asignarTransportista(Long idEntrega, AsignarTransportistaDTO asignarDTO) {
        log.info("Asignando transportista {} a entrega {}", asignarDTO.getIdTransportista(), idEntrega);
        
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new EntregaNotFoundException("Entrega no encontrada con ID: " + idEntrega));
        
        entrega.setIdTransportista(asignarDTO.getIdTransportista());
        entrega.setFechaAsignacion(LocalDateTime.now());
        
        if (asignarDTO.getObservacion() != null && !asignarDTO.getObservacion().isEmpty()) {
            entrega.setObservacion(asignarDTO.getObservacion());
        }
        
        // Si estaba pendiente, cambiar a "en_camino"
        if ("pendiente".equals(entrega.getEstadoEntrega())) {
            entrega.setEstadoEntrega("en_camino");
        }
        
        Entrega entregaActualizada = entregaRepository.save(entrega);
        log.info("Transportista asignado exitosamente a entrega {}", idEntrega);
        
        return entregaMapper.toDTO(entregaActualizada);
    }

    @Override
    public EntregaDTO actualizarEstado(Long idEntrega, ActualizarEstadoDTO estadoDTO) {
        log.info("Actualizando estado de entrega {} a {}", idEntrega, estadoDTO.getEstadoEntrega());
        
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new EntregaNotFoundException("Entrega no encontrada con ID: " + idEntrega));
        
        entrega.setEstadoEntrega(estadoDTO.getEstadoEntrega());
        
        if (estadoDTO.getObservacion() != null && !estadoDTO.getObservacion().isEmpty()) {
            String observacionActual = entrega.getObservacion() != null ? entrega.getObservacion() + " | " : "";
            entrega.setObservacion(observacionActual + estadoDTO.getObservacion());
        }
        
        // Si el estado es "entregada", registrar la fecha de entrega
        if ("entregada".equals(estadoDTO.getEstadoEntrega())) {
            entrega.setFechaEntrega(LocalDateTime.now());
        }
        
        Entrega entregaActualizada = entregaRepository.save(entrega);
        log.info("Estado de entrega {} actualizado exitosamente", idEntrega);
        
        return entregaMapper.toDTO(entregaActualizada);
    }

    @Override
    public EntregaDTO completarEntrega(Long idEntrega, String observacion) {
        log.info("Completando entrega {}", idEntrega);
        
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new EntregaNotFoundException("Entrega no encontrada con ID: " + idEntrega));
        
        entrega.setEstadoEntrega("entregada");
        entrega.setFechaEntrega(LocalDateTime.now());
        
        if (observacion != null && !observacion.isEmpty()) {
            String observacionActual = entrega.getObservacion() != null ? entrega.getObservacion() + " | " : "";
            entrega.setObservacion(observacionActual + observacion);
        }
        
        Entrega entregaActualizada = entregaRepository.save(entrega);
        log.info("Entrega {} completada exitosamente", idEntrega);
        
        return entregaMapper.toDTO(entregaActualizada);
    }

    @Override
    public EntregaDTO cancelarEntrega(Long idEntrega, String observacion) {
        log.info("Cancelando entrega {}", idEntrega);
        
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new EntregaNotFoundException("Entrega no encontrada con ID: " + idEntrega));
        
        entrega.setEstadoEntrega("cancelada");
        
        if (observacion != null && !observacion.isEmpty()) {
            String observacionActual = entrega.getObservacion() != null ? entrega.getObservacion() + " | " : "";
            entrega.setObservacion(observacionActual + "CANCELADA: " + observacion);
        }
        
        Entrega entregaActualizada = entregaRepository.save(entrega);
        log.info("Entrega {} cancelada exitosamente", idEntrega);
        
        return entregaMapper.toDTO(entregaActualizada);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarEntregasPorEstado(String estado) {
        log.info("Contando entregas con estado: {}", estado);
        return entregaRepository.countByEstadoEntrega(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public Long contarEntregasPorTransportista(Long idTransportista) {
        log.info("Contando entregas del transportista: {}", idTransportista);
        return entregaRepository.countByIdTransportista(idTransportista);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntregaDTO> buscarPorFechaAsignacion(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.info("Buscando entregas por fecha de asignación entre {} y {}", fechaInicio, fechaFin);
        return entregaRepository.findByFechaAsignacionBetween(fechaInicio, fechaFin).stream()
                .map(entregaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EntregaDTO> buscarPorFechaEntrega(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.info("Buscando entregas por fecha de entrega entre {} y {}", fechaInicio, fechaFin);
        return entregaRepository.findByFechaEntregaBetween(fechaInicio, fechaFin).stream()
                .map(entregaMapper::toDTO)
                .collect(Collectors.toList());
    }
}
