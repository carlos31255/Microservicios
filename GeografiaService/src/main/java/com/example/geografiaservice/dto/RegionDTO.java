package com.example.geografiaservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionDTO {
    private Long id;
    private String nombre;
    private String codigo;
    private Integer orden;
}
