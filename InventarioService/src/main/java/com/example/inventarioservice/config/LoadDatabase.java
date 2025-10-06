package com.example.inventarioservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.inventarioservice.model.Inventario;
import com.example.inventarioservice.repository.InventarioRepository;

import java.util.Arrays;

@Configuration
public class LoadDatabase {
    
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(InventarioRepository inventarioRepository) {
        return args -> {
            // Verificar si ya hay datos
            if (inventarioRepository.count() > 0) {
                log.info("La base de datos ya contiene datos. Saltando la inicialización.");
                return;
            }

            log.info("Cargando datos de prueba en la base de datos de inventario...");

            // ==================== ZAPATILLAS DEPORTIVAS ====================
            
            // Nike Air Max 270 - Producto ID: 1001
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 1001L, "Nike Air Max 270", "36", 15, 5),
                new Inventario(null, 1001L, "Nike Air Max 270", "37", 20, 5),
                new Inventario(null, 1001L, "Nike Air Max 270", "38", 25, 5),
                new Inventario(null, 1001L, "Nike Air Max 270", "39", 30, 5),
                new Inventario(null, 1001L, "Nike Air Max 270", "40", 28, 5),
                new Inventario(null, 1001L, "Nike Air Max 270", "41", 22, 5),
                new Inventario(null, 1001L, "Nike Air Max 270", "42", 18, 5),
                new Inventario(null, 1001L, "Nike Air Max 270", "43", 12, 5)
            ));

            // Adidas Ultraboost 22 - Producto ID: 1002
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "36", 10, 5),
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "37", 18, 5),
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "38", 22, 5),
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "39", 25, 5),
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "40", 20, 5),
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "41", 15, 5),
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "42", 12, 5),
                new Inventario(null, 1002L, "Adidas Ultraboost 22", "43", 8, 5)
            ));

            // Puma RS-X - Producto ID: 1003
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 1003L, "Puma RS-X", "36", 12, 5),
                new Inventario(null, 1003L, "Puma RS-X", "37", 16, 5),
                new Inventario(null, 1003L, "Puma RS-X", "38", 20, 5),
                new Inventario(null, 1003L, "Puma RS-X", "39", 18, 5),
                new Inventario(null, 1003L, "Puma RS-X", "40", 22, 5),
                new Inventario(null, 1003L, "Puma RS-X", "41", 16, 5),
                new Inventario(null, 1003L, "Puma RS-X", "42", 10, 5),
                new Inventario(null, 1003L, "Puma RS-X", "43", 6, 5)
            ));

            // ==================== ZAPATILLAS URBANAS ====================

            // Converse Chuck Taylor All Star - Producto ID: 2001
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 2001L, "Converse Chuck Taylor", "36", 20, 10),
                new Inventario(null, 2001L, "Converse Chuck Taylor", "37", 25, 10),
                new Inventario(null, 2001L, "Converse Chuck Taylor", "38", 30, 10),
                new Inventario(null, 2001L, "Converse Chuck Taylor", "39", 28, 10),
                new Inventario(null, 2001L, "Converse Chuck Taylor", "40", 24, 10),
                new Inventario(null, 2001L, "Converse Chuck Taylor", "41", 20, 10),
                new Inventario(null, 2001L, "Converse Chuck Taylor", "42", 15, 10),
                new Inventario(null, 2001L, "Converse Chuck Taylor", "43", 10, 10)
            ));

            // Vans Old Skool - Producto ID: 2002
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 2002L, "Vans Old Skool", "36", 18, 8),
                new Inventario(null, 2002L, "Vans Old Skool", "37", 22, 8),
                new Inventario(null, 2002L, "Vans Old Skool", "38", 26, 8),
                new Inventario(null, 2002L, "Vans Old Skool", "39", 24, 8),
                new Inventario(null, 2002L, "Vans Old Skool", "40", 20, 8),
                new Inventario(null, 2002L, "Vans Old Skool", "41", 16, 8),
                new Inventario(null, 2002L, "Vans Old Skool", "42", 12, 8),
                new Inventario(null, 2002L, "Vans Old Skool", "43", 8, 8)
            ));

            // ==================== ZAPATILLAS RUNNING ====================

            // New Balance 1080v12 - Producto ID: 3001
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 3001L, "New Balance 1080v12", "36", 8, 5),
                new Inventario(null, 3001L, "New Balance 1080v12", "37", 12, 5),
                new Inventario(null, 3001L, "New Balance 1080v12", "38", 15, 5),
                new Inventario(null, 3001L, "New Balance 1080v12", "39", 18, 5),
                new Inventario(null, 3001L, "New Balance 1080v12", "40", 20, 5),
                new Inventario(null, 3001L, "New Balance 1080v12", "41", 14, 5),
                new Inventario(null, 3001L, "New Balance 1080v12", "42", 10, 5),
                new Inventario(null, 3001L, "New Balance 1080v12", "43", 6, 5)
            ));

            // Asics Gel-Kayano 29 - Producto ID: 3002
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "36", 10, 5),
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "37", 14, 5),
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "38", 18, 5),
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "39", 16, 5),
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "40", 22, 5),
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "41", 18, 5),
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "42", 12, 5),
                new Inventario(null, 3002L, "Asics Gel-Kayano 29", "43", 8, 5)
            ));

            // ==================== ZAPATILLAS CASUAL ====================

            // Skechers D'Lites - Producto ID: 4001
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 4001L, "Skechers D'Lites", "36", 25, 10),
                new Inventario(null, 4001L, "Skechers D'Lites", "37", 30, 10),
                new Inventario(null, 4001L, "Skechers D'Lites", "38", 35, 10),
                new Inventario(null, 4001L, "Skechers D'Lites", "39", 32, 10),
                new Inventario(null, 4001L, "Skechers D'Lites", "40", 28, 10),
                new Inventario(null, 4001L, "Skechers D'Lites", "41", 22, 10),
                new Inventario(null, 4001L, "Skechers D'Lites", "42", 18, 10),
                new Inventario(null, 4001L, "Skechers D'Lites", "43", 12, 10)
            ));

            // Reebok Club C 85 - Producto ID: 4002
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 4002L, "Reebok Club C 85", "36", 15, 8),
                new Inventario(null, 4002L, "Reebok Club C 85", "37", 20, 8),
                new Inventario(null, 4002L, "Reebok Club C 85", "38", 24, 8),
                new Inventario(null, 4002L, "Reebok Club C 85", "39", 22, 8),
                new Inventario(null, 4002L, "Reebok Club C 85", "40", 18, 8),
                new Inventario(null, 4002L, "Reebok Club C 85", "41", 14, 8),
                new Inventario(null, 4002L, "Reebok Club C 85", "42", 10, 8),
                new Inventario(null, 4002L, "Reebok Club C 85", "43", 6, 8)
            ));

            // ==================== BOTAS Y BOTINES ====================

            // Timberland 6-Inch Premium - Producto ID: 5001
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "36", 8, 3),
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "37", 10, 3),
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "38", 12, 3),
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "39", 14, 3),
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "40", 16, 3),
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "41", 12, 3),
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "42", 10, 3),
                new Inventario(null, 5001L, "Timberland 6-Inch Premium", "43", 6, 3)
            ));

            // Dr. Martens 1460 - Producto ID: 5002
            inventarioRepository.saveAll(Arrays.asList(
                new Inventario(null, 5002L, "Dr. Martens 1460", "36", 6, 3),
                new Inventario(null, 5002L, "Dr. Martens 1460", "37", 8, 3),
                new Inventario(null, 5002L, "Dr. Martens 1460", "38", 10, 3),
                new Inventario(null, 5002L, "Dr. Martens 1460", "39", 12, 3),
                new Inventario(null, 5002L, "Dr. Martens 1460", "40", 10, 3),
                new Inventario(null, 5002L, "Dr. Martens 1460", "41", 8, 3),
                new Inventario(null, 5002L, "Dr. Martens 1460", "42", 6, 3),
                new Inventario(null, 5002L, "Dr. Martens 1460", "43", 4, 3)
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
        };
    }
}
