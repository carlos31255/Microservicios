package com.example.ventasservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.VentasService.dto.BoletaDTO;
import com.example.VentasService.dto.CrearBoletaRequest;
import com.example.VentasService.dto.DetalleBoletaDTO;
import com.example.VentasService.model.Boleta;
import com.example.VentasService.model.DetalleBoleta;
import com.example.VentasService.repository.BoletaRepository;
import com.example.VentasService.repository.DetalleBoletaRepository;
import com.example.VentasService.service.BoletaService;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class BoletaServiceTest {

    @Mock
    private BoletaRepository boletaRepository;

    @Mock
    private DetalleBoletaRepository detalleBoletaRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private BoletaService boletaService;

    // Mocks auxiliares para la cadena de WebClient
    @Mock private WebClient webClientMock;
    @Mock private WebClient.RequestBodyUriSpec requestBodyUriSpecMock;
    @Mock private WebClient.RequestBodySpec requestBodySpecMock;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpecMock;
    @Mock private WebClient.ResponseSpec responseSpecMock;

    private Boleta boletaEjemplo;
    private DetalleBoleta detalleEjemplo;
    private DetalleBoletaDTO detalleDTOEjemplo;

    @BeforeEach
    void setUp() {
        
        ReflectionTestUtils.setField(boletaService, "inventarioServiceUrl", "http://localhost:8082");
        ReflectionTestUtils.setField(boletaService, "entregasServiceUrl", "http://localhost:8084");

        // Datos de prueba comunes
        boletaEjemplo = new Boleta();
        boletaEjemplo.setId(1L);
        boletaEjemplo.setClienteId(100L);
        boletaEjemplo.setTotal(5000);
        boletaEjemplo.setEstado("pendiente");
        boletaEjemplo.setFechaVenta(LocalDateTime.now());
        boletaEjemplo.setMetodoPago("EFECTIVO");

        detalleEjemplo = new DetalleBoleta();
        detalleEjemplo.setId(10L);
        detalleEjemplo.setBoletaId(1L);
        detalleEjemplo.setInventarioId(50L);
        detalleEjemplo.setCantidad(2);
        detalleEjemplo.setPrecioUnitario(2500);
        detalleEjemplo.setSubtotal(5000);

        detalleDTOEjemplo = new DetalleBoletaDTO();
        detalleDTOEjemplo.setInventarioId(50L);
        detalleDTOEjemplo.setCantidad(2);
        detalleDTOEjemplo.setPrecioUnitario(2500);
    }

    @Test
    void testObtenerTodasLasBoletas_Ok() {
        // Arrange
        when(boletaRepository.findAllByOrderByFechaVentaDesc()).thenReturn(Arrays.asList(boletaEjemplo));

        // Act
        List<BoletaDTO> resultado = boletaService.obtenerTodasLasBoletas();

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(boletaEjemplo.getId(), resultado.get(0).getId());
        verify(boletaRepository, times(1)).findAllByOrderByFechaVentaDesc();
    }

    @Test
    void testObtenerBoletaPorId_Ok() {
        // Arrange
        when(boletaRepository.findById(1L)).thenReturn(Optional.of(boletaEjemplo));
        when(detalleBoletaRepository.findByBoletaIdOrderByIdAsc(1L)).thenReturn(Arrays.asList(detalleEjemplo));

        // Act
        Optional<BoletaDTO> resultado = boletaService.obtenerBoletaPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(boletaEjemplo.getId(), resultado.get().getId());
        assertEquals(1, resultado.get().getDetalles().size());
        verify(boletaRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerBoletasPorCliente_Ok() {
        // Arrange
        when(boletaRepository.findByClienteIdOrderByFechaVentaDesc(100L)).thenReturn(Arrays.asList(boletaEjemplo));

        // Act
        List<BoletaDTO> resultado = boletaService.obtenerBoletasPorCliente(100L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(100L, resultado.get(0).getClienteId());
        verify(boletaRepository, times(1)).findByClienteIdOrderByFechaVentaDesc(100L);
    }

    @Test
    void testObtenerDetallesPorBoleta_Ok() {
        // Arrange
        when(detalleBoletaRepository.findByBoletaIdOrderByIdAsc(1L)).thenReturn(Arrays.asList(detalleEjemplo));

        // Act
        List<DetalleBoletaDTO> resultado = boletaService.obtenerDetallesPorBoleta(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(detalleEjemplo.getInventarioId(), resultado.get(0).getInventarioId());
        verify(detalleBoletaRepository, times(1)).findByBoletaIdOrderByIdAsc(1L);
    }

    @Test
    void testCrearBoleta_Ok() {
        /*
         Este test es complejo porque el método hace 3 cosas:
         1. Guarda en DB (Boleta y Detalles)
         2. Llama a Inventario (PUT)
         3. Llama a Entregas (POST)
         Debemos mockear todo esto.
        */

        // 1. Arrange Datos de Entrada
        CrearBoletaRequest request = new CrearBoletaRequest();
        request.setClienteId(100L);
        request.setMetodoPago("TARJETA");
        request.setDetalles(Arrays.asList(detalleDTOEjemplo));

        // 2. Arrange Mocks de Base de Datos
        when(boletaRepository.save(any(Boleta.class))).thenReturn(boletaEjemplo);
        when(detalleBoletaRepository.saveAll(anyList())).thenReturn(Arrays.asList(detalleEjemplo));
        // El metodo final convierte a DTO buscando los detalles de nuevo
        when(detalleBoletaRepository.findByBoletaIdOrderByIdAsc(anyLong())).thenReturn(Arrays.asList(detalleEjemplo));

        // Simulamos que el builder siempre devuelve nuestro cliente mock
        when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClientMock);

        // Configuración para la llamada PUT (Inventario)
        when(webClientMock.put()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(anyString(), any(Object[].class))).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.contentType(any())).thenReturn(requestBodySpecMock);
        when(requestBodySpecMock.bodyValue(any())).thenReturn(requestHeadersSpecMock);
        when(requestHeadersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.bodyToMono(String.class)).thenReturn(Mono.just("OK")); // Respuesta exitosa

        // Configuración para la llamada POST (Entregas)
        when(webClientMock.post()).thenReturn(requestBodyUriSpecMock);
        when(requestBodyUriSpecMock.uri(anyString())).thenReturn(requestBodySpecMock);
        // Reutilizamos los mocks de body y headers ya configurados arriba o re-configuramos si es necesario
        // Nota: al ser mocks, si ya definimos el comportamiento genérico (any), servirá para ambos.
        when(responseSpecMock.toBodilessEntity()).thenReturn(Mono.empty());

        // Act
        BoletaDTO resultado = boletaService.crearBoleta(request);

        // Assert
        assertNotNull(resultado);
        assertEquals(boletaEjemplo.getTotal(), resultado.getTotal());
        assertEquals("pendiente", resultado.getEstado());
        
        // Verificaciones
        verify(boletaRepository, times(1)).save(any(Boleta.class));
        verify(detalleBoletaRepository, times(1)).saveAll(anyList());
        
        // Verificar que se intentó llamar a Inventario (PUT) y Entregas (POST)
        verify(webClientMock, times(1)).put(); 
        verify(webClientMock, times(1)).post();
    }

    @Test
    void testCambiarEstado_Ok() {
        // Arrange
        String nuevoEstado = "ENVIADO";
        Boleta boletaActualizada = new Boleta();
        boletaActualizada.setId(1L);
        boletaActualizada.setEstado(nuevoEstado);
        
        when(boletaRepository.findById(1L)).thenReturn(Optional.of(boletaEjemplo));
        when(boletaRepository.save(any(Boleta.class))).thenReturn(boletaActualizada);

        // Act
        Optional<BoletaDTO> resultado = boletaService.cambiarEstado(1L, nuevoEstado);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(nuevoEstado, resultado.get().getEstado());
        verify(boletaRepository).save(argThat(b -> b.getEstado().equals(nuevoEstado)));
    }
}
