package com.example.VentasService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltroReporteRequest {
    private Integer anio;
    private Integer mes; // Puede ser null

   
    
}
