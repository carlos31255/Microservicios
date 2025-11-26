package com.example.inventarioservice.service;

import com.example.inventarioservice.dto.InventarioDTO;
import com.example.inventarioservice.model.Inventario;
import com.example.inventarioservice.model.Producto;
import com.example.inventarioservice.model.Talla;
import com.example.inventarioservice.repository.InventarioRepository;
import com.example.inventarioservice.repository.MovimientoInventarioRepository;
import com.example.inventarioservice.repository.ProductoRepository;
import com.example.inventarioservice.repository.TallaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private MovimientoInventarioRepository movimientoInventarioRepository;

    @Mock
    private TallaRepository tallaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private InventarioService inventarioService;

    @Test
    void testObtenerTodoElInventario() {
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProductoId(100L);
        inventario.setNombre("Zapatilla Nike");
        inventario.setTalla("42");
        inventario.setCantidad(50);
        inventario.setStockMinimo(10);

        when(inventarioRepository.findAll()).thenReturn(Arrays.asList(inventario));

        List<InventarioDTO> resultado = inventarioService.obtenerTodoElInventario();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Zapatilla Nike");
        assertThat(resultado.get(0).getCantidad()).isEqualTo(50);
    }

    @Test
    void testObtenerInventarioPorId() {
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setNombre("Zapatilla Nike");
        inventario.setCantidad(50);

        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(inventario));

        InventarioDTO resultado = inventarioService.obtenerInventarioPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Zapatilla Nike");
    }

    @Test
    void testObtenerInventarioPorProductoId() {
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProductoId(100L);
        inventario.setTalla("42");
        inventario.setCantidad(50);

        when(inventarioRepository.findAllByProductoId(100L)).thenReturn(Arrays.asList(inventario));

        List<InventarioDTO> resultado = inventarioService.obtenerInventarioPorProductoId(100L);

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getProductoId()).isEqualTo(100L);
        assertThat(resultado.get(0).getTalla()).isEqualTo("42");
    }

    @Test
    void testObtenerInventarioPorProductoIdYTalla() {
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProductoId(100L);
        inventario.setTalla("42");
        inventario.setCantidad(50);

        when(inventarioRepository.findByProductoIdAndTalla(100L, "42")).thenReturn(Optional.of(inventario));

        InventarioDTO resultado = inventarioService.obtenerInventarioPorProductoIdYTalla(100L, "42");

        assertThat(resultado.getProductoId()).isEqualTo(100L);
        assertThat(resultado.getTalla()).isEqualTo("42");
        assertThat(resultado.getCantidad()).isEqualTo(50);
    }

    @Test
    void testObtenerProductosConStockBajo() {
        Inventario inventario1 = new Inventario();
        inventario1.setId(1L);
        inventario1.setNombre("Zapatilla Nike");
        inventario1.setCantidad(5);
        inventario1.setStockMinimo(10);

        Inventario inventario2 = new Inventario();
        inventario2.setId(2L);
        inventario2.setNombre("Zapatilla Adidas");
        inventario2.setCantidad(50);
        inventario2.setStockMinimo(10);

        when(inventarioRepository.findAll()).thenReturn(Arrays.asList(inventario1, inventario2));

        List<InventarioDTO> resultado = inventarioService.obtenerProductosConStockBajo();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Zapatilla Nike");
        assertThat(resultado.get(0).getCantidad()).isEqualTo(5);
    }

    @Test
    void testBuscarProductosPorNombre() {
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setNombre("Zapatilla Nike");
        inventario.setCantidad(50);

        when(inventarioRepository.findByNombreContainingIgnoreCase("Nike")).thenReturn(Arrays.asList(inventario));

        List<InventarioDTO> resultado = inventarioService.buscarProductosPorNombre("Nike");

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNombre()).isEqualTo("Zapatilla Nike");
    }

    @Test
   void testCrearInventario() {
        // 1. PREPARAR EL INPUT (DTO)
        InventarioDTO dto = new InventarioDTO();
        dto.setProductoId(100L);
        dto.setTallaId(10L); 
        dto.setCantidad(50);
        dto.setStockMinimo(10);

        // 2. PREPARAR LOS DATOS SIMULADOS (MOCKS)
        Producto mockProducto = new Producto();
        mockProducto.setId(100L);
        mockProducto.setNombre("Zapatilla Nike");

        // El servicio buscará la Talla, así que debemos simular que existe
        Talla mockTalla = new Talla();
        mockTalla.setId(10L);
        mockTalla.setValor("42");

        // Este es el objeto que simularás que guarda la BD
        Inventario inventarioGuardado = new Inventario();
        inventarioGuardado.setId(1L);
        inventarioGuardado.setProductoId(100L);
        inventarioGuardado.setNombre("Zapatilla Nike");
        inventarioGuardado.setTalla("42");
        inventarioGuardado.setCantidad(50);
        inventarioGuardado.setStockMinimo(10);

        // 3. DEFINIR EL COMPORTAMIENTO DE LOS MOCKS
        when(productoRepository.findById(100L)).thenReturn(Optional.of(mockProducto));
        
        when(tallaRepository.findById(10L)).thenReturn(Optional.of(mockTalla));
        
        when(inventarioRepository.existsByProductoIdAndTalla(100L, "42")).thenReturn(false);
        
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(inventarioGuardado);

        Inventario resultado = inventarioService.crearInventario(dto);

        // 5. VERIFICACIONES (ASSERTS)
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNombre()).isEqualTo("Zapatilla Nike");
        assertThat(resultado.getCantidad()).isEqualTo(50);
        assertThat(resultado.getTalla()).isEqualTo("42");
    }
    @Test
    void testVerificarDisponibilidad() {
        Inventario inventario = new Inventario();
        inventario.setId(1L);
        inventario.setProductoId(100L);
        inventario.setTalla("42");
        inventario.setCantidad(50);

        when(inventarioRepository.findByProductoIdAndTalla(100L, "42")).thenReturn(Optional.of(inventario));

        boolean resultado = inventarioService.verificarDisponibilidad(100L, "42", 30);

        assertThat(resultado).isTrue();
    }

    @Test
    void testEliminarInventario() {
        when(inventarioRepository.existsById(1L)).thenReturn(true);

        inventarioService.eliminarInventario(1L);

        verify(inventarioRepository).deleteById(1L);
    }
}
