package com.example.VentasService.controller;

import com.example.VentasService.dto.*;
import com.example.VentasService.service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas/boletas")
@Tag(name = "Boletas", description = "Gestión de boletas de venta")
public class BoletaController {
    
    @Autowired
    private BoletaService boletaService;
    
    @GetMapping
    @Operation(summary = "Listar todas las boletas",
               description = "Obtiene todas las boletas ordenadas por fecha descendente")
    public ResponseEntity<List<BoletaDTO>> listarTodas() {
        return ResponseEntity.ok(boletaService.obtenerTodasLasBoletas());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Obtener boleta por ID",
               description = "Retorna una boleta específica con sus detalles")
    public ResponseEntity<BoletaDTO> obtenerPorId(@PathVariable Long id) {
        return boletaService.obtenerBoletaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/cliente/{idCliente}")
    @Operation(summary = "Obtener boletas por cliente",
               description = "Retorna todas las boletas de un cliente específico")
    public ResponseEntity<List<BoletaDTO>> obtenerPorCliente(@PathVariable Long idCliente) {
        return ResponseEntity.ok(boletaService.obtenerBoletasPorCliente(idCliente));
    }
    
    @GetMapping("/{id}/detalles")
    @Operation(summary = "Obtener detalles de una boleta",
               description = "Retorna los productos incluidos en una boleta")
    public ResponseEntity<List<DetalleBoletaDTO>> obtenerDetalles(@PathVariable Long id) {
        return ResponseEntity.ok(boletaService.obtenerDetallesPorBoleta(id));
    }
    
    @PostMapping("/crear")
    @Operation(summary = "Crear nueva venta",
               description = "Crea una nueva boleta con sus detalles de productos")
    public ResponseEntity<BoletaDTO> crear(@RequestBody CrearBoletaRequest request) {
        BoletaDTO nuevaBoleta = boletaService.crearBoleta(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaBoleta);
    }
    
    @PutMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de boleta",
               description = "Actualiza el estado de una boleta (pendiente, confirmada, cancelada, completada)")
    public ResponseEntity<BoletaDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestBody CambiarEstadoRequest request) {
        return boletaService.cambiarEstado(id, request.getNuevoEstado())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
