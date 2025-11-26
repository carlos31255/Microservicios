package com.example.entregasService.config;

import com.example.entregasService.model.Entrega;
import com.example.entregasService.repository.EntregaRepository;
import com.example.entregasService.dto.externo.BoletaExternaDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(EntregaRepository entregaRepository,
                                   @Qualifier("ventasWebClient") WebClient ventasWebClient) {
        return args -> {
            log.info("Iniciando precarga de entregas de ejemplo...");

            // Si ya existen entregas no hacemos nada
            if (entregaRepository.count() > 0) {
                log.info("Entregas ya precargadas (count={})", entregaRepository.count());
                return;
            }

            // Intentar obtener boletas reales desde VentasService
            Long[] boletaIds = new Long[0];
            try {
                java.util.List<BoletaExternaDTO> boletas = ventasWebClient.get()
                        .uri("/ventas/boletas")
                        .retrieve()
                        .bodyToFlux(BoletaExternaDTO.class)
                        .collectList()
                        .block(java.time.Duration.ofSeconds(3));

                if (boletas != null && !boletas.isEmpty()) {
                    boletaIds = boletas.stream().map(BoletaExternaDTO::getId).limit(2).toArray(Long[]::new);
                }
            } catch (Exception ex) {
                log.warn("No fue posible obtener boletas desde VentasService durante la precarga: {}", ex.getMessage());
            }

            // Supongamos que el transportista precargado tiene ID = 1
            Long transportistaId = 1L;


            // Solo crear entregas si recuperamos boletas reales 
            if (boletaIds.length > 0) {
                Entrega e1 = new Entrega();
                e1.setIdBoleta(boletaIds[0]);
                e1.setIdTransportista(transportistaId);
                e1.setEstadoEntrega("asignada");
                e1.setFechaAsignacion(LocalDateTime.now().minusDays(1));
                e1.setDireccionEntrega("Av. Ejemplo 123");
                e1.setIdComuna(205L);
                entregaRepository.save(e1);
            } else {
                log.warn("No se encontraron boletas reales en VentasService; omitiendo creación de entregas precargadas para evitar inconsistencias.");
            }

            if (boletaIds.length > 1) {
                Entrega e2 = new Entrega();
                e2.setIdBoleta(boletaIds[1]);
                e2.setIdTransportista(transportistaId);
                e2.setEstadoEntrega("pendiente");
                e2.setFechaAsignacion(LocalDateTime.now());
                e2.setDireccionEntrega("Calle Falsa 742");
                e2.setIdComuna(205L);
                entregaRepository.save(e2);
            }

            log.info("Precarga completada: entregas creadas para transportista id={}", transportistaId);
        };
    }
}
