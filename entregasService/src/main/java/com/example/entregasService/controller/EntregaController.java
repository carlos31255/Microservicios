package com.example.entregasService.controller;

import com.example.entregasService.dto.EntregaDTO;
import com.example.entregasService.dto.EntregaRequestDTO;
import com.example.entregasService.service.EntregaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/entregas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Entregas", description = "API para gestión de entregas")
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    @GetMapping
    @Operation(summary = "Listar todas las entregas", description = "Devuelve el listado completo de entregas registradas en el sistema")
    public ResponseEntity<List<EntregaDTO>> listarTodas() {
        List<EntregaDTO> entregas = entregaService.obtenerTodasLasEntregas();
        return ResponseEntity.ok(entregas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener entrega por ID", description = "Busca los detalles de una entrega específica mediante su identificador único")
    public ResponseEntity<EntregaDTO> obtenerPorId(
            @Parameter(description = "ID único de la entrega", example = "1") 
            @PathVariable Long id) {
        EntregaDTO entrega = entregaService.obtenerEntregaPorId(id);
        return ResponseEntity.ok(entrega);
    }

    @GetMapping("/transportista/{transportistaId}")
    @Operation(summary = "Listar por Transportista", description = "Obtiene todas las entregas asignadas a un transportista específico")
    public ResponseEntity<List<EntregaDTO>> listarPorTransportista(
            @Parameter(description = "ID del transportista", example = "5") 
            @PathVariable Long transportistaId) {
        List<EntregaDTO> entregas = entregaService.getEntregasByTransportista(transportistaId);
        return ResponseEntity.ok(entregas);
    }

    @PostMapping("/crear")
    @Operation(
        summary = "Crear nueva entrega", 
        description = "Crea una nueva entrega en el sistema para una boleta específica",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos requeridos para la creación",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"idBoleta\": 102030, \"direccion\": \"Av. Siempre Viva 742\", \"contacto\": \"+56912345678\"}"
                )
            )
        )
    )
    public ResponseEntity<EntregaDTO> crearEntrega(@RequestBody EntregaRequestDTO request) {
        EntregaDTO entrega = entregaService.crearEntrega(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(entrega);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Listar por Estado", description = "Filtra las entregas según su estado actual")
    public ResponseEntity<List<EntregaDTO>> listarPorEstado(
            @Parameter(description = "Estado de la entrega", example = "PENDIENTE") 
            @PathVariable String estado) {
        List<EntregaDTO> entregas = entregaService.getEntregasByEstado(estado);
        return ResponseEntity.ok(entregas);
    }

    @PutMapping("/{id}/asignar")
    @Operation(summary = "Asignar Transportista", description = "Vincula una entrega existente a un transportista mediante su ID")
    public ResponseEntity<EntregaDTO> asignarTransportista(
            @Parameter(description = "ID de la entrega", example = "1") 
            @PathVariable Long id,
            @Parameter(description = "ID del transportista a asignar", example = "15") 
            @RequestParam Long transportistaId) {
        EntregaDTO entrega = entregaService.asignarTransportista(id, transportistaId);
        return ResponseEntity.ok(entrega);
    }

    @PutMapping("/{id}/completar")
    @Operation(summary = "Completar Entrega", description = "Marca una entrega como finalizada")
    public ResponseEntity<EntregaDTO> completarEntrega(
            @Parameter(description = "ID de la entrega", example = "1") 
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Observación opcional sobre la entrega",
                content = @Content(examples = @ExampleObject(value = "Entregado en conserjería a Juan Pérez"))
            )
            @RequestBody(required = false) String observacion) {
        EntregaDTO entrega = entregaService.completarEntrega(id, observacion);
        return ResponseEntity.ok(entrega);
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "Cambiar Estado Manualmente", description = "Permite forzar el cambio de estado de una entrega")
    public ResponseEntity<EntregaDTO> cambiarEstado(
            @Parameter(description = "ID de la entrega", example = "1") 
            @PathVariable Long id,
            @Parameter(description = "Nuevo estado a aplicar (texto)", example = "ENTREGADO") 
            @RequestParam String nuevoEstado) {
        EntregaDTO entrega = entregaService.cambiarEstado(id, nuevoEstado);
        return ResponseEntity.ok(entrega);
    }

}
