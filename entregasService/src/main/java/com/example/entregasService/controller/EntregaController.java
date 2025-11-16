package com.example.entregasService.controller;

import com.example.entregasService.dto.ActualizarEstadoDTO;
import com.example.entregasService.dto.AsignarTransportistaDTO;
import com.example.entregasService.dto.ContarEstadoDTO;
import com.example.entregasService.dto.EntregaDTO;
import com.example.entregasService.dto.EntregaRequestDTO;
import com.example.entregasService.service.EntregaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/entregas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Entregas", description = "API para gestión de entregas")
@CrossOrigin(origins = "*")
public class EntregaController {

    private final EntregaService entregaService;

    @PostMapping
    @Operation(summary = "Crear nueva entrega", description = "Crea una nueva entrega asociada a una boleta de venta")
    public ResponseEntity<EntregaDTO> crearEntrega(@Valid @RequestBody EntregaRequestDTO requestDTO) {
        log.info("POST /api/entregas - Creando nueva entrega");
        EntregaDTO entrega = entregaService.crearEntrega(requestDTO);
        return new ResponseEntity<>(entrega, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Obtener todas las entregas", description = "Retorna la lista completa de entregas")
    public ResponseEntity<List<EntregaDTO>> obtenerTodasLasEntregas() {
        log.info("GET /api/entregas - Obteniendo todas las entregas");
        List<EntregaDTO> entregas = entregaService.obtenerTodasLasEntregas();
        return ResponseEntity.ok(entregas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener entrega por ID", description = "Retorna una entrega específica por su ID")
    public ResponseEntity<EntregaDTO> obtenerEntregaPorId(
            @Parameter(description = "ID de la entrega") @PathVariable Long id) {
        log.info("GET /api/entregas/{} - Obteniendo entrega por ID", id);
        EntregaDTO entrega = entregaService.obtenerEntregaPorId(id);
        return ResponseEntity.ok(entrega);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar entrega", description = "Actualiza los datos de una entrega existente")
    public ResponseEntity<EntregaDTO> actualizarEntrega(
            @Parameter(description = "ID de la entrega") @PathVariable Long id,
            @Valid @RequestBody EntregaRequestDTO requestDTO) {
        log.info("PUT /api/entregas/{} - Actualizando entrega", id);
        EntregaDTO entrega = entregaService.actualizarEntrega(id, requestDTO);
        return ResponseEntity.ok(entrega);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar entrega", description = "Elimina una entrega del sistema")
    public ResponseEntity<Map<String, String>> eliminarEntrega(
            @Parameter(description = "ID de la entrega") @PathVariable Long id) {
        log.info("DELETE /api/entregas/{} - Eliminando entrega", id);
        entregaService.eliminarEntrega(id);
        return ResponseEntity.ok(Map.of("mensaje", "Entrega eliminada exitosamente"));
    }

    @GetMapping("/boleta/{idBoleta}")
    @Operation(summary = "Obtener entrega por boleta", description = "Retorna la entrega asociada a una boleta de venta")
    public ResponseEntity<EntregaDTO> obtenerEntregaPorBoleta(
            @Parameter(description = "ID de la boleta") @PathVariable Integer idBoleta) {
        log.info("GET /api/entregas/boleta/{} - Obteniendo entrega por boleta", idBoleta);
        EntregaDTO entrega = entregaService.obtenerEntregaPorBoleta(idBoleta);
        return ResponseEntity.ok(entrega);
    }

    @GetMapping("/transportista/{idTransportista}")
    @Operation(summary = "Obtener entregas por transportista", description = "Retorna todas las entregas de un transportista")
    public ResponseEntity<List<EntregaDTO>> obtenerEntregasPorTransportista(
            @Parameter(description = "ID del transportista") @PathVariable Long idTransportista) {
        log.info("GET /api/entregas/transportista/{} - Obteniendo entregas por transportista", idTransportista);
        List<EntregaDTO> entregas = entregaService.obtenerEntregasPorTransportista(idTransportista);
        return ResponseEntity.ok(entregas);
    }

    @GetMapping("/estado/{estado}")
    @Operation(summary = "Obtener entregas por estado", description = "Retorna todas las entregas con un estado específico")
    public ResponseEntity<List<EntregaDTO>> obtenerEntregasPorEstado(
            @Parameter(description = "Estado de la entrega (pendiente, en_camino, entregada, cancelada)") 
            @PathVariable String estado) {
        log.info("GET /api/entregas/estado/{} - Obteniendo entregas por estado", estado);
        List<EntregaDTO> entregas = entregaService.obtenerEntregasPorEstado(estado);
        return ResponseEntity.ok(entregas);
    }

    @GetMapping("/pendientes")
    @Operation(summary = "Obtener entregas pendientes de asignación", 
               description = "Retorna entregas que aún no tienen transportista asignado")
    public ResponseEntity<List<EntregaDTO>> obtenerEntregasPendientes() {
        log.info("GET /api/entregas/pendientes - Obteniendo entregas pendientes de asignación");
        List<EntregaDTO> entregas = entregaService.obtenerEntregasPendientesDeAsignacion();
        return ResponseEntity.ok(entregas);
    }

    @GetMapping("/activas")
    @Operation(summary = "Obtener entregas activas", 
               description = "Retorna entregas que no están entregadas ni canceladas")
    public ResponseEntity<List<EntregaDTO>> obtenerEntregasActivas() {
        log.info("GET /api/entregas/activas - Obteniendo entregas activas");
        List<EntregaDTO> entregas = entregaService.obtenerEntregasActivas();
        return ResponseEntity.ok(entregas);
    }

    @PatchMapping("/{id}/asignar-transportista")
    @Operation(summary = "Asignar transportista a entrega", 
               description = "Asigna un transportista a una entrega pendiente")
    public ResponseEntity<EntregaDTO> asignarTransportista(
            @Parameter(description = "ID de la entrega") @PathVariable Long id,
            @Valid @RequestBody AsignarTransportistaDTO asignarDTO) {
        log.info("PATCH /api/entregas/{}/asignar-transportista - Asignando transportista", id);
        EntregaDTO entrega = entregaService.asignarTransportista(id, asignarDTO);
        return ResponseEntity.ok(entrega);
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Actualizar estado de entrega", 
               description = "Actualiza el estado de una entrega")
    public ResponseEntity<EntregaDTO> actualizarEstado(
            @Parameter(description = "ID de la entrega") @PathVariable Long id,
            @Valid @RequestBody ActualizarEstadoDTO estadoDTO) {
        log.info("PATCH /api/entregas/{}/estado - Actualizando estado a: {}", id, estadoDTO.getEstadoEntrega());
        EntregaDTO entrega = entregaService.actualizarEstado(id, estadoDTO);
        return ResponseEntity.ok(entrega);
    }

    @PatchMapping("/{id}/completar")
    @Operation(summary = "Completar entrega", 
               description = "Marca una entrega como completada y registra la fecha de entrega")
    public ResponseEntity<EntregaDTO> completarEntrega(
            @Parameter(description = "ID de la entrega") @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        log.info("PATCH /api/entregas/{}/completar - Completando entrega", id);
        String observacion = body != null ? body.get("observacion") : null;
        EntregaDTO entrega = entregaService.completarEntrega(id, observacion);
        return ResponseEntity.ok(entrega);
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar entrega", 
               description = "Marca una entrega como cancelada")
    public ResponseEntity<EntregaDTO> cancelarEntrega(
            @Parameter(description = "ID de la entrega") @PathVariable Long id,
            @RequestBody(required = false) Map<String, String> body) {
        log.info("PATCH /api/entregas/{}/cancelar - Cancelando entrega", id);
        String observacion = body != null ? body.get("observacion") : null;
        EntregaDTO entrega = entregaService.cancelarEntrega(id, observacion);
        return ResponseEntity.ok(entrega);
    }

    @GetMapping("/estadisticas/estado/{estado}")
    @Operation(summary = "Contar entregas por estado", 
               description = "Retorna el número total de entregas con un estado específico")
    public ResponseEntity<ContarEstadoDTO> contarPorEstado(
            @Parameter(description = "Estado de la entrega") @PathVariable String estado) {
        log.info("GET /api/entregas/estadisticas/estado/{} - Contando entregas", estado);
        Long count = entregaService.contarEntregasPorEstado(estado);
        return ResponseEntity.ok(new ContarEstadoDTO(estado, count));
    }

    @GetMapping("/estadisticas/transportista/{idTransportista}")
    @Operation(summary = "Contar entregas por transportista", 
               description = "Retorna el número total de entregas de un transportista")
    public ResponseEntity<Map<String, Object>> contarPorTransportista(
            @Parameter(description = "ID del transportista") @PathVariable Long idTransportista) {
        log.info("GET /api/entregas/estadisticas/transportista/{} - Contando entregas", idTransportista);
        Long count = entregaService.contarEntregasPorTransportista(idTransportista);
        return ResponseEntity.ok(Map.of("idTransportista", idTransportista, "total", count));
    }

    @GetMapping("/buscar/fecha-asignacion")
    @Operation(summary = "Buscar entregas por fecha de asignación", 
               description = "Retorna entregas asignadas en un rango de fechas")
    public ResponseEntity<List<EntregaDTO>> buscarPorFechaAsignacion(
            @Parameter(description = "Fecha de inicio (formato: yyyy-MM-dd'T'HH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin (formato: yyyy-MM-dd'T'HH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        log.info("GET /api/entregas/buscar/fecha-asignacion - Buscando entre {} y {}", fechaInicio, fechaFin);
        List<EntregaDTO> entregas = entregaService.buscarPorFechaAsignacion(fechaInicio, fechaFin);
        return ResponseEntity.ok(entregas);
    }

    @GetMapping("/buscar/fecha-entrega")
    @Operation(summary = "Buscar entregas por fecha de entrega", 
               description = "Retorna entregas entregadas en un rango de fechas")
    public ResponseEntity<List<EntregaDTO>> buscarPorFechaEntrega(
            @Parameter(description = "Fecha de inicio (formato: yyyy-MM-dd'T'HH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin (formato: yyyy-MM-dd'T'HH:mm:ss)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {
        log.info("GET /api/entregas/buscar/fecha-entrega - Buscando entre {} y {}", fechaInicio, fechaFin);
        List<EntregaDTO> entregas = entregaService.buscarPorFechaEntrega(fechaInicio, fechaFin);
        return ResponseEntity.ok(entregas);
    }
}
