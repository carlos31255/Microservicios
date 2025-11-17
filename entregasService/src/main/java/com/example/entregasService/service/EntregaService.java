package com.example.entregasService.service;

import com.example.entregasService.dto.ActualizarEstadoDTO;
import com.example.entregasService.dto.AsignarTransportistaDTO;
import com.example.entregasService.dto.EntregaDTO;
import com.example.entregasService.dto.EntregaRequestDTO;
import com.example.entregasService.model.Entrega;
import com.example.entregasService.repository.EntregaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class EntregaService {
    
    @Autowired
    private EntregaRepository entregaRepository;

    // CRUD básico
    public EntregaDTO crearEntrega(EntregaRequestDTO requestDTO) {
        log.info("Creando nueva entrega para boleta: {}", requestDTO.getIdBoleta());

        if (entregaRepository.existsByIdBoleta(requestDTO.getIdBoleta())) {
            throw new IllegalArgumentException("Ya existe una entrega para la boleta: " + requestDTO.getIdBoleta());
        }

        Entrega entrega = toEntity(requestDTO);
        Entrega entregaGuardada = entregaRepository.save(entrega);

        log.info("Entrega creada exitosamente con ID: {}", entregaGuardada.getIdEntrega());
        return toDTO(entregaGuardada);
    }

    public EntregaDTO obtenerEntregaPorId(Integer id) {
        log.info("Buscando entrega con ID: {}", id);
        Entrega entrega = entregaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Entrega no encontrada con ID: " + id));
        return toDTO(entrega);
    }

    public List<EntregaDTO> obtenerTodasLasEntregas() {
        log.info("Obteniendo todas las entregas");
        return entregaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public EntregaDTO actualizarEntrega(Integer id, EntregaRequestDTO requestDTO) {
        log.info("Actualizando entrega con ID: {}", id);

        Entrega entrega = entregaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Entrega no encontrada con ID: " + id));

        updateEntityFromRequest(requestDTO, entrega);
        Entrega entregaActualizada = entregaRepository.save(entrega);

        log.info("Entrega actualizada exitosamente con ID: {}", id);
        return toDTO(entregaActualizada);
    }

    public void eliminarEntrega(Integer id) {
        log.info("Eliminando entrega con ID: {}", id);

        if (!entregaRepository.existsById(id)) {
            throw new NoSuchElementException("Entrega no encontrada con ID: " + id);
        }

        entregaRepository.deleteById(id);
        log.info("Entrega eliminada exitosamente con ID: {}", id);
    }

    // Consultas especializadas
    public EntregaDTO obtenerEntregaPorBoleta(Integer idBoleta) {
        log.info("Buscando entrega para boleta: {}", idBoleta);
        Entrega entrega = entregaRepository.findByIdBoleta(idBoleta)
                .orElseThrow(() -> new NoSuchElementException("No se encontró entrega para la boleta: " + idBoleta));
        return toDTO(entrega);
    }

    public List<EntregaDTO> obtenerEntregasPorTransportista(Integer idTransportista) {
        log.info("Obteniendo entregas del transportista: {}", idTransportista);
        return entregaRepository.findByIdTransportista(idTransportista).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<EntregaDTO> obtenerEntregasPorEstado(String estado) {
        log.info("Obteniendo entregas con estado: {}", estado);
        return entregaRepository.findByEstadoEntrega(estado).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<EntregaDTO> obtenerEntregasPendientesDeAsignacion() {
        log.info("Obteniendo entregas pendientes de asignación");
        return entregaRepository.findEntregasPendientesDeAsignacion().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<EntregaDTO> obtenerEntregasActivas() {
        log.info("Obteniendo entregas activas");
        return entregaRepository.findEntregasActivas().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Operaciones de negocio
    public EntregaDTO asignarTransportista(Integer idEntrega, AsignarTransportistaDTO asignarDTO) {
        log.info("Asignando transportista {} a entrega {}", asignarDTO.getIdTransportista(), idEntrega);

        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new NoSuchElementException("Entrega no encontrada con ID: " + idEntrega));

        entrega.setIdTransportista(asignarDTO.getIdTransportista());
        entrega.setFechaAsignacion(LocalDateTime.now());

        if (asignarDTO.getObservacion() != null && !asignarDTO.getObservacion().isEmpty()) {
            entrega.setObservacion(asignarDTO.getObservacion());
        }

        if ("pendiente".equals(entrega.getEstadoEntrega())) {
            entrega.setEstadoEntrega("en_camino");
        }

        Entrega entregaActualizada = entregaRepository.save(entrega);
        log.info("Transportista asignado exitosamente a entrega {}", idEntrega);

        return toDTO(entregaActualizada);
    }

    public EntregaDTO actualizarEstado(Integer idEntrega, ActualizarEstadoDTO estadoDTO) {
        log.info("Actualizando estado de entrega {} a {}", idEntrega, estadoDTO.getEstadoEntrega());

        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new NoSuchElementException("Entrega no encontrada con ID: " + idEntrega));

        entrega.setEstadoEntrega(estadoDTO.getEstadoEntrega());

        if (estadoDTO.getObservacion() != null && !estadoDTO.getObservacion().isEmpty()) {
            String observacionActual = entrega.getObservacion() != null ? entrega.getObservacion() + " | " : "";
            entrega.setObservacion(observacionActual + estadoDTO.getObservacion());
        }

        if ("entregada".equals(estadoDTO.getEstadoEntrega())) {
            entrega.setFechaEntrega(LocalDateTime.now());
        }

        Entrega entregaActualizada = entregaRepository.save(entrega);
        log.info("Estado de entrega {} actualizado exitosamente", idEntrega);

        return toDTO(entregaActualizada);
    }

    public EntregaDTO completarEntrega(Integer idEntrega, String observacion) {
        log.info("Completando entrega {}", idEntrega);

        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new NoSuchElementException("Entrega no encontrada con ID: " + idEntrega));

        entrega.setEstadoEntrega("entregada");
        entrega.setFechaEntrega(LocalDateTime.now());

        if (observacion != null && !observacion.isEmpty()) {
            String observacionActual = entrega.getObservacion() != null ? entrega.getObservacion() + " | " : "";
            entrega.setObservacion(observacionActual + observacion);
        }

        Entrega entregaActualizada = entregaRepository.save(entrega);
        log.info("Entrega {} completada exitosamente", idEntrega);

        return toDTO(entregaActualizada);
    }

    public EntregaDTO cancelarEntrega(Integer idEntrega, String observacion) {
        log.info("Cancelando entrega {}", idEntrega);

        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new NoSuchElementException("Entrega no encontrada con ID: " + idEntrega));

        entrega.setEstadoEntrega("cancelada");

        if (observacion != null && !observacion.isEmpty()) {
            String observacionActual = entrega.getObservacion() != null ? entrega.getObservacion() + " | " : "";
            entrega.setObservacion(observacionActual + "CANCELADA: " + observacion);
        }

        Entrega entregaActualizada = entregaRepository.save(entrega);
        log.info("Entrega {} cancelada exitosamente", idEntrega);

        return toDTO(entregaActualizada);
    }

    // Estadísticas
    public Long contarEntregasPorEstado(String estado) {
        log.info("Contando entregas con estado: {}", estado);
        return entregaRepository.countByEstadoEntrega(estado);
    }

    public Long contarEntregasPorTransportista(Integer idTransportista) {
        log.info("Contando entregas del transportista: {}", idTransportista);
        return entregaRepository.countByIdTransportista(idTransportista);
    }

    // Búsquedas por fecha
    public List<EntregaDTO> buscarPorFechaAsignacion(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.info("Buscando entregas por fecha de asignación entre {} y {}", fechaInicio, fechaFin);
        return entregaRepository.findByFechaAsignacionBetween(fechaInicio, fechaFin).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<EntregaDTO> buscarPorFechaEntrega(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        log.info("Buscando entregas por fecha de entrega entre {} y {}", fechaInicio, fechaFin);
        return entregaRepository.findByFechaEntregaBetween(fechaInicio, fechaFin).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // --- Helper mapping methods (manual mapping) ---
    private EntregaDTO toDTO(Entrega entrega) {
        if (entrega == null) return null;
        EntregaDTO dto = new EntregaDTO();
        dto.setIdEntrega(entrega.getIdEntrega());
        dto.setIdBoleta(entrega.getIdBoleta());
        dto.setIdTransportista(entrega.getIdTransportista());
        dto.setEstadoEntrega(entrega.getEstadoEntrega());
        dto.setFechaAsignacion(entrega.getFechaAsignacion());
        dto.setFechaEntrega(entrega.getFechaEntrega());
        dto.setObservacion(entrega.getObservacion());
        return dto;
    }

    private Entrega toEntity(EntregaRequestDTO request) {
        if (request == null) return null;
        Entrega e = new Entrega();
        e.setIdBoleta(request.getIdBoleta());
        e.setIdTransportista(request.getIdTransportista());
        e.setEstadoEntrega(request.getEstadoEntrega() != null && !request.getEstadoEntrega().isEmpty()
                ? request.getEstadoEntrega() : "pendiente");
        e.setObservacion(request.getObservacion());
        e.setFechaAsignacion(LocalDateTime.now());
        return e;
    }

    private void updateEntityFromRequest(EntregaRequestDTO request, Entrega e) {
        if (request == null || e == null) return;
        if (request.getIdTransportista() != null) {
            e.setIdTransportista(request.getIdTransportista());
        }
        if (request.getEstadoEntrega() != null && !request.getEstadoEntrega().isEmpty()) {
            e.setEstadoEntrega(request.getEstadoEntrega());
        }
        if (request.getObservacion() != null) {
            e.setObservacion(request.getObservacion());
        }
    }
}
