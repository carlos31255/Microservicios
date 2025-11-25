package com.example.inventarioservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopStockDTO {
    public Long productoId;
    public String nombre;
    public String talla;
    public Integer cantidad;
}
