package com.example.ventasservice.service;

import com.example.ventasservice.dto.*;
import com.example.ventasservice.model.Boleta;
import com.example.ventasservice.model.DetalleBoleta;
import com.example.ventasservice.repository.BoletaRepository;
import com.example.ventasservice.repository.DetalleBoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
