package com.example.inventarioservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TallaDetalleDTO {
    public String talla;
    public Integer cantidad;
    public Integer stockMinimo;
    public Boolean alertaStockBajo;
}
