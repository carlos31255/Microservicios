package com.example.UsuarioService.model;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transportista")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Transportista")
public class Transportista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transportista")
    private Long idTransportista;

    @Column(name = "id_persona", nullable = false)
    private Long idPersona;

    @Column(length = 30)
    private String patente;

    @Column(length = 50)
    private String tipoVehiculo;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "fecha_registro")
    private Long fechaRegistro = System.currentTimeMillis();
}
