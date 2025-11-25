
package com.example.inventarioservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductoResumenDTO {
    public Long id;
    public Long productoId;
    public String nombre;
    public String talla;
    public Integer cantidadActual;
    public Integer stockMinimo;
    public Integer diferencia;
}
