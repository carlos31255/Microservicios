package com.example.inventarioservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.inventarioservice.model.Inventario;
import com.example.inventarioservice.model.Marca;
import com.example.inventarioservice.model.ModeloZapato;
import com.example.inventarioservice.model.Talla;
import com.example.inventarioservice.repository.InventarioRepository;
import com.example.inventarioservice.repository.MarcaRepository;
import com.example.inventarioservice.repository.ModeloZapatoRepository;
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
                                   ModeloZapatoRepository modeloRepository,
                                   TallaRepository tallaRepository) {
        return args -> {
            log.info("Inicialización forzada: limpiando tablas de inventario, modelos y lookup antes de precarga...");

            // Borrar datos previos para re-seed limpio (el usuario indicó que puede eliminar la BD)
            try {
                inventarioRepository.deleteAll();
                modeloRepository.deleteAll();
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

            // ------------------ Crear / Actualizar modelos por producto ------------------
            // Creamos una lista de modelos deseados y hacemos upsert por nombre para
            // no romper referencias desde Inventario (actualiza si existe, inserta si falta).
            // Mapear productos a marcas por nombre de marca contenido
            Marca nike = marcaRepository.findAll().stream().filter(m -> m.getNombre().equalsIgnoreCase("Nike")).findFirst().orElse(null);
            Marca adidas = marcaRepository.findAll().stream().filter(m -> m.getNombre().equalsIgnoreCase("Adidas")).findFirst().orElse(null);
            Marca puma = marcaRepository.findAll().stream().filter(m -> m.getNombre().equalsIgnoreCase("Puma")).findFirst().orElse(null);
            Marca converse = marcaRepository.findAll().stream().filter(m -> m.getNombre().equalsIgnoreCase("Converse")).findFirst().orElse(null);
            Marca vans = marcaRepository.findAll().stream().filter(m -> m.getNombre().equalsIgnoreCase("Vans")).findFirst().orElse(null);
            Marca nb = marcaRepository.findAll().stream().filter(m -> m.getNombre().equalsIgnoreCase("New Balance")).findFirst().orElse(null);
            Marca asics = marcaRepository.findAll().stream().filter(m -> m.getNombre().equalsIgnoreCase("Asics")).findFirst().orElse(null);
            Marca skechers = marcaRepository.findAll().stream().filter(m -> m.getNombre().equalsIgnoreCase("Skechers")).findFirst().orElse(null);
            Marca reebok = marcaRepository.findAll().stream().filter(m -> m.getNombre().equalsIgnoreCase("Reebok")).findFirst().orElse(null);
            Marca timberland = marcaRepository.findAll().stream().filter(m -> m.getNombre().equalsIgnoreCase("Timberland")).findFirst().orElse(null);
            Marca drmartens = marcaRepository.findAll().stream().filter(m -> m.getNombre().equalsIgnoreCase("Dr. Martens")).findFirst().orElse(null);
            Marca stepstyle = marcaRepository.findAll().stream().filter(m -> m.getNombre().equalsIgnoreCase("StepStyle")).findFirst().orElse(null);

            List<ModeloZapato> modelosDeseados = new ArrayList<>();
            ModeloZapato m1 = new ModeloZapato();
            m1.setNombre("Nike Air Max 270"); m1.setMarcaId(nike != null ? nike.getId() : null); m1.setDescripcion("Nike Air Max 270 modelo"); m1.setImagenUrl(null); m1.setPrecioUnitario(120000); modelosDeseados.add(m1);
            ModeloZapato m2 = new ModeloZapato();
            m2.setNombre("Adidas Ultraboost 22"); m2.setMarcaId(adidas != null ? adidas.getId() : null); m2.setDescripcion("Adidas Ultraboost 22 modelo"); m2.setImagenUrl(null); m2.setPrecioUnitario(130000); modelosDeseados.add(m2);
            ModeloZapato m3 = new ModeloZapato();
            m3.setNombre("Puma RS-X"); m3.setMarcaId(puma != null ? puma.getId() : null); m3.setDescripcion("Puma RS-X modelo"); m3.setImagenUrl(null); m3.setPrecioUnitario(90000); modelosDeseados.add(m3);
            ModeloZapato m4 = new ModeloZapato();
            m4.setNombre("Converse Chuck Taylor"); m4.setMarcaId(converse != null ? converse.getId() : null); m4.setDescripcion("Converse modelo"); m4.setImagenUrl(null); m4.setPrecioUnitario(60000); modelosDeseados.add(m4);
            ModeloZapato m5 = new ModeloZapato();
            m5.setNombre("Vans Old Skool"); m5.setMarcaId(vans != null ? vans.getId() : null); m5.setDescripcion("Vans modelo"); m5.setImagenUrl(null); m5.setPrecioUnitario(70000); modelosDeseados.add(m5);
            ModeloZapato m6 = new ModeloZapato();
            m6.setNombre("New Balance 1080v12"); m6.setMarcaId(nb != null ? nb.getId() : null); m6.setDescripcion("NB modelo"); m6.setImagenUrl(null); m6.setPrecioUnitario(110000); modelosDeseados.add(m6);
            ModeloZapato m7 = new ModeloZapato();
            m7.setNombre("Asics Gel-Kayano 29"); m7.setMarcaId(asics != null ? asics.getId() : null); m7.setDescripcion("Asics modelo"); m7.setImagenUrl(null); m7.setPrecioUnitario(100000); modelosDeseados.add(m7);
            ModeloZapato m8 = new ModeloZapato();
            m8.setNombre("Skechers D'Lites"); m8.setMarcaId(skechers != null ? skechers.getId() : null); m8.setDescripcion("Skechers modelo"); m8.setImagenUrl(null); m8.setPrecioUnitario(80000); modelosDeseados.add(m8);
            ModeloZapato m9 = new ModeloZapato();
            m9.setNombre("Reebok Club C 85"); m9.setMarcaId(reebok != null ? reebok.getId() : null); m9.setDescripcion("Reebok modelo"); m9.setImagenUrl(null); m9.setPrecioUnitario(65000); modelosDeseados.add(m9);
            ModeloZapato m10 = new ModeloZapato();
            m10.setNombre("Timberland 6-Inch Premium"); m10.setMarcaId(timberland != null ? timberland.getId() : null); m10.setDescripcion("Timberland modelo"); m10.setImagenUrl(null); m10.setPrecioUnitario(140000); modelosDeseados.add(m10);
            ModeloZapato m11 = new ModeloZapato();
            m11.setNombre("Dr. Martens 1460"); m11.setMarcaId(drmartens != null ? drmartens.getId() : null); m11.setDescripcion("DrMartens modelo"); m11.setImagenUrl(null); m11.setPrecioUnitario(90000); modelosDeseados.add(m11);
            ModeloZapato m12 = new ModeloZapato();
            m12.setNombre("StepStyle Classic"); m12.setMarcaId(stepstyle != null ? stepstyle.getId() : null); m12.setDescripcion("Zapatillas clásicas cómodas"); m12.setImagenUrl(null); m12.setPrecioUnitario(45000); modelosDeseados.add(m12);
            ModeloZapato m13 = new ModeloZapato();
            m13.setNombre("StepStyle Runner"); m13.setMarcaId(stepstyle != null ? stepstyle.getId() : null); m13.setDescripcion("Runner ligero para entrenamiento"); m13.setImagenUrl(null); m13.setPrecioUnitario(48000); modelosDeseados.add(m13);
            ModeloZapato m14 = new ModeloZapato();
            m14.setNombre("StepStyle Urban"); m14.setMarcaId(stepstyle != null ? stepstyle.getId() : null); m14.setDescripcion("Casual urbano con diseño moderno"); m14.setImagenUrl(null); m14.setPrecioUnitario(47000); modelosDeseados.add(m14);
            ModeloZapato m15 = new ModeloZapato();
            m15.setNombre("StepStyle Kids"); m15.setMarcaId(stepstyle != null ? stepstyle.getId() : null); m15.setDescripcion("Zapatillas para niños"); m15.setImagenUrl(null); m15.setPrecioUnitario(30000); modelosDeseados.add(m15);

            for (ModeloZapato modelo : modelosDeseados) {
                ModeloZapato existente = modeloRepository.findByNombreIgnoreCase(modelo.getNombre());
                if (existente != null) {
                    existente.setMarcaId(modelo.getMarcaId());
                    existente.setDescripcion(modelo.getDescripcion());
                    modeloRepository.save(existente);
                } else {
                    modeloRepository.save(modelo);
                }
            }

            // --- Mapear imágenes
            Map<String, String> modelosToImage = Map.of(
                    "stepstyle classic", "static/images/classic.jpg",
                    "stepstyle runner", "static/images/runner.jpg",
                    "stepstyle urban", "static/images/urban.jpg",
                    "stepstyle kids", "static/images/kids.webp"
            );

            for (ModeloZapato modelo : modeloRepository.findAll()) {
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
                                modeloRepository.save(modelo);
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

            // ==================== ZAPATILLAS DEPORTIVAS ====================
            // Nike Air Max 270 - Producto ID: 1001
            ModeloZapato modeloNike = modeloRepository.findByNombreIgnoreCase("Nike Air Max 270");
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 1001L, "Nike Air Max 270", "36", 15, 5, modeloNike != null ? modeloNike.getId() : null, tallaRepository.findByValor("36") != null ? tallaRepository.findByValor("36").getId() : null),
                new Inventario(null, 1001L, "Nike Air Max 270", "37", 20, 5, modeloNike != null ? modeloNike.getId() : null, tallaRepository.findByValor("37") != null ? tallaRepository.findByValor("37").getId() : null),
                new Inventario(null, 1001L, "Nike Air Max 270", "38", 25, 5, modeloNike != null ? modeloNike.getId() : null, tallaRepository.findByValor("38") != null ? tallaRepository.findByValor("38").getId() : null),
                new Inventario(null, 1001L, "Nike Air Max 270", "39", 30, 5, modeloNike != null ? modeloNike.getId() : null, tallaRepository.findByValor("39") != null ? tallaRepository.findByValor("39").getId() : null),
                new Inventario(null, 1001L, "Nike Air Max 270", "40", 28, 5, modeloNike != null ? modeloNike.getId() : null, tallaRepository.findByValor("40") != null ? tallaRepository.findByValor("40").getId() : null),
                new Inventario(null, 1001L, "Nike Air Max 270", "41", 22, 5, modeloNike != null ? modeloNike.getId() : null, tallaRepository.findByValor("41") != null ? tallaRepository.findByValor("41").getId() : null),
                new Inventario(null, 1001L, "Nike Air Max 270", "42", 18, 5, modeloNike != null ? modeloNike.getId() : null, tallaRepository.findByValor("42") != null ? tallaRepository.findByValor("42").getId() : null),
                new Inventario(null, 1001L, "Nike Air Max 270", "43", 12, 5, modeloNike != null ? modeloNike.getId() : null, tallaRepository.findByValor("43") != null ? tallaRepository.findByValor("43").getId() : null)
            ));

            // Adidas Ultraboost 22 - Producto ID: 1002
            ModeloZapato modeloAdidas = modeloRepository.findByNombreIgnoreCase("Adidas Ultraboost 22");
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "36", 10, 5, modeloAdidas != null ? modeloAdidas.getId() : null, tallaRepository.findByValor("36") != null ? tallaRepository.findByValor("36").getId() : null),
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "37", 18, 5, modeloAdidas != null ? modeloAdidas.getId() : null, tallaRepository.findByValor("37") != null ? tallaRepository.findByValor("37").getId() : null),
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "38", 22, 5, modeloAdidas != null ? modeloAdidas.getId() : null, tallaRepository.findByValor("38") != null ? tallaRepository.findByValor("38").getId() : null),
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "39", 25, 5, modeloAdidas != null ? modeloAdidas.getId() : null, tallaRepository.findByValor("39") != null ? tallaRepository.findByValor("39").getId() : null),
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "40", 20, 5, modeloAdidas != null ? modeloAdidas.getId() : null, tallaRepository.findByValor("40") != null ? tallaRepository.findByValor("40").getId() : null),
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "41", 15, 5, modeloAdidas != null ? modeloAdidas.getId() : null, tallaRepository.findByValor("41") != null ? tallaRepository.findByValor("41").getId() : null),
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "42", 12, 5, modeloAdidas != null ? modeloAdidas.getId() : null, tallaRepository.findByValor("42") != null ? tallaRepository.findByValor("42").getId() : null),
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "43", 8, 5, modeloAdidas != null ? modeloAdidas.getId() : null, tallaRepository.findByValor("43") != null ? tallaRepository.findByValor("43").getId() : null)
            ));

            // Puma RS-X - Producto ID: 1003
            ModeloZapato modeloPuma = modeloRepository.findByNombreIgnoreCase("Puma RS-X");
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 1003L, "Puma RS-X", "36", 12, 5, modeloPuma != null ? modeloPuma.getId() : null, tallaRepository.findByValor("36") != null ? tallaRepository.findByValor("36").getId() : null),
                new Inventario(null, 1003L, "Puma RS-X", "37", 16, 5, modeloPuma != null ? modeloPuma.getId() : null, tallaRepository.findByValor("37") != null ? tallaRepository.findByValor("37").getId() : null),
                new Inventario(null, 1003L, "Puma RS-X", "38", 20, 5, modeloPuma != null ? modeloPuma.getId() : null, tallaRepository.findByValor("38") != null ? tallaRepository.findByValor("38").getId() : null),
                new Inventario(null, 1003L, "Puma RS-X", "39", 18, 5, modeloPuma != null ? modeloPuma.getId() : null, tallaRepository.findByValor("39") != null ? tallaRepository.findByValor("39").getId() : null),
                new Inventario(null, 1003L, "Puma RS-X", "40", 22, 5, modeloPuma != null ? modeloPuma.getId() : null, tallaRepository.findByValor("40") != null ? tallaRepository.findByValor("40").getId() : null),
                new Inventario(null, 1003L, "Puma RS-X", "41", 16, 5, modeloPuma != null ? modeloPuma.getId() : null, tallaRepository.findByValor("41") != null ? tallaRepository.findByValor("41").getId() : null),
                new Inventario(null, 1003L, "Puma RS-X", "42", 10, 5, modeloPuma != null ? modeloPuma.getId() : null, tallaRepository.findByValor("42") != null ? tallaRepository.findByValor("42").getId() : null),
                new Inventario(null, 1003L, "Puma RS-X", "43", 6, 5, modeloPuma != null ? modeloPuma.getId() : null, tallaRepository.findByValor("43") != null ? tallaRepository.findByValor("43").getId() : null)
            ));

            // ==================== ZAPATILLAS URBANAS ====================

            // Converse Chuck Taylor All Star - Producto ID: 2001
            ModeloZapato modeloConverse = modeloRepository.findByNombreIgnoreCase("Converse Chuck Taylor");
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 2001L, "Converse Chuck Taylor", "36", 20, 10, modeloConverse != null ? modeloConverse.getId() : null, tallaRepository.findByValor("36") != null ? tallaRepository.findByValor("36").getId() : null),
                new Inventario(null, 2001L, "Converse Chuck Taylor", "37", 25, 10, modeloConverse != null ? modeloConverse.getId() : null, tallaRepository.findByValor("37") != null ? tallaRepository.findByValor("37").getId() : null),
                new Inventario(null, 2001L, "Converse Chuck Taylor", "38", 30, 10, modeloConverse != null ? modeloConverse.getId() : null, tallaRepository.findByValor("38") != null ? tallaRepository.findByValor("38").getId() : null),
                new Inventario(null, 2001L, "Converse Chuck Taylor", "39", 28, 10, modeloConverse != null ? modeloConverse.getId() : null, tallaRepository.findByValor("39") != null ? tallaRepository.findByValor("39").getId() : null),
                new Inventario(null, 2001L, "Converse Chuck Taylor", "40", 24, 10, modeloConverse != null ? modeloConverse.getId() : null, tallaRepository.findByValor("40") != null ? tallaRepository.findByValor("40").getId() : null),
                new Inventario(null, 2001L, "Converse Chuck Taylor", "41", 20, 10, modeloConverse != null ? modeloConverse.getId() : null, tallaRepository.findByValor("41") != null ? tallaRepository.findByValor("41").getId() : null),
                new Inventario(null, 2001L, "Converse Chuck Taylor", "42", 15, 10, modeloConverse != null ? modeloConverse.getId() : null, tallaRepository.findByValor("42") != null ? tallaRepository.findByValor("42").getId() : null),
                new Inventario(null, 2001L, "Converse Chuck Taylor", "43", 10, 10, modeloConverse != null ? modeloConverse.getId() : null, tallaRepository.findByValor("43") != null ? tallaRepository.findByValor("43").getId() : null)
            ));

            // Vans Old Skool - Producto ID: 2002
            ModeloZapato modeloVans = modeloRepository.findByNombreIgnoreCase("Vans Old Skool");
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 2002L, "Vans Old Skool", "36", 18, 8, modeloVans != null ? modeloVans.getId() : null, tallaRepository.findByValor("36") != null ? tallaRepository.findByValor("36").getId() : null),
                new Inventario(null, 2002L, "Vans Old Skool", "37", 22, 8, modeloVans != null ? modeloVans.getId() : null, tallaRepository.findByValor("37") != null ? tallaRepository.findByValor("37").getId() : null),
                new Inventario(null, 2002L, "Vans Old Skool", "38", 26, 8, modeloVans != null ? modeloVans.getId() : null, tallaRepository.findByValor("38") != null ? tallaRepository.findByValor("38").getId() : null),
                new Inventario(null, 2002L, "Vans Old Skool", "39", 24, 8, modeloVans != null ? modeloVans.getId() : null, tallaRepository.findByValor("39") != null ? tallaRepository.findByValor("39").getId() : null),
                new Inventario(null, 2002L, "Vans Old Skool", "40", 20, 8, modeloVans != null ? modeloVans.getId() : null, tallaRepository.findByValor("40") != null ? tallaRepository.findByValor("40").getId() : null),
                new Inventario(null, 2002L, "Vans Old Skool", "41", 16, 8, modeloVans != null ? modeloVans.getId() : null, tallaRepository.findByValor("41") != null ? tallaRepository.findByValor("41").getId() : null),
                new Inventario(null, 2002L, "Vans Old Skool", "42", 12, 8, modeloVans != null ? modeloVans.getId() : null, tallaRepository.findByValor("42") != null ? tallaRepository.findByValor("42").getId() : null),
                new Inventario(null, 2002L, "Vans Old Skool", "43", 8, 8, modeloVans != null ? modeloVans.getId() : null, tallaRepository.findByValor("43") != null ? tallaRepository.findByValor("43").getId() : null)
            ));

            // ==================== ZAPATILLAS RUNNING ====================

            // New Balance 1080v12 - Producto ID: 3001
            ModeloZapato modeloNB = modeloRepository.findByNombreIgnoreCase("New Balance 1080v12");
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 3001L, "New Balance 1080v12", "36", 8, 5, modeloNB != null ? modeloNB.getId() : null, tallaRepository.findByValor("36") != null ? tallaRepository.findByValor("36").getId() : null),
                new Inventario(null, 3001L, "New Balance 1080v12", "37", 12, 5, modeloNB != null ? modeloNB.getId() : null, tallaRepository.findByValor("37") != null ? tallaRepository.findByValor("37").getId() : null),
                new Inventario(null, 3001L, "New Balance 1080v12", "38", 15, 5, modeloNB != null ? modeloNB.getId() : null, tallaRepository.findByValor("38") != null ? tallaRepository.findByValor("38").getId() : null),
                new Inventario(null, 3001L, "New Balance 1080v12", "39", 18, 5, modeloNB != null ? modeloNB.getId() : null, tallaRepository.findByValor("39") != null ? tallaRepository.findByValor("39").getId() : null),
                new Inventario(null, 3001L, "New Balance 1080v12", "40", 20, 5, modeloNB != null ? modeloNB.getId() : null, tallaRepository.findByValor("40") != null ? tallaRepository.findByValor("40").getId() : null),
                new Inventario(null, 3001L, "New Balance 1080v12", "41", 14, 5, modeloNB != null ? modeloNB.getId() : null, tallaRepository.findByValor("41") != null ? tallaRepository.findByValor("41").getId() : null),
                new Inventario(null, 3001L, "New Balance 1080v12", "42", 10, 5, modeloNB != null ? modeloNB.getId() : null, tallaRepository.findByValor("42") != null ? tallaRepository.findByValor("42").getId() : null),
                new Inventario(null, 3001L, "New Balance 1080v12", "43", 6, 5, modeloNB != null ? modeloNB.getId() : null, tallaRepository.findByValor("43") != null ? tallaRepository.findByValor("43").getId() : null)
            ));

            // Asics Gel-Kayano 29 - Producto ID: 3002
            ModeloZapato modeloAsics = modeloRepository.findByNombreIgnoreCase("Asics Gel-Kayano 29");
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "36", 10, 5, modeloAsics != null ? modeloAsics.getId() : null, tallaRepository.findByValor("36") != null ? tallaRepository.findByValor("36").getId() : null),
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "37", 14, 5, modeloAsics != null ? modeloAsics.getId() : null, tallaRepository.findByValor("37") != null ? tallaRepository.findByValor("37").getId() : null),
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "38", 18, 5, modeloAsics != null ? modeloAsics.getId() : null, tallaRepository.findByValor("38") != null ? tallaRepository.findByValor("38").getId() : null),
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "39", 16, 5, modeloAsics != null ? modeloAsics.getId() : null, tallaRepository.findByValor("39") != null ? tallaRepository.findByValor("39").getId() : null),
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "40", 22, 5, modeloAsics != null ? modeloAsics.getId() : null, tallaRepository.findByValor("40") != null ? tallaRepository.findByValor("40").getId() : null),
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "41", 18, 5, modeloAsics != null ? modeloAsics.getId() : null, tallaRepository.findByValor("41") != null ? tallaRepository.findByValor("41").getId() : null),
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "42", 12, 5, modeloAsics != null ? modeloAsics.getId() : null, tallaRepository.findByValor("42") != null ? tallaRepository.findByValor("42").getId() : null),
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "43", 8, 5, modeloAsics != null ? modeloAsics.getId() : null, tallaRepository.findByValor("43") != null ? tallaRepository.findByValor("43").getId() : null)
            ));

            // ==================== ZAPATILLAS CASUAL ====================

            // Skechers D'Lites - Producto ID: 4001
            ModeloZapato modeloSkechers = modeloRepository.findByNombreIgnoreCase("Skechers D'Lites");
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 4001L, "Skechers D'Lites", "36", 25, 10, modeloSkechers != null ? modeloSkechers.getId() : null, tallaRepository.findByValor("36") != null ? tallaRepository.findByValor("36").getId() : null),
                new Inventario(null, 4001L, "Skechers D'Lites", "37", 30, 10, modeloSkechers != null ? modeloSkechers.getId() : null, tallaRepository.findByValor("37") != null ? tallaRepository.findByValor("37").getId() : null),
                new Inventario(null, 4001L, "Skechers D'Lites", "38", 35, 10, modeloSkechers != null ? modeloSkechers.getId() : null, tallaRepository.findByValor("38") != null ? tallaRepository.findByValor("38").getId() : null),
                new Inventario(null, 4001L, "Skechers D'Lites", "39", 32, 10, modeloSkechers != null ? modeloSkechers.getId() : null, tallaRepository.findByValor("39") != null ? tallaRepository.findByValor("39").getId() : null),
                new Inventario(null, 4001L, "Skechers D'Lites", "40", 28, 10, modeloSkechers != null ? modeloSkechers.getId() : null, tallaRepository.findByValor("40") != null ? tallaRepository.findByValor("40").getId() : null),
                new Inventario(null, 4001L, "Skechers D'Lites", "41", 22, 10, modeloSkechers != null ? modeloSkechers.getId() : null, tallaRepository.findByValor("41") != null ? tallaRepository.findByValor("41").getId() : null),
                new Inventario(null, 4001L, "Skechers D'Lites", "42", 18, 10, modeloSkechers != null ? modeloSkechers.getId() : null, tallaRepository.findByValor("42") != null ? tallaRepository.findByValor("42").getId() : null),
                new Inventario(null, 4001L, "Skechers D'Lites", "43", 12, 10, modeloSkechers != null ? modeloSkechers.getId() : null, tallaRepository.findByValor("43") != null ? tallaRepository.findByValor("43").getId() : null)
            ));

            // Reebok Club C 85 - Producto ID: 4002
            ModeloZapato modeloReebok = modeloRepository.findByNombreIgnoreCase("Reebok Club C 85");
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 4002L, "Reebok Club C 85", "36", 15, 8, modeloReebok != null ? modeloReebok.getId() : null, tallaRepository.findByValor("36") != null ? tallaRepository.findByValor("36").getId() : null),
                new Inventario(null, 4002L, "Reebok Club C 85", "37", 20, 8, modeloReebok != null ? modeloReebok.getId() : null, tallaRepository.findByValor("37") != null ? tallaRepository.findByValor("37").getId() : null),
                new Inventario(null, 4002L, "Reebok Club C 85", "38", 24, 8, modeloReebok != null ? modeloReebok.getId() : null, tallaRepository.findByValor("38") != null ? tallaRepository.findByValor("38").getId() : null),
                new Inventario(null, 4002L, "Reebok Club C 85", "39", 22, 8, modeloReebok != null ? modeloReebok.getId() : null, tallaRepository.findByValor("39") != null ? tallaRepository.findByValor("39").getId() : null),
                new Inventario(null, 4002L, "Reebok Club C 85", "40", 18, 8, modeloReebok != null ? modeloReebok.getId() : null, tallaRepository.findByValor("40") != null ? tallaRepository.findByValor("40").getId() : null),
                new Inventario(null, 4002L, "Reebok Club C 85", "41", 14, 8, modeloReebok != null ? modeloReebok.getId() : null, tallaRepository.findByValor("41") != null ? tallaRepository.findByValor("41").getId() : null),
                new Inventario(null, 4002L, "Reebok Club C 85", "42", 10, 8, modeloReebok != null ? modeloReebok.getId() : null, tallaRepository.findByValor("42") != null ? tallaRepository.findByValor("42").getId() : null),
                new Inventario(null, 4002L, "Reebok Club C 85", "43", 6, 8, modeloReebok != null ? modeloReebok.getId() : null, tallaRepository.findByValor("43") != null ? tallaRepository.findByValor("43").getId() : null)
            ));

            // ==================== BOTAS Y BOTINES ====================

            // Timberland 6-Inch Premium - Producto ID: 5001
            ModeloZapato modeloTimberland = modeloRepository.findByNombreIgnoreCase("Timberland 6-Inch Premium");
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "36", 8, 3, modeloTimberland != null ? modeloTimberland.getId() : null, tallaRepository.findByValor("36") != null ? tallaRepository.findByValor("36").getId() : null),
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "37", 10, 3, modeloTimberland != null ? modeloTimberland.getId() : null, tallaRepository.findByValor("37") != null ? tallaRepository.findByValor("37").getId() : null),
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "38", 12, 3, modeloTimberland != null ? modeloTimberland.getId() : null, tallaRepository.findByValor("38") != null ? tallaRepository.findByValor("38").getId() : null),
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "39", 14, 3, modeloTimberland != null ? modeloTimberland.getId() : null, tallaRepository.findByValor("39") != null ? tallaRepository.findByValor("39").getId() : null),
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "40", 16, 3, modeloTimberland != null ? modeloTimberland.getId() : null, tallaRepository.findByValor("40") != null ? tallaRepository.findByValor("40").getId() : null),
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "41", 12, 3, modeloTimberland != null ? modeloTimberland.getId() : null, tallaRepository.findByValor("41") != null ? tallaRepository.findByValor("41").getId() : null),
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "42", 10, 3, modeloTimberland != null ? modeloTimberland.getId() : null, tallaRepository.findByValor("42") != null ? tallaRepository.findByValor("42").getId() : null),
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "43", 6, 3, modeloTimberland != null ? modeloTimberland.getId() : null, tallaRepository.findByValor("43") != null ? tallaRepository.findByValor("43").getId() : null)
            ));

            // Dr. Martens 1460 - Producto ID: 5002
            ModeloZapato modeloDrMartens = modeloRepository.findByNombreIgnoreCase("Dr. Martens 1460");
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 5002L, "Dr. Martens 1460", "36", 6, 3, modeloDrMartens != null ? modeloDrMartens.getId() : null, tallaRepository.findByValor("36") != null ? tallaRepository.findByValor("36").getId() : null),
                new Inventario(null, 5002L, "Dr. Martens 1460", "37", 8, 3, modeloDrMartens != null ? modeloDrMartens.getId() : null, tallaRepository.findByValor("37") != null ? tallaRepository.findByValor("37").getId() : null),
                new Inventario(null, 5002L, "Dr. Martens 1460", "38", 10, 3, modeloDrMartens != null ? modeloDrMartens.getId() : null, tallaRepository.findByValor("38") != null ? tallaRepository.findByValor("38").getId() : null),
                new Inventario(null, 5002L, "Dr. Martens 1460", "39", 12, 3, modeloDrMartens != null ? modeloDrMartens.getId() : null, tallaRepository.findByValor("39") != null ? tallaRepository.findByValor("39").getId() : null),
                new Inventario(null, 5002L, "Dr. Martens 1460", "40", 10, 3, modeloDrMartens != null ? modeloDrMartens.getId() : null, tallaRepository.findByValor("40") != null ? tallaRepository.findByValor("40").getId() : null),
                new Inventario(null, 5002L, "Dr. Martens 1460", "41", 8, 3, modeloDrMartens != null ? modeloDrMartens.getId() : null, tallaRepository.findByValor("41") != null ? tallaRepository.findByValor("41").getId() : null),
                new Inventario(null, 5002L, "Dr. Martens 1460", "42", 6, 3, modeloDrMartens != null ? modeloDrMartens.getId() : null, tallaRepository.findByValor("42") != null ? tallaRepository.findByValor("42").getId() : null),
                new Inventario(null, 5002L, "Dr. Martens 1460", "43", 4, 3, modeloDrMartens != null ? modeloDrMartens.getId() : null, tallaRepository.findByValor("43") != null ? tallaRepository.findByValor("43").getId() : null)
            ));

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

            // Obtener todos los modelos StepStyle creados
            List<ModeloZapato> modelosStep = modeloRepository.findAll().stream()
                    .filter(m -> m.getNombre() != null && m.getNombre().toLowerCase().startsWith("stepstyle"))
                    .collect(Collectors.toList());

            if (!modelosStep.isEmpty()) {
                long baseProductId = 6000L;
                int modelIndex = 0;
                for (ModeloZapato modelo : modelosStep) {
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
                }
            }
        };
    }
}
