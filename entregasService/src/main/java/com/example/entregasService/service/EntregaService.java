package com.example.entregasService.service;

import com.example.entregasService.dto.EntregaDTO;
import com.example.entregasService.dto.EntregaRequestDTO;
import com.example.entregasService.dto.externo.BoletaExternaDTO;
import com.example.entregasService.dto.externo.ClienteExternoDTO;
import com.example.entregasService.model.Entrega;
import com.example.entregasService.repository.EntregaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
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
    // Inyectar los clientes WebClient pre-configurados
    @Autowired
    @Qualifier("ventasWebClient")
    private WebClient ventasWebClient;

    @Autowired
    @Qualifier("usuarioWebClient")
    private WebClient usuarioWebClient;

    
    public List<EntregaDTO> obtenerTodasLasEntregas() {
        return entregaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public EntregaDTO obtenerEntregaPorId(Integer id) {
        Entrega entrega = entregaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Entrega no encontrada: " + id));
        return toDTO(entrega);
    }

    public EntregaDTO crearEntrega(EntregaRequestDTO requestDTO) {
        Entrega entrega = toEntity(requestDTO);
        // Validaciones adicionales podrían ir aquí
        return toDTO(entregaRepository.save(entrega));
    }

    // Buscar por Transportista
    public List<EntregaDTO> getEntregasByTransportista(Integer idTransportista) {
        return entregaRepository.findByIdTransportista(idTransportista).stream()
                .map(this::toDTO) // Usamos el toDTO inteligente que creamos antes
                .collect(Collectors.toList());
    }

    // Buscar por Estado
    public List<EntregaDTO> getEntregasByEstado(String estado) {
        return entregaRepository.findByEstadoEntrega(estado).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // Asignar Transportista
    public EntregaDTO asignarTransportista(Integer idEntrega, Integer idTransportista) {
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));

        entrega.setIdTransportista(idTransportista);
        entrega.setEstadoEntrega("asignada"); // Cambiamos estado automáticamente
        entrega.setFechaAsignacion(LocalDateTime.now());

        return toDTO(entregaRepository.save(entrega));
    }

    // Completar Entrega (Con observación)
    public EntregaDTO completarEntrega(Integer idEntrega, String observacion) {
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));

        entrega.setEstadoEntrega("completada"); // O "entregada", asegúrate que coincida con tu filtro en Android
        entrega.setObservacion(observacion);
        entrega.setFechaEntrega(LocalDateTime.now());

        return toDTO(entregaRepository.save(entrega));
    }

    // Cambiar Estado Genérico
    public EntregaDTO cambiarEstado(Integer idEntrega, String nuevoEstado) {
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new RuntimeException("Entrega no encontrada"));

        entrega.setEstadoEntrega(nuevoEstado);
        return toDTO(entregaRepository.save(entrega));
    }

    // Conversión Entidad a DTO con enriquecimiento externo 
    private EntregaDTO toDTO(Entrega entrega) {
        if (entrega == null) return null;

        EntregaDTO dto = new EntregaDTO();
        // 1. Mapeo Local
        dto.setIdEntrega(entrega.getIdEntrega());
        dto.setIdBoleta(entrega.getIdBoleta());
        dto.setIdTransportista(entrega.getIdTransportista());
        dto.setEstadoEntrega(entrega.getEstadoEntrega());
        dto.setFechaAsignacion(entrega.getFechaAsignacion());
        dto.setFechaEntrega(entrega.getFechaEntrega());
        dto.setObservacion(entrega.getObservacion());
        dto.setDireccionEntrega(entrega.getDireccionEntrega());
        dto.setIdComuna(entrega.getIdComuna());

        // Valores por defecto para evitar NullPointerException en el Front
        dto.setTotalBoleta(0); 
        dto.setNombreCliente("Cargando...");
        dto.setTelefonoCliente("No disponible");

        // Llamadas Externas Protegidas
        try {
            // LLAMADA A VENTAS
            BoletaExternaDTO boleta = ventasWebClient.get()
                    .uri("/api/ventas/boletas/" + entrega.getIdBoleta())
                    .retrieve()
                    .bodyToMono(BoletaExternaDTO.class)
                    .block(Duration.ofSeconds(2));

            if (boleta != null) {
                dto.setTotalBoleta(boleta.getTotal());

                // LLAMADA A USUARIOS (Solo si tenemos clienteId)
                if (boleta.getClienteId() != null) {
                    try {
                        ClienteExternoDTO cliente = usuarioWebClient.get()
                                .uri("/api/clientes/" + boleta.getClienteId())
                                .retrieve()
                                .bodyToMono(ClienteExternoDTO.class)
                                .block(Duration.ofSeconds(2));

                        if (cliente != null) {
                            dto.setNombreCliente(cliente.getNombreCompleto());
                            dto.setTelefonoCliente(cliente.getTelefono());
                        }
                    } catch (WebClientResponseException.NotFound ex) {
                        log.warn("Cliente ID {} no encontrado en UsuarioService", boleta.getClienteId());
                        dto.setNombreCliente("Cliente no encontrado (ID: " + boleta.getClienteId() + ")");
                    } catch (Exception e) {
                        log.error("Error conectando con UsuarioService: {}", e.getMessage());
                        dto.setNombreCliente("Error serv. usuarios");
                    }
                } else {
                    dto.setNombreCliente("Boleta sin cliente asociado");
                }
            }

        } catch (WebClientResponseException.NotFound ex) {
            // En caso de entrega para boleta inexistente
            log.error("Inconsistencia: Boleta {} no existe en VentasService", entrega.getIdBoleta());
            dto.setNombreCliente("ERROR: Boleta no existe");
            dto.setTotalBoleta(0);
        } catch (WebClientResponseException ex) {
            // Errores 5xx del servidor de ventas
            log.error("Error servidor Ventas: {}", ex.getStatusCode());
            dto.setNombreCliente("Sistema Ventas no disponible");
        } catch (Exception e) {
            // Timeouts u otros errores de red
            log.error("Error de conexión al enriquecer entrega {}: {}", entrega.getIdEntrega(), e.getMessage());
            dto.setNombreCliente("Error de conexión");
        }

        return dto;
    }


    private Entrega toEntity(EntregaRequestDTO request) {
        if (request == null) return null;
        Entrega e = new Entrega();
        e.setIdBoleta(request.getIdBoleta());
        e.setIdTransportista(request.getIdTransportista());
        e.setEstadoEntrega(request.getEstadoEntrega() != null && !request.getEstadoEntrega().isEmpty()
                ? request.getEstadoEntrega()
                : "pendiente");
        e.setObservacion(request.getObservacion());
        e.setFechaAsignacion(LocalDateTime.now());
        return e;
    }
   
}
