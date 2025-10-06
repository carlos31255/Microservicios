package com.example.geografiaservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CiudadDTO {
    private Long id;
    private String nombre;
    private Long regionId;
}
