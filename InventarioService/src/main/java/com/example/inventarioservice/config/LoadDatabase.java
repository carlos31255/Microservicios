package com.example.inventarioservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.inventarioservice.model.Inventario;
import com.example.inventarioservice.model.Marca;
import com.example.inventarioservice.model.Producto;
import com.example.inventarioservice.model.Talla;
import com.example.inventarioservice.repository.InventarioRepository;
import com.example.inventarioservice.repository.MarcaRepository;
import com.example.inventarioservice.repository.ProductoRepository;
import com.example.inventarioservice.repository.TallaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.Map;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class LoadDatabase {
    
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(InventarioRepository inventarioRepository,
                                   MarcaRepository marcaRepository,
                                   ProductoRepository productoRepository,
                                   TallaRepository tallaRepository) {
        return args -> {
            log.info("Inicialización forzada: limpiando tablas de inventario, modelos y lookup antes de precarga...");

            // Borrar datos previos para re-seed limpio (el usuario indicó que puede eliminar la BD)
            try {
                inventarioRepository.deleteAll();
                productoRepository.deleteAll();
                marcaRepository.deleteAll();
                tallaRepository.deleteAll();
                log.info("Tablas inventario/modelo/marca/talla limpiadas.");
            } catch (Exception e) {
                log.warn("No se pudieron limpiar todas las tablas: {}", e.getMessage());
            }

            log.info("Inicializando tablas lookup (marca, modelo, talla) y cargando inventario de prueba...");

            // ------------------ Crear marcas ------------------
            if (marcaRepository.count() == 0) {
                Marca nike = new Marca(null, "Nike", "Marca Nike");
                Marca adidas = new Marca(null, "Adidas", "Marca Adidas");
                Marca puma = new Marca(null, "Puma", "Marca Puma");
                Marca converse = new Marca(null, "Converse", "Marca Converse");
                Marca vans = new Marca(null, "Vans", "Marca Vans");
                Marca nb = new Marca(null, "New Balance", "Marca New Balance");
                Marca asics = new Marca(null, "Asics", "Marca Asics");
                Marca skechers = new Marca(null, "Skechers", "Marca Skechers");
                Marca reebok = new Marca(null, "Reebok", "Marca Reebok");
                Marca timberland = new Marca(null, "Timberland", "Marca Timberland");
                Marca drmartens = new Marca(null, "Dr. Martens", "Marca Dr. Martens");
                Marca stepstyle = new Marca(null, "StepStyle", "Marca StepStyle");
                marcaRepository.saveAll(Arrays.asList(nike, adidas, puma, converse, vans, nb, asics, skechers, reebok, timberland, drmartens, stepstyle));
            }

            // ------------------ Crear tallas ------------------
            if (tallaRepository.count() == 0) {
                Talla t36 = new Talla(null, "36", "Talla 36");
                Talla t37 = new Talla(null, "37", "Talla 37");
                Talla t38 = new Talla(null, "38", "Talla 38");
                Talla t39 = new Talla(null, "39", "Talla 39");
                Talla t40 = new Talla(null, "40", "Talla 40");
                Talla t41 = new Talla(null, "41", "Talla 41");
                Talla t42 = new Talla(null, "42", "Talla 42");
                Talla t43 = new Talla(null, "43", "Talla 43");
                tallaRepository.saveAll(Arrays.asList(t36, t37, t38, t39, t40, t41, t42, t43));
            }

            // ------------------ Crear / Actualizar solo modelos StepStyle ------------------
            // Mantener únicamente los modelos propios que se usan en la precarga de inventario
            Marca stepstyle = marcaRepository.findAll().stream().filter(m -> m.getNombre().equalsIgnoreCase("StepStyle")).findFirst().orElse(null);

            List<Producto> modelosDeseados = new ArrayList<>();
            Producto m12 = new Producto();
            m12.setNombre("StepStyle Classic"); m12.setMarcaId(stepstyle != null ? stepstyle.getId() : null); m12.setDescripcion("Zapatillas clásicas cómodas"); m12.setImagenUrl(null); m12.setPrecioUnitario(45000); modelosDeseados.add(m12);
            Producto m13 = new Producto();
            m13.setNombre("StepStyle Runner"); m13.setMarcaId(stepstyle != null ? stepstyle.getId() : null); m13.setDescripcion("Runner ligero para entrenamiento"); m13.setImagenUrl(null); m13.setPrecioUnitario(48000); modelosDeseados.add(m13);
            Producto m14 = new Producto();
            m14.setNombre("StepStyle Urban"); m14.setMarcaId(stepstyle != null ? stepstyle.getId() : null); m14.setDescripcion("Casual urbano con diseño moderno"); m14.setImagenUrl(null); m14.setPrecioUnitario(47000); modelosDeseados.add(m14);
            Producto m15 = new Producto();
            m15.setNombre("StepStyle Kids"); m15.setMarcaId(stepstyle != null ? stepstyle.getId() : null); m15.setDescripcion("Zapatillas para niños"); m15.setImagenUrl(null); m15.setPrecioUnitario(30000); modelosDeseados.add(m15);

            for (Producto modelo : modelosDeseados) {
                Producto existente = productoRepository.findByNombreIgnoreCase(modelo.getNombre());
                if (existente != null) {
                    existente.setMarcaId(modelo.getMarcaId());
                    existente.setDescripcion(modelo.getDescripcion());
                    productoRepository.save(existente);
                } else {
                    productoRepository.save(modelo);
                }
            }

            // --- Mapear imágenes solo para StepStyle (si existen en classpath)
            Map<String, String> modelosToImage = Map.of(
                    "stepstyle classic", "static/images/classic.jpg",
                    "stepstyle runner", "static/images/runner.jpg",
                    "stepstyle urban", "static/images/urban.jpg",
                    "stepstyle kids", "static/images/kids.webp"
            );

            for (Producto modelo : productoRepository.findAll()) {
                if (modelo.getNombre() == null) continue;
                String key = modelo.getNombre().toLowerCase();
                for (Map.Entry<String, String> e : modelosToImage.entrySet()) {
                    if (key.contains(e.getKey())) {
                        try {
                            ClassPathResource imgRes = new ClassPathResource(e.getValue());
                            if (imgRes.exists()) {
                                byte[] bytes = imgRes.getInputStream().readAllBytes();
                                modelo.setImagen(bytes);
                                // opcional: también establecer imagenUrl para compatibilidad
                                modelo.setImagenUrl("/images/" + imgRes.getFilename());
                                productoRepository.save(modelo);
                                log.info("Asociada imagen '{}' al modelo '{}'", imgRes.getFilename(), modelo.getNombre());
                            } else {
                                log.warn("Imagen no encontrada en classpath: {}", e.getValue());
                            }
                        } catch (IOException ioe) {
                            log.warn("Error leyendo imagen {}: {}", e.getValue(), ioe.getMessage());
                        }
                        break;
                    }
                }
            }

            log.info("¡Base de datos de inventario cargada exitosamente con {} productos únicos!", 
                inventarioRepository.count());
            
            // Mostrar resumen por producto
            log.info("=== RESUMEN DE INVENTARIO ===");
            log.info("Zapatillas Deportivas: Nike Air Max 270 (8 tallas), Adidas Ultraboost 22 (8 tallas), Puma RS-X (8 tallas)");
            log.info("Zapatillas Urbanas: Converse Chuck Taylor (8 tallas), Vans Old Skool (8 tallas)");
            log.info("Zapatillas Running: New Balance 1080v12 (8 tallas), Asics Gel-Kayano 29 (8 tallas)");
            log.info("Zapatillas Casual: Skechers D'Lites (8 tallas), Reebok Club C 85 (8 tallas)");
            log.info("Botas y Botines: Timberland 6-Inch Premium (8 tallas), Dr. Martens 1460 (8 tallas)");
            log.info("Total de registros de inventario: {}", inventarioRepository.count());
            
            // ------------------ Productos StepStyle (propios) - replicar precarga móvil ------------------
            // Talllas a precargar y stock por índice (mismo arreglo que la app móvil)
            List<String> tallasIniciales = Arrays.asList("38", "39", "40", "41", "42", "43");
            int[] stockPorTalla = new int[] {5, 4, 6, 3, 2, 1};

                // Obtener solo los modelos StepStyle que deben precargarse (lista blanca)
                List<String> allowedStepModels = Arrays.asList(
                    "stepstyle classic",
                    "stepstyle runner",
                    "stepstyle urban",
                    "stepstyle kids",
                    "stepstylekids"
                );

                List<Producto> modelosStep = productoRepository.findAll().stream()
                    .filter(m -> {
                    if (m.getNombre() == null) return false;
                    String name = m.getNombre().toLowerCase();
                    for (String allowed : allowedStepModels) {
                        if (name.contains(allowed)) return true;
                    }
                    return false;
                    })
                    .collect(Collectors.toList());

            if (!modelosStep.isEmpty()) {
                long baseProductId = 6000L;
                int modelIndex = 0;
                for (Producto modelo : modelosStep) {
                    modelIndex++;
                    long productoId = baseProductId + modelIndex; // 6001, 6002, ...
                    String nombreModelo = modelo.getNombre();

                    for (int i = 0; i < tallasIniciales.size(); i++) {
                        String numero = tallasIniciales.get(i);
                        int stockInicial = stockPorTalla[i];
                        // Evitar duplicados por modelo+talla
                        if (inventarioRepository.findByModeloIdAndTalla(modelo.getId(), numero).isEmpty()) {
                            Long tallaId = null;
                            try {
                                Talla t = tallaRepository.findByValor(numero);
                                if (t != null) tallaId = t.getId();
                            } catch (Exception ignored) {}

                            Inventario inv = new Inventario(null, productoId, nombreModelo, numero, stockInicial, 1, modelo.getId(), tallaId);
                            inventarioRepository.save(inv);
                        }
                    }
                    // Asociar tallas disponibles al producto 
                    try {
                        java.util.Set<Talla> set = new java.util.HashSet<>();
                        for (String numero : tallasIniciales) {
                            Talla t = tallaRepository.findByValor(numero);
                            if (t != null) set.add(t);
                        }
                        modelo.setTallas(set);
                        productoRepository.save(modelo);
                    } catch (Exception e) {
                        log.warn("No se pudo asociar tallas a producto {}: {}", modelo.getNombre(), e.getMessage());
                    }
                }
            }
        };
    }
}
