package com.example.inventarioservice.service;

import com.example.inventarioservice.dto.MovimientoInventarioDTO;
import com.example.inventarioservice.model.MovimientosInventario;
import com.example.inventarioservice.repository.MovimientoInventarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MovimientoInventarioService {
    
    private final MovimientoInventarioRepository movimientoInventarioRepository;

    public MovimientoInventarioService(MovimientoInventarioRepository movimientoInventarioRepository) {
        this.movimientoInventarioRepository = movimientoInventarioRepository;
    }

    // Obtener todos los movimientos
    public List<MovimientoInventarioDTO> obtenerTodosLosMovimientos() {
        return movimientoInventarioRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener movimiento por ID
    public MovimientoInventarioDTO obtenerMovimientoPorId(Long id) {
        MovimientosInventario movimiento = movimientoInventarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con id: " + id));
        return convertirADTO(movimiento);
    }

    // Obtener movimientos por inventarioId
    public List<MovimientoInventarioDTO> obtenerMovimientosPorInventarioId(Long inventarioId) {
        return movimientoInventarioRepository.findByInventarioIdOrderByIdDesc(inventarioId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener movimientos por tipo
    public List<MovimientoInventarioDTO> obtenerMovimientosPorTipo(String tipo) {
        return movimientoInventarioRepository.findByTipo(tipo)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener movimientos por usuarioId
    public List<MovimientoInventarioDTO> obtenerMovimientosPorUsuarioId(Long usuarioId) {
        return movimientoInventarioRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Convertir entidad a DTO
    private MovimientoInventarioDTO convertirADTO(MovimientosInventario movimiento) {
        return new MovimientoInventarioDTO(
                movimiento.getId(),
                movimiento.getInventarioId(),
                movimiento.getTipo(),
                movimiento.getCantidad(),
                movimiento.getMotivo(),
                movimiento.getUsuarioId()
        );
    }
}
