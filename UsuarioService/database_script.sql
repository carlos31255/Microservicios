-- ============================================
-- Script de creación de base de datos: db_usuario
-- Microservicio: UsuarioService
-- ============================================

-- Eliminar base de datos si existe (CUIDADO: esto borrará todos los datos)
DROP DATABASE IF EXISTS db_usuario;

-- Crear base de datos
CREATE DATABASE db_usuario CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE db_usuario;

-- ============================================
-- Tabla: rol
-- Descripción: Roles del sistema (Administrador, Vendedor, Transportista, Cliente)
-- ============================================
CREATE TABLE rol (
    id_rol BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    INDEX idx_nombre_rol (nombre_rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Tabla: persona
-- Descripción: Información personal de todos los usuarios del sistema
-- ============================================
CREATE TABLE persona (
    id_persona BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    rut VARCHAR(12) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    email VARCHAR(100),
    id_comuna BIGINT,
    calle VARCHAR(200),
    numero_puerta VARCHAR(10),
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    fecha_registro BIGINT NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'activo',
    INDEX idx_rut (rut),
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_estado (estado)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Tabla: usuario
-- Descripción: Relación entre persona y rol
-- ============================================
CREATE TABLE usuario (
    id_persona BIGINT PRIMARY KEY,
    id_rol BIGINT NOT NULL,
    FOREIGN KEY (id_persona) REFERENCES persona(id_persona) ON DELETE CASCADE,
    FOREIGN KEY (id_rol) REFERENCES rol(id_rol),
    INDEX idx_usuario_rol (id_rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- Tabla: cliente
-- Descripción: Información adicional de clientes
-- ============================================
CREATE TABLE cliente (
    id_persona BIGINT PRIMARY KEY,
    categoria VARCHAR(20),
    FOREIGN KEY (id_persona) REFERENCES persona(id_persona) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================
-- NOTA: Los datos iniciales se cargarán automáticamente
-- mediante LoadDatabase.java al iniciar el microservicio
-- 
-- Roles que se precargarán:
--   1. Administrador
--   2. Transportista  
--   3. Cliente
-- ============================================
