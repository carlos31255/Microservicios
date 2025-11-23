package com.example.ventasservice.service;

import com.example.ventasservice.dto.*;
import com.example.ventasservice.model.Boleta;
import com.example.ventasservice.model.DetalleBoleta;
import com.example.ventasservice.repository.BoletaRepository;
import com.example.ventasservice.repository.DetalleBoletaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class BoletaServiceTest {

    @Mock
    private BoletaRepository boletaRepository;

    @Mock
    private DetalleBoletaRepository detalleBoletaRepository;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private BoletaService boletaService;

    private Boleta boleta;
    private DetalleBoleta detalleBoleta;

    @BeforeEach
    void setUp() {
        boleta = new Boleta();
        boleta.setId(1L);
        boleta.setClienteId(100L);
        boleta.setTotal(50000);
        boleta.setEstado("pendiente");
        boleta.setMetodoPago("efectivo");
        boleta.setFechaVenta(LocalDateTime.now());
        boleta.setFechaCreacion(LocalDateTime.now());
        boleta.setFechaActualizacion(LocalDateTime.now());

        detalleBoleta = new DetalleBoleta();
        detalleBoleta.setId(1L);
        detalleBoleta.setBoletaId(1L);
        detalleBoleta.setInventarioId(10L);
        detalleBoleta.setNombreProducto("Zapatilla Nike");
        detalleBoleta.setTalla("42");
        detalleBoleta.setCantidad(2);
        detalleBoleta.setPrecioUnitario(25000);
        detalleBoleta.setSubtotal(50000);
    }

    @Test
    void testObtenerTodasLasBoletas() {
        Boleta boleta2 = new Boleta();
        boleta2.setId(2L);
        boleta2.setClienteId(200L);
        boleta2.setTotal(75000);
        boleta2.setEstado("confirmada");
        boleta2.setFechaVenta(LocalDateTime.now());

        when(boletaRepository.findAllByOrderByFechaVentaDesc()).thenReturn(Arrays.asList(boleta, boleta2));

        List<BoletaDTO> resultado = boletaService.obtenerTodasLasBoletas();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getId()).isEqualTo(1L);
        assertThat(resultado.get(0).getTotal()).isEqualTo(50000);
        assertThat(resultado.get(1).getId()).isEqualTo(2L);
        verify(boletaRepository).findAllByOrderByFechaVentaDesc();
    }

    @Test
    void testObtenerBoletaPorId() {
        when(boletaRepository.findById(1L)).thenReturn(Optional.of(boleta));
        when(detalleBoletaRepository.findByBoletaIdOrderByIdAsc(1L)).thenReturn(Arrays.asList(detalleBoleta));

        Optional<BoletaDTO> resultado = boletaService.obtenerBoletaPorId(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
        assertThat(resultado.get().getClienteId()).isEqualTo(100L);
        assertThat(resultado.get().getDetalles()).hasSize(1);
        assertThat(resultado.get().getDetalles().get(0).getNombreProducto()).isEqualTo("Zapatilla Nike");
        verify(boletaRepository).findById(1L);
    }

    @Test
    void testObtenerBoletaPorId_noEncontrada() {
        when(boletaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<BoletaDTO> resultado = boletaService.obtenerBoletaPorId(999L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void testObtenerBoletasPorCliente() {
        Boleta boleta2 = new Boleta();
        boleta2.setId(2L);
        boleta2.setClienteId(100L);
        boleta2.setTotal(30000);
        boleta2.setEstado("completada");
        boleta2.setFechaVenta(LocalDateTime.now());

        when(boletaRepository.findByClienteIdOrderByFechaVentaDesc(100L)).thenReturn(Arrays.asList(boleta, boleta2));

        List<BoletaDTO> resultado = boletaService.obtenerBoletasPorCliente(100L);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getClienteId()).isEqualTo(100L);
        assertThat(resultado.get(1).getClienteId()).isEqualTo(100L);
        verify(boletaRepository).findByClienteIdOrderByFechaVentaDesc(100L);
    }

    @Test
    void testObtenerDetallesPorBoleta() {
        DetalleBoleta detalle2 = new DetalleBoleta();
        detalle2.setId(2L);
        detalle2.setBoletaId(1L);
        detalle2.setNombreProducto("Zapatilla Adidas");
        detalle2.setTalla("41");
        detalle2.setCantidad(1);
        detalle2.setPrecioUnitario(30000);
        detalle2.setSubtotal(30000);

        when(detalleBoletaRepository.findByBoletaIdOrderByIdAsc(1L)).thenReturn(Arrays.asList(detalleBoleta, detalle2));

        List<DetalleBoletaDTO> resultado = boletaService.obtenerDetallesPorBoleta(1L);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombreProducto()).isEqualTo("Zapatilla Nike");
        assertThat(resultado.get(1).getNombreProducto()).isEqualTo("Zapatilla Adidas");
        verify(detalleBoletaRepository).findByBoletaIdOrderByIdAsc(1L);
    }

    @Test
    void testCrearBoleta() {
        // Preparar request
        CrearBoletaRequest request = new CrearBoletaRequest();
        request.setClienteId(100L);
        request.setMetodoPago("efectivo");
        request.setObservaciones("Entrega rápida");

        DetalleBoletaDTO detalleDTO = new DetalleBoletaDTO();
        detalleDTO.setInventarioId(10L);
        detalleDTO.setNombreProducto("Zapatilla Nike");
        detalleDTO.setTalla("42");
        detalleDTO.setCantidad(2);
        detalleDTO.setPrecioUnitario(25000);

        request.setDetalles(Arrays.asList(detalleDTO));

        // Mock de repositorios
        when(boletaRepository.save(any(Boleta.class))).thenReturn(boleta);
        when(detalleBoletaRepository.saveAll(anyList())).thenReturn(Arrays.asList(detalleBoleta));
        when(detalleBoletaRepository.findByBoletaIdOrderByIdAsc(1L)).thenReturn(Arrays.asList(detalleBoleta));

        // Mock de WebClient para ajuste de inventario
        doReturn(webClientBuilder).when(webClientBuilder).baseUrl(any());
        when(webClientBuilder.build()).thenReturn(webClient);
        when(webClient.put()).thenReturn(requestBodyUriSpec);
        doAnswer(invocation -> requestBodySpec).when(requestBodyUriSpec).uri(anyString(), any(Object[].class));
        doReturn(requestBodySpec).when(requestBodySpec).contentType(any());
        doReturn(requestBodySpec).when(requestBodySpec).bodyValue(any());
        doReturn(responseSpec).when(requestBodySpec).retrieve();
        when(responseSpec.bodyToMono(String.class)).thenReturn(Mono.just("OK"));

        // Mock de WebClient para crear entrega
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        doReturn(requestBodySpec).when(requestBodyUriSpec).uri(anyString());
        doReturn(responseSpec).when(requestBodySpec).retrieve();
        when(responseSpec.toBodilessEntity()).thenReturn(Mono.just(mock(org.springframework.http.ResponseEntity.class)));

        BoletaDTO resultado = boletaService.crearBoleta(request);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getClienteId()).isEqualTo(100L);
        assertThat(resultado.getTotal()).isEqualTo(50000);
        assertThat(resultado.getDetalles()).hasSize(1);
        verify(boletaRepository).save(any(Boleta.class));
        verify(detalleBoletaRepository).saveAll(anyList());
    }

    @Test
    void testCambiarEstado() {
        when(boletaRepository.findById(1L)).thenReturn(Optional.of(boleta));
        when(boletaRepository.save(any(Boleta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<BoletaDTO> resultado = boletaService.cambiarEstado(1L, "confirmada");

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getEstado()).isEqualTo("confirmada");
        verify(boletaRepository).save(any(Boleta.class));
    }

    @Test
    void testCambiarEstado_noEncontrada() {
        when(boletaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<BoletaDTO> resultado = boletaService.cambiarEstado(999L, "confirmada");

        assertThat(resultado).isEmpty();
        verify(boletaRepository, never()).save(any(Boleta.class));
    }

    @Test
    void testObtenerBoletasPorCliente_listaVacia() {
        when(boletaRepository.findByClienteIdOrderByFechaVentaDesc(999L)).thenReturn(Arrays.asList());

        List<BoletaDTO> resultado = boletaService.obtenerBoletasPorCliente(999L);

        assertThat(resultado).isEmpty();
        verify(boletaRepository).findByClienteIdOrderByFechaVentaDesc(999L);
    }

    @Test
    void testObtenerDetallesPorBoleta_listaVacia() {
        when(detalleBoletaRepository.findByBoletaIdOrderByIdAsc(999L)).thenReturn(Arrays.asList());

        List<DetalleBoletaDTO> resultado = boletaService.obtenerDetallesPorBoleta(999L);

        assertThat(resultado).isEmpty();
        verify(detalleBoletaRepository).findByBoletaIdOrderByIdAsc(999L);
    }
}
