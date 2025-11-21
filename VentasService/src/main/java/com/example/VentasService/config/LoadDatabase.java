package com.example.ventasservice.config;

import com.example.ventasservice.model.Boleta;
import com.example.ventasservice.model.DetalleBoleta;
import com.example.ventasservice.repository.BoletaRepository;
import com.example.ventasservice.repository.DetalleBoletaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Configuración para precargar datos iniciales de ventas en la base de datos.
 * Crea boletas de ejemplo enlazadas a clientes del UsuarioService.
 */
@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);
    private static final Random random = new Random();

    @Bean
    CommandLineRunner initDatabase(
            BoletaRepository boletaRepository,
            DetalleBoletaRepository detalleBoletaRepository) {

        return args -> {
            log.info("=== Iniciando precarga de datos de ventas ===");

            // Si ya hay boletas, no precargar
            if (boletaRepository.count() > 0) {
                log.info("Ya existen boletas en la base de datos. Omitiendo precarga.");
                return;
            }


            // IDs de clientes reales del UsuarioService
            // idPersona: 3=González, 4=Ramírez, 5=Martínez, 6=Fernández, 7=López, 8=Silva, 9=Rojas, 10=Morales, 11=Vargas
            Long[] clienteIds = {3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L};
            String[] clienteNombres = {
                "María González", "Pedro Ramírez", "Ana Martínez", 
                "Luis Fernández", "Carmen López", "Roberto Silva",
                "Patricia Rojas", "Diego Morales", "Sofía Vargas"
            };

            // IDs de inventario simulados (ajustar según tu InventarioService)
            Long[] inventarioIds = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L, 11L, 12L};
            String[] productos = {
                "Zapatilla Nike Air Max", "Zapatilla Adidas Superstar", "Zapatilla Puma RS-X",
                "Zapatilla Reebok Classic", "Zapatilla New Balance 574", "Zapatilla Converse Chuck Taylor",
                "Zapatilla Vans Old Skool", "Zapatilla Fila Disruptor", "Zapatilla Skechers D'Lites",
                "Zapatilla Under Armour Hovr", "Zapatilla Asics Gel-Kayano", "Zapatilla Salomon Speedcross"
            };
            String[] tallas = {"38", "39", "40", "41", "42", "43"};
            String[] metodosPago = {"efectivo", "transferencia", "tarjeta"};
            String[] estados = {"completada", "completada", "confirmada", "pendiente"};

            List<Boleta> boletas = new ArrayList<>();
            List<DetalleBoleta> detalles = new ArrayList<>();

            log.info("Creando boletas de ejemplo...");

            // Crear 20 boletas de ejemplo
            for (int i = 0; i < 20; i++) {
                Long clienteId = clienteIds[random.nextInt(clienteIds.length)];
                String clienteNombre = clienteNombres[random.nextInt(clienteNombres.length)];
                String metodoPago = metodosPago[random.nextInt(metodosPago.length)];
                String estado = estados[random.nextInt(estados.length)];
                
                // Fecha aleatoria en los últimos 30 días
                LocalDateTime fechaVenta = LocalDateTime.now().minusDays(random.nextInt(30));

                Boleta boleta = new Boleta();
                boleta.setClienteId(clienteId);
                boleta.setFechaVenta(fechaVenta);
                boleta.setMetodoPago(metodoPago);
                boleta.setEstado(estado);
                boleta.setObservaciones("Venta generada automáticamente - Cliente: " + clienteNombre);
                
                // Guardar boleta primero para obtener ID
                Boleta boletaGuardada = boletaRepository.save(boleta);
                log.info("Creada boleta #{} - Cliente ID: {} - Estado: {}", 
                    boletaGuardada.getId(), clienteId, estado);

                // Crear entre 1 y 4 productos por boleta
                int cantidadProductos = 1 + random.nextInt(4);
                Integer totalBoleta = 0;

                for (int j = 0; j < cantidadProductos; j++) {
                    int productoIndex = random.nextInt(productos.length);
                    Long inventarioId = inventarioIds[productoIndex];
                    String producto = productos[productoIndex];
                    String talla = tallas[random.nextInt(tallas.length)];
                    Integer cantidad = 1 + random.nextInt(3); // 1-3 unidades
                    Integer precioUnitario = 25000 + (random.nextInt(76) * 1000); // 25.000 - 100.000

                    DetalleBoleta detalle = new DetalleBoleta();
                    detalle.setBoletaId(boletaGuardada.getId());
                    detalle.setInventarioId(inventarioId);
                    detalle.setNombreProducto(producto);
                    detalle.setTalla(talla);
                    detalle.setCantidad(cantidad);
                    detalle.setPrecioUnitario(precioUnitario);
                    // El subtotal se calcula automáticamente en @PrePersist

                    detalles.add(detalle);
                    totalBoleta += cantidad * precioUnitario;
                }

                // Actualizar total de la boleta
                boletaGuardada.setTotal(totalBoleta);
                boletaRepository.save(boletaGuardada);
            }

            // Guardar todos los detalles
            detalleBoletaRepository.saveAll(detalles);
            log.info("Creados {} detalles de boleta", detalles.size());

            // Crear algunas boletas adicionales con estados específicos
            log.info("Creando boletas con estados específicos...");

            // Boleta pendiente
            Boleta boletaPendiente = new Boleta();
            boletaPendiente.setClienteId(clienteIds[0]); // María González
            boletaPendiente.setFechaVenta(LocalDateTime.now());
            boletaPendiente.setMetodoPago("transferencia");
            boletaPendiente.setEstado("pendiente");
            boletaPendiente.setObservaciones("Esperando confirmación de pago");
            boletaPendiente = boletaRepository.save(boletaPendiente);

            DetalleBoleta detallePendiente = new DetalleBoleta();
            detallePendiente.setBoletaId(boletaPendiente.getId());
            detallePendiente.setInventarioId(1L);
            detallePendiente.setNombreProducto("Zapatilla Nike Air Max");
            detallePendiente.setTalla("40");
            detallePendiente.setCantidad(1);
            detallePendiente.setPrecioUnitario(89990);
            detalleBoletaRepository.save(detallePendiente);

            boletaPendiente.setTotal(89990);
            boletaRepository.save(boletaPendiente);

            // Boleta cancelada
            Boleta boletaCancelada = new Boleta();
            boletaCancelada.setClienteId(clienteIds[1]); // Pedro Ramírez
            boletaCancelada.setFechaVenta(LocalDateTime.now().minusDays(5));
            boletaCancelada.setMetodoPago("tarjeta");
            boletaCancelada.setEstado("cancelada");
            boletaCancelada.setObservaciones("Cliente canceló el pedido");
            boletaCancelada = boletaRepository.save(boletaCancelada);

            DetalleBoleta detalleCancelada = new DetalleBoleta();
            detalleCancelada.setBoletaId(boletaCancelada.getId());
            detalleCancelada.setInventarioId(2L);
            detalleCancelada.setNombreProducto("Zapatilla Adidas Superstar");
            detalleCancelada.setTalla("42");
            detalleCancelada.setCantidad(2);
            detalleCancelada.setPrecioUnitario(79990);
            detalleBoletaRepository.save(detalleCancelada);

            boletaCancelada.setTotal(159980);
            boletaRepository.save(boletaCancelada);

            // ============================================
            // Resumen de precarga
            // ============================================
            log.info("=== Precarga de ventas completada exitosamente ===");
            log.info("Total Boletas: {}", boletaRepository.count());
            log.info("Total Detalles: {}", detalleBoletaRepository.count());
            log.info("Boletas pendientes: {}", boletaRepository.countByEstado("pendiente"));
            log.info("Boletas confirmadas: {}", boletaRepository.countByEstado("confirmada"));
            log.info("Boletas completadas: {}", boletaRepository.countByEstado("completada"));
            log.info("Boletas canceladas: {}", boletaRepository.countByEstado("cancelada"));
            
                Integer totalVentas = boletaRepository.findAll().stream()
                    .mapToInt(b -> b.getTotal() != null ? b.getTotal() : 0)
                    .sum();
            log.info("Total en ventas: ${}", String.format("%,d", totalVentas));
            log.info("==================================================");
        };
    }
}
