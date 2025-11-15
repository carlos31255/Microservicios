package com.example.entregasService.repository;

import com.example.entregasService.entity.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    // Buscar entrega por ID de boleta
    Optional<Entrega> findByIdBoleta(Integer idBoleta);

    // Buscar entregas por transportista
    List<Entrega> findByIdTransportista(Long idTransportista);

    // Buscar entregas por estado
    List<Entrega> findByEstadoEntrega(String estadoEntrega);

    // Buscar entregas pendientes de asignación (sin transportista)
    List<Entrega> findByIdTransportistaIsNullAndEstadoEntrega(String estadoEntrega);

    // Buscar entregas pendientes sin transportista
    @Query("SELECT e FROM Entrega e WHERE e.idTransportista IS NULL AND e.estadoEntrega = 'pendiente'")
    List<Entrega> findEntregasPendientesDeAsignacion();

    // Buscar entregas por transportista y estado
    List<Entrega> findByIdTransportistaAndEstadoEntrega(Long idTransportista, String estadoEntrega);

    // Buscar entregas por rango de fechas de asignación
    @Query("SELECT e FROM Entrega e WHERE e.fechaAsignacion BETWEEN :fechaInicio AND :fechaFin")
    List<Entrega> findByFechaAsignacionBetween(
        @Param("fechaInicio") LocalDateTime fechaInicio, 
        @Param("fechaFin") LocalDateTime fechaFin
    );

    // Buscar entregas por rango de fechas de entrega
    @Query("SELECT e FROM Entrega e WHERE e.fechaEntrega BETWEEN :fechaInicio AND :fechaFin")
    List<Entrega> findByFechaEntregaBetween(
        @Param("fechaInicio") LocalDateTime fechaInicio, 
        @Param("fechaFin") LocalDateTime fechaFin
    );

    // Contar entregas por estado
    Long countByEstadoEntrega(String estadoEntrega);

    // Contar entregas por transportista
    Long countByIdTransportista(Long idTransportista);

    // Buscar entregas activas (no entregadas ni canceladas)
    @Query("SELECT e FROM Entrega e WHERE e.estadoEntrega NOT IN ('entregada', 'cancelada')")
    List<Entrega> findEntregasActivas();

    // Verificar si existe entrega para una boleta
    boolean existsByIdBoleta(Integer idBoleta);
}
