# Servicio de Entregas - Documentación

## Descripción
Microservicio para la gestión de entregas asociadas a las ventas realizadas en el sistema de zapatería. Maneja la asignación de transportistas, seguimiento de estados y consultas relacionadas con las entregas.

## Puerto
**8084**

## Base de Datos
**entregas_db** (MySQL en puerto 3306)

## Integración con Otros Microservicios

### 1. Integración con VentasService (Puerto 8084)
El servicio de entregas depende del servicio de ventas para obtener información sobre las boletas.

#### Datos Requeridos de VentasService:
- **id_boleta** (Integer): Identificador único de la boleta de venta
- **Estado de la Boleta**: Idealmente, solo se deberían crear entregas para boletas "confirmadas"

#### Flujo de Integración:
1. Cuando se crea una boleta en VentasService, opcionalmente se puede crear una entrega
2. EntregasService almacena el `id_boleta` como referencia
3. Para obtener detalles completos de la venta, sería necesario consultar a VentasService

#### Tipos de Datos:
- EntregasService usa `Integer` para `id_boleta` (compatible con VentasService)

### 2. Integración con UsuarioService (Puerto 8083)
El servicio de entregas necesita identificar transportistas para asignar entregas.

#### Datos Requeridos de UsuarioService:
- **id_transportista** (Long): Corresponde al `id_persona` de un usuario con rol de transportista
- El transportista debe ser un Usuario activo en el sistema
- Se debe verificar el rol del usuario para confirmar que es transportista

#### Flujo de Integración:
1. Al asignar un transportista, se debe validar que el `id_transportista` exista en UsuarioService
2. El transportista debe tener rol activo y permisos adecuados
3. EntregasService consulta a UsuarioService para validar transportistas

#### Recomendaciones:
- Crear un endpoint en UsuarioService para validar transportistas: `GET /api/usuarios/{id}/es-transportista`
- O consultar: `GET /api/usuarios/{id}` y verificar el rol

### 3. Posible Integración con GeografiaService (Puerto: A definir)
Para gestionar direcciones de entrega y rutas.

#### Datos que Podría Requerir:
- Direcciones de entrega vinculadas a las boletas
- Cálculo de rutas para los transportistas
- Zonas de cobertura

## Estados de Entrega

### Estados Disponibles:
1. **pendiente**: Entrega creada, sin transportista asignado
2. **en_camino**: Transportista asignado, entrega en curso
3. **entregada**: Entrega completada exitosamente
4. **cancelada**: Entrega cancelada

### Transiciones de Estado Válidas:
```
pendiente → en_camino (al asignar transportista)
pendiente → cancelada
en_camino → entregada
en_camino → cancelada
```

## Endpoints Principales

### Crear Entrega
```
POST /api/entregas
Body: {
  "idBoleta": 123,  // Integer - debe existir en VentasService
  "idTransportista": 456,  // Long (opcional) - debe existir en UsuarioService
  "estadoEntrega": "pendiente",  // String (opcional, default: "pendiente")
  "observacion": "string"  // String (opcional)
}
```

### Asignar Transportista
```
PATCH /api/entregas/{id}/asignar-transportista
Body: {
  "idTransportista": 456,  // Long - debe ser un usuario válido con rol transportista
  "observacion": "string"  // String (opcional)
}
```

### Actualizar Estado
```
PATCH /api/entregas/{id}/estado
Body: {
  "estadoEntrega": "en_camino",  // String - valores: pendiente|en_camino|entregada|cancelada
  "observacion": "string"  // String (opcional)
}
```

### Consultar por Boleta
```
GET /api/entregas/boleta/{idBoleta}
```

### Consultar por Transportista
```
GET /api/entregas/transportista/{idTransportista}
```

## Validaciones Recomendadas

### Al Crear Entrega:
1. ✅ Verificar que no existe otra entrega para la misma boleta
2. ⚠️ **FALTA**: Validar que la boleta existe en VentasService
3. ⚠️ **FALTA**: Validar que la boleta está en estado "confirmada"
4. ✅ Si se proporciona transportista, validar que existe (implementación pendiente)

### Al Asignar Transportista:
1. ✅ Verificar que la entrega existe
2. ⚠️ **FALTA**: Validar que el transportista existe en UsuarioService
3. ⚠️ **FALTA**: Validar que el usuario tiene rol de transportista
4. ⚠️ **FALTA**: Validar que el transportista está activo

### Al Actualizar Estado:
1. ✅ Verificar que el estado es válido
2. ✅ Registrar fecha de entrega cuando el estado sea "entregada"
3. ✅ Validar transiciones de estado

## Mejoras Recomendadas

### 1. Implementar Cliente HTTP para Comunicación entre Microservicios
```java
// Agregar dependencia en pom.xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>

// O usar RestTemplate / OpenFeign
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>
```

### 2. Crear DTOs de Respuesta de Otros Servicios
```java
// Para VentasService
public class BoletaResponseDTO {
    private Integer id;
    private Integer clienteId;
    private String estado;
    // ... otros campos
}

// Para UsuarioService
public class UsuarioResponseDTO {
    private Long idPersona;
    private Long idRol;
    private Boolean activo;
    // ... otros campos
}
```

### 3. Implementar Validaciones Asíncronas
- Validar boleta en VentasService antes de crear entrega
- Validar transportista en UsuarioService antes de asignar

### 4. Manejo de Errores de Integración
- CircuitBreaker para tolerancia a fallos
- Fallback cuando otros servicios no están disponibles
- Retry policies para reintentos automáticos

### 5. Usar Enum en lugar de String para Estados
- Ya se creó `EstadoEntrega.java` enum
- Actualizar entidad, DTOs y validaciones para usar el enum

## Problemas Corregidos

### 1. ✅ Declaraciones de Paquete
- **Problema**: Los archivos tenían `package main.java.com.example.entregasService.*`
- **Solución**: Corregido a `package com.example.entregasService.*`

### 2. ✅ Inconsistencia en Group ID
- **Problema**: pom.xml usaba `com.zapateria` pero el código usaba `com.example`
- **Solución**: Actualizado pom.xml a `com.example` para mantener consistencia

### 3. ✅ Tipos de Datos Incompatibles
- **Problema**: VentasService usa `Integer` para id_boleta, EntregasService usaba `Long`
- **Solución**: Cambiado `idBoleta` a `Integer` en toda la aplicación

### 4. ✅ Estados de Entrega
- **Problema**: Estados manejados como String sin validación centralizada
- **Solución**: Creado enum `EstadoEntrega` con validaciones

## Configuración

### application.properties
```properties
server.port=8084
spring.application.name=entregas-service

# Base de datos
spring.datasource.url=jdbc:mysql://localhost:3306/entregas_db
spring.datasource.username=root
spring.datasource.password=1234

# URLs de otros microservicios (recomendado agregar)
app.ventas-service.url=http://localhost:8084
app.usuario-service.url=http://localhost:8083
app.geografia-service.url=http://localhost:8085
```

## Swagger/OpenAPI
Acceder a la documentación interactiva en:
```
http://localhost:8084/swagger-ui.html
```

## Notas Importantes

1. **Validación de Boletas**: Actualmente no se valida que la boleta exista en VentasService. Esto debe implementarse.

2. **Validación de Transportistas**: Actualmente no se valida que el transportista exista y tenga el rol adecuado en UsuarioService. Esto debe implementarse.

3. **Consistencia de Datos**: Los microservicios deben mantener consistencia eventual. Considerar implementar:
   - Eventos de dominio
   - Mensajería asíncrona (RabbitMQ, Kafka)
   - Saga pattern para transacciones distribuidas

4. **Configuración de IDE**: Si estás usando Eclipse y ves errores de "non-project file", puede ser necesario:
   - Limpiar el proyecto: Project → Clean
   - Actualizar Maven: Alt+F5 → Update Project
   - Verificar que la estructura de carpetas sea correcta

## Próximos Pasos

1. Implementar cliente HTTP para comunicación con VentasService
2. Implementar cliente HTTP para comunicación con UsuarioService
3. Agregar validaciones de existencia de boletas y transportistas
4. Considerar implementar eventos para notificar cambios de estado
5. Agregar pruebas de integración
6. Documentar APIs de los otros microservicios para facilitar integración
