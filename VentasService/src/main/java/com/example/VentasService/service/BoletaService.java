package com.example.ventasservice.service;

import com.example.ventasservice.dto.*;
import com.example.ventasservice.model.Boleta;
import com.example.ventasservice.model.DetalleBoleta;
import com.example.ventasservice.repository.BoletaRepository;
import com.example.ventasservice.repository.DetalleBoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.Duration;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BoletaService {
    
    @Autowired
    private BoletaRepository boletaRepository;
    
    @Autowired
    private DetalleBoletaRepository detalleBoletaRepository;

    @Autowired
    private org.springframework.web.reactive.function.client.WebClient.Builder webClientBuilder;

    @Value("${services.inventario.url:http://localhost:8082}")
    private String inventarioServiceUrl;

    @Value("${services.entregas.url:http://localhost:8084}")
    private String entregasServiceUrl;
    
    // Obtener todas las boletas
    public List<BoletaDTO> obtenerTodasLasBoletas() {
        return boletaRepository.findAllByOrderByFechaVentaDesc().stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Obtener boleta por ID con sus detalles
    public Optional<BoletaDTO> obtenerBoletaPorId(Integer id) {
        return boletaRepository.findById(id)
                .map(this::convertirADTOConDetalles);
    }
    
    // Obtener boletas por cliente
    public List<BoletaDTO> obtenerBoletasPorCliente(Integer clienteId) {
        return boletaRepository.findByClienteIdOrderByFechaVentaDesc(clienteId).stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Obtener detalles de una boleta
    public List<DetalleBoletaDTO> obtenerDetallesPorBoleta(Integer boletaId) {
        return detalleBoletaRepository.findByBoletaIdOrderByIdAsc(boletaId).stream()
                .map(this::convertirDetalleADTO)
                .collect(Collectors.toList());
    }
    
    // Crear nueva boleta con sus detalles
    @Transactional
    public BoletaDTO crearBoleta(CrearBoletaRequest request) {
        // Calcular total
        Integer total = request.getDetalles().stream()
                .mapToInt(d -> d.getCantidad() * d.getPrecioUnitario())
                .sum();
        
        // Crear boleta
        Boleta boleta = new Boleta();
        boleta.setClienteId(request.getClienteId());
        boleta.setTotal(total);
        boleta.setMetodoPago(request.getMetodoPago());
        boleta.setObservaciones(request.getObservaciones());
        boleta.setEstado("pendiente");
        boleta.setFechaVenta(LocalDateTime.now());
        
        Boleta boletaGuardada = boletaRepository.save(boleta);
        
        // Crear detalles
        List<DetalleBoleta> detalles = request.getDetalles().stream()
                .map(dto -> {
                    DetalleBoleta detalle = new DetalleBoleta();
                    detalle.setBoletaId(boletaGuardada.getId());
                    detalle.setInventarioId(dto.getInventarioId());
                    detalle.setNombreProducto(dto.getNombreProducto());
                    detalle.setTalla(dto.getTalla());
                    detalle.setCantidad(dto.getCantidad());
                    detalle.setPrecioUnitario(dto.getPrecioUnitario());
                    return detalle;
                })
                .collect(Collectors.toList());
        
        detalleBoletaRepository.saveAll(detalles);

        // Ajustar inventario (sincrónico) para cada detalle usando WebClient
        for (DetalleBoleta d : detalles) {
            try {
                WebClient client = webClientBuilder.baseUrl(inventarioServiceUrl).build();
                Integer delta = -Math.abs(d.getCantidad());
                java.util.Map<String, Object> body = java.util.Map.of(
                        "cantidad", delta,
                        "tipo", "salida",
                        "motivo", "venta",
                        "usuarioId", boletaGuardada.getClienteId()
                );
                client.put()
                        .uri("/api/inventario/{id}/ajustar", d.getInventarioId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body)
                        .retrieve()
                        .bodyToMono(String.class)
                        .timeout(Duration.ofSeconds(6))
                        .block(Duration.ofSeconds(7));
            } catch (Exception ex) {
                // Fallo en ajuste de inventario: revertir transacción
                throw new RuntimeException("No fue posible ajustar inventario: " + ex.getMessage(), ex);
            }
        }

        // Crear entrega en EntregasService (sincrónico, no crítico) usando WebClient
        try {
            WebClient client2 = webClientBuilder.baseUrl(entregasServiceUrl).build();
            java.util.Map<String, Object> entregaBody = java.util.Map.of(
                    "idBoleta", boletaGuardada.getId(),
                    "estadoEntrega", "pendiente"
            );
            client2.post()
                    .uri("/api/entregas")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(entregaBody)
                    .retrieve()
                    .toBodilessEntity()
                    .block(Duration.ofSeconds(5));
        } catch (Exception e) {
            // No hacemos rollback por fallo de creación de entrega, sólo logueamos
            System.err.println("Advertencia: no se pudo crear entrega: " + e.getMessage());
        }

        return convertirADTOConDetalles(boletaGuardada);
    }
    
    // Cambiar estado de boleta
    @Transactional
    public Optional<BoletaDTO> cambiarEstado(Integer id, String nuevoEstado) {
        return boletaRepository.findById(id)
                .map(boleta -> {
                    boleta.setEstado(nuevoEstado);
                    Boleta actualizada = boletaRepository.save(boleta);
                    return convertirADTO(actualizada);
                });
    }
    
    // Conversión a DTO sin detalles
    private BoletaDTO convertirADTO(Boleta boleta) {
        BoletaDTO dto = new BoletaDTO();
        dto.setId(boleta.getId());
        dto.setClienteId(boleta.getClienteId());
        dto.setFechaVenta(boleta.getFechaVenta());
        dto.setTotal(boleta.getTotal());
        dto.setEstado(boleta.getEstado());
        dto.setMetodoPago(boleta.getMetodoPago());
        dto.setObservaciones(boleta.getObservaciones());
        dto.setFechaCreacion(boleta.getFechaCreacion());
        dto.setFechaActualizacion(boleta.getFechaActualizacion());
        return dto;
    }
    
    // Conversión a DTO con detalles
    private BoletaDTO convertirADTOConDetalles(Boleta boleta) {
        BoletaDTO dto = convertirADTO(boleta);
        List<DetalleBoletaDTO> detalles = detalleBoletaRepository.findByBoletaIdOrderByIdAsc(boleta.getId())
                .stream()
                .map(this::convertirDetalleADTO)
                .collect(Collectors.toList());
        dto.setDetalles(detalles);
        return dto;
    }
    
    // Conversión DetalleBoleta a DTO
    private DetalleBoletaDTO convertirDetalleADTO(DetalleBoleta detalle) {
        DetalleBoletaDTO dto = new DetalleBoletaDTO();
        dto.setId(detalle.getId());
        dto.setBoletaId(detalle.getBoletaId());
        dto.setInventarioId(detalle.getInventarioId());
        dto.setNombreProducto(detalle.getNombreProducto());
        dto.setTalla(detalle.getTalla());
        dto.setCantidad(detalle.getCantidad());
        dto.setPrecioUnitario(detalle.getPrecioUnitario());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }
}
