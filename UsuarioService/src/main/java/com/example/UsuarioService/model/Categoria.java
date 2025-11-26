package com.example.UsuarioService.model;

public enum Categoria {
    REGULAR,
    VIP,
    PREMIUM,
    ADMIN;

    public static Categoria fromString(String value) {
        if (value == null) throw new IllegalArgumentException("Categoría no puede ser nula");
        try {
            return Categoria.valueOf(value.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Categoría inválida: " + value + ". Valores permitidos: REGULAR, VIP, PREMIUM");
        }
    }
}
