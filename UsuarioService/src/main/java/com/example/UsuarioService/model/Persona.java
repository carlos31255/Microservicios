package com.example.UsuarioService.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "persona",
    indexes = {
        @Index(name = "idx_rut", columnList = "rut", unique = true),
        @Index(name = "idx_username", columnList = "username", unique = true)
    }
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Persona")
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Long idPersona;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true, length = 12)
    private String rut; // RUT completo con dígito verificador (ej: "12345678-9")

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String email;

    @Column(name = "id_comuna")
    private Long idComuna;

    @Column(length = 200)
    private String calle;

    @Column(name = "numero_puerta", length = 10)
    private String numeroPuerta;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passHash;

    @Column(name = "fecha_registro", nullable = false)
    private Long fechaRegistro;

    @Column(nullable = false, length = 20)
    private String estado = "activo";

    // Constructor para crear nuevas personas (sin ID)
    public Persona(String nombre, String apellido, String rut, String telefono, String email,
                   Long idComuna, String calle, String numeroPuerta, String username,
                   String passHash, Long fechaRegistro, String estado) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
        this.telefono = telefono;
        this.email = email;
        this.idComuna = idComuna;
        this.calle = calle;
        this.numeroPuerta = numeroPuerta;
        this.username = username;
        this.passHash = passHash;
        this.fechaRegistro = fechaRegistro != null ? fechaRegistro : System.currentTimeMillis();
        this.estado = estado != null ? estado : "activo";
    }
}
