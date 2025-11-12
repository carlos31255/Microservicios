package com.example.inventarioservice.service;

import com.example.inventarioservice.dto.MovimientoInventarioDTO;
import com.example.inventarioservice.model.MovimientosInventario;
import com.example.inventarioservice.repository.MovimientoInventarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovimientoInventarioServiceTest {

    @Mock
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @InjectMocks
    private MovimientoInventarioService movimientoInventarioService;

    @Test
    void testObtenerTodosLosMovimientos() {
        MovimientosInventario movimiento = new MovimientosInventario();
        movimiento.setId(1L);
        movimiento.setInventarioId(10L);
        movimiento.setTipo("entrada");
        movimiento.setCantidad(50);
        movimiento.setMotivo("Stock inicial");
        movimiento.setUsuarioId(1L);

        when(movimientoInventarioRepository.findAll()).thenReturn(Arrays.asList(movimiento));

        List<MovimientoInventarioDTO> resultado = movimientoInventarioService.obtenerTodosLosMovimientos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipo()).isEqualTo("entrada");
        assertThat(resultado.get(0).getCantidad()).isEqualTo(50);
    }

    @Test
    void testObtenerMovimientoPorId() {
        MovimientosInventario movimiento = new MovimientosInventario();
        movimiento.setId(1L);
        movimiento.setTipo("entrada");
        movimiento.setCantidad(50);

        when(movimientoInventarioRepository.findById(1L)).thenReturn(Optional.of(movimiento));

        MovimientoInventarioDTO resultado = movimientoInventarioService.obtenerMovimientoPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getTipo()).isEqualTo("entrada");
    }

    @Test
    void testObtenerMovimientosPorInventarioId() {
        MovimientosInventario movimiento = new MovimientosInventario();
        movimiento.setId(1L);
        movimiento.setInventarioId(10L);
        movimiento.setTipo("entrada");
        movimiento.setCantidad(50);

        when(movimientoInventarioRepository.findByInventarioIdOrderByIdDesc(10L))
                .thenReturn(Arrays.asList(movimiento));

        List<MovimientoInventarioDTO> resultado = movimientoInventarioService.obtenerMovimientosPorInventarioId(10L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getInventarioId()).isEqualTo(10L);
    }

    @Test
    void testObtenerMovimientosPorTipo() {
        MovimientosInventario movimiento = new MovimientosInventario();
        movimiento.setId(1L);
        movimiento.setTipo("salida");
        movimiento.setCantidad(20);

        when(movimientoInventarioRepository.findByTipo("salida")).thenReturn(Arrays.asList(movimiento));

        List<MovimientoInventarioDTO> resultado = movimientoInventarioService.obtenerMovimientosPorTipo("salida");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getTipo()).isEqualTo("salida");
        assertThat(resultado.get(0).getCantidad()).isEqualTo(20);
    }

    @Test
    void testObtenerMovimientosPorUsuarioId() {
        MovimientosInventario movimiento = new MovimientosInventario();
        movimiento.setId(1L);
        movimiento.setUsuarioId(5L);
        movimiento.setTipo("entrada");

        when(movimientoInventarioRepository.findByUsuarioId(5L)).thenReturn(Arrays.asList(movimiento));

        List<MovimientoInventarioDTO> resultado = movimientoInventarioService.obtenerMovimientosPorUsuarioId(5L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getUsuarioId()).isEqualTo(5L);
    }
}
