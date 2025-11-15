package com.example.UsuarioService.config;

import com.example.UsuarioService.model.Persona;
import com.example.UsuarioService.model.Rol;
import com.example.UsuarioService.model.Usuario;
import com.example.UsuarioService.model.Cliente;
import com.example.UsuarioService.repository.PersonaRepository;
import com.example.UsuarioService.repository.RolRepository;
import com.example.UsuarioService.repository.UsuarioRepository;
import com.example.UsuarioService.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class LoadDatabase {

    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    // bean para encriptar contraseñas
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner initDatabase(
            RolRepository rolRepository,
            PersonaRepository personaRepository,
            UsuarioRepository usuarioRepository,
            ClienteRepository clienteRepository,
            BCryptPasswordEncoder passwordEncoder) {

        return args -> {
            log.info("=== Iniciando precarga de datos ===");

            // ============================================
            // Precargar roles
            // ============================================
            log.info("Paso 1: Verificando roles...");
            
            Rol adminRol = rolRepository.findByNombreRol("Administrador")
                    .orElseGet(() -> {
                        log.info("Creando rol: Administrador");
                        return rolRepository.save(new Rol(
                                null,
                                "Administrador",
                                "Encargado principal: acceso total al sistema, gestión de usuarios, inventario y configuración"
                        ));
                    });

            Rol transportistaRol = rolRepository.findByNombreRol("Transportista")
                    .orElseGet(() -> {
                        log.info("Creando rol: Transportista");
                        return rolRepository.save(new Rol(
                                null,
                                "Transportista",
                                "Personal de entregas: gestión de despachos y seguimiento de pedidos"
                        ));
                    });

            Rol clienteRol = rolRepository.findByNombreRol("Cliente")
                    .orElseGet(() -> {
                        log.info("Creando rol: Cliente");
                        return rolRepository.save(new Rol(
                                null,
                                "Cliente",
                                "Usuario cliente: puede ver catálogo, realizar pedidos y gestionar su perfil"
                        ));
                    });

            log.info("Roles verificados: {} roles en base de datos", rolRepository.count());

            // ============================================
            // Precargar Personas
            // ============================================
            log.info("Paso 2: Verificando personas...");

            // Administrador
            Persona personaAdmin = personaRepository.findByUsername("admin@zapateria.cl")
                    .orElseGet(() -> {
                        log.info("Creando persona: Admin Sistema");
                        Persona p = new Persona();
                        p.setNombre("Admin");
                        p.setApellido("Sistema");
                        p.setRut("11111111-1");
                        p.setTelefono("+56911111111");
                        p.setEmail("admin@zapateria.cl");
                        p.setIdComuna(197L); // Santiago (RM)
                        p.setCalle("Av. Libertador Bernardo O'Higgins");
                        p.setNumeroPuerta("1000");
                        p.setUsername("admin@zapateria.cl");
                        p.setPassHash(passwordEncoder.encode("admin123!"));
                        p.setFechaRegistro(System.currentTimeMillis());
                        p.setEstado("activo");
                        return personaRepository.save(p);
                    });

            // Transportista
            Persona personaTransportista = personaRepository.findByUsername("tra@zapa.cl")
                    .orElseGet(() -> {
                        log.info("Creando persona: Juan Transportista");
                        Persona p = new Persona();
                        p.setNombre("Juan");
                        p.setApellido("Transportista");
                        p.setRut("33333333-3");
                        p.setTelefono("+56933333333");
                        p.setEmail("tra@zapa.cl");
                        p.setIdComuna(205L); // Maipú (RM)
                        p.setCalle("Av. Pajaritos");
                        p.setNumeroPuerta("1234");
                        p.setUsername("tra@zapa.cl");
                        p.setPassHash(passwordEncoder.encode("tra123!"));
                        p.setFechaRegistro(System.currentTimeMillis());
                        p.setEstado("activo");
                        return personaRepository.save(p);
                    });

            // Cliente principal
            Persona personaCliente1 = personaRepository.findByUsername("cli@zapa.cl")
                    .orElseGet(() -> {
                        log.info("Creando persona: María González");
                        Persona p = new Persona();
                        p.setNombre("María");
                        p.setApellido("González");
                        p.setRut("44444444-4");
                        p.setTelefono("+56944444444");
                        p.setEmail("cli@zapa.cl");
                        p.setIdComuna(198L); // Providencia (RM)
                        p.setCalle("Av. Providencia");
                        p.setNumeroPuerta("1234");
                        p.setUsername("cli@zapa.cl");
                        p.setPassHash(passwordEncoder.encode("cli123!"));
                        p.setFechaRegistro(System.currentTimeMillis());
                        p.setEstado("activo");
                        return personaRepository.save(p);
                    });

            // Clientes adicionales
            Persona personaPedro = personaRepository.findByUsername("pedro.ramirez@email.cl")
                    .orElseGet(() -> {
                        log.info("Creando persona: Pedro Ramírez");
                        Persona p = new Persona();
                        p.setNombre("Pedro");
                        p.setApellido("Ramírez");
                        p.setRut("15678432-1");
                        p.setTelefono("+56912345678");
                        p.setEmail("pedro.ramirez@email.cl");
                        p.setIdComuna(199L); // Las Condes (RM)
                        p.setCalle("Av. Apoquindo");
                        p.setNumeroPuerta("567");
                        p.setUsername("pedro.ramirez@email.cl");
                        p.setPassHash(passwordEncoder.encode("pedro123"));
                        p.setFechaRegistro(System.currentTimeMillis());
                        p.setEstado("activo");
                        return personaRepository.save(p);
                    });

            Persona personaAna = personaRepository.findByUsername("ana.martinez@email.cl")
                    .orElseGet(() -> {
                        log.info("Creando persona: Ana Martínez");
                        Persona p = new Persona();
                        p.setNombre("Ana");
                        p.setApellido("Martínez");
                        p.setRut("18234567-8");
                        p.setTelefono("+56987654321");
                        p.setEmail("ana.martinez@email.cl");
                        p.setIdComuna(200L); // Ñuñoa (RM)
                        p.setCalle("Av. Irarrázaval");
                        p.setNumeroPuerta("123");
                        p.setUsername("ana.martinez@email.cl");
                        p.setPassHash(passwordEncoder.encode("ana123"));
                        p.setFechaRegistro(System.currentTimeMillis());
                        p.setEstado("activo");
                        return personaRepository.save(p);
                    });

            Persona personaLuis = personaRepository.findByUsername("luis.fernandez@email.cl")
                    .orElseGet(() -> {
                        log.info("Creando persona: Luis Fernández");
                        Persona p = new Persona();
                        p.setNombre("Luis");
                        p.setApellido("Fernández");
                        p.setRut("19876543-2");
                        p.setTelefono("+56945678901");
                        p.setEmail("luis.fernandez@email.cl");
                        p.setIdComuna(207L); // Puente Alto (RM)
                        p.setCalle("Av. Concha y Toro");
                        p.setNumeroPuerta("890");
                        p.setUsername("luis.fernandez@email.cl");
                        p.setPassHash(passwordEncoder.encode("luis123"));
                        p.setFechaRegistro(System.currentTimeMillis());
                        p.setEstado("activo");
                        return personaRepository.save(p);
                    });

            Persona personaCarmen = personaRepository.findByUsername("carmen.lopez@email.cl")
                    .orElseGet(() -> {
                        log.info("Creando persona: Carmen López");
                        Persona p = new Persona();
                        p.setNombre("Carmen");
                        p.setApellido("López");
                        p.setRut("17345678-9");
                        p.setTelefono("+56956781234");
                        p.setEmail("carmen.lopez@email.cl");
                        p.setIdComuna(201L); // Vitacura (RM)
                        p.setCalle("Av. Vitacura");
                        p.setNumeroPuerta("456");
                        p.setUsername("carmen.lopez@email.cl");
                        p.setPassHash(passwordEncoder.encode("carmen123"));
                        p.setFechaRegistro(System.currentTimeMillis());
                        p.setEstado("activo");
                        return personaRepository.save(p);
                    });

            Persona personaRoberto = personaRepository.findByUsername("roberto.silva@email.cl")
                    .orElseGet(() -> {
                        log.info("Creando persona: Roberto Silva");
                        Persona p = new Persona();
                        p.setNombre("Roberto");
                        p.setApellido("Silva");
                        p.setRut("16543210-7");
                        p.setTelefono("+56923456789");
                        p.setEmail("roberto.silva@email.cl");
                        p.setIdComuna(209L); // La Florida (RM)
                        p.setCalle("Av. Vicuña Mackenna");
                        p.setNumeroPuerta("2345");
                        p.setUsername("roberto.silva@email.cl");
                        p.setPassHash(passwordEncoder.encode("roberto123"));
                        p.setFechaRegistro(System.currentTimeMillis());
                        p.setEstado("activo");
                        return personaRepository.save(p);
                    });

            Persona personaPatricia = personaRepository.findByUsername("patricia.rojas@email.cl")
                    .orElseGet(() -> {
                        log.info("Creando persona: Patricia Rojas");
                        Persona p = new Persona();
                        p.setNombre("Patricia");
                        p.setApellido("Rojas");
                        p.setRut("20123456-5");
                        p.setTelefono("+56934567890");
                        p.setEmail("patricia.rojas@email.cl");
                        p.setIdComuna(211L); // San Bernardo (RM)
                        p.setCalle("Av. Eyzaguirre");
                        p.setNumeroPuerta("678");
                        p.setUsername("patricia.rojas@email.cl");
                        p.setPassHash(passwordEncoder.encode("patricia123"));
                        p.setFechaRegistro(System.currentTimeMillis());
                        p.setEstado("activo");
                        return personaRepository.save(p);
                    });

            Persona personaDiego = personaRepository.findByUsername("diego.morales@email.cl")
                    .orElseGet(() -> {
                        log.info("Creando persona: Diego Morales");
                        Persona p = new Persona();
                        p.setNombre("Diego");
                        p.setApellido("Morales");
                        p.setRut("19234567-3");
                        p.setTelefono("+56945678012");
                        p.setEmail("diego.morales@email.cl");
                        p.setIdComuna(203L); // Macul (RM)
                        p.setCalle("Av. Macul");
                        p.setNumeroPuerta("1567");
                        p.setUsername("diego.morales@email.cl");
                        p.setPassHash(passwordEncoder.encode("diego123"));
                        p.setFechaRegistro(System.currentTimeMillis());
                        p.setEstado("activo");
                        return personaRepository.save(p);
                    });

            Persona personaSofia = personaRepository.findByUsername("sofia.vargas@email.cl")
                    .orElseGet(() -> {
                        log.info("Creando persona: Sofía Vargas");
                        Persona p = new Persona();
                        p.setNombre("Sofía");
                        p.setApellido("Vargas");
                        p.setRut("18765432-0");
                        p.setTelefono("+56956789012");
                        p.setEmail("sofia.vargas@email.cl");
                        p.setIdComuna(204L); // Peñalolén (RM)
                        p.setCalle("Av. Grecia");
                        p.setNumeroPuerta("890");
                        p.setUsername("sofia.vargas@email.cl");
                        p.setPassHash(passwordEncoder.encode("sofia123"));
                        p.setFechaRegistro(System.currentTimeMillis());
                        p.setEstado("activo");
                        return personaRepository.save(p);
                    });

            log.info("Personas verificadas: {} personas en base de datos", personaRepository.count());

            // ============================================
            // Precargar Usuarios (relación Persona-Rol)
            // ============================================
            log.info("Paso 3: Verificando usuarios...");

            // Usuario Admin
            if (!usuarioRepository.existsByIdPersona(personaAdmin.getIdPersona())) {
                log.info("Creando usuario: Admin - Administrador");
                Usuario usuarioAdmin = new Usuario();
                usuarioAdmin.setIdPersona(personaAdmin.getIdPersona());
                usuarioAdmin.setIdRol(adminRol.getIdRol());
                usuarioRepository.save(usuarioAdmin);
            }

            // Usuario Transportista
            if (!usuarioRepository.existsByIdPersona(personaTransportista.getIdPersona())) {
                log.info("Creando usuario: Juan - Transportista");
                Usuario usuarioTransportista = new Usuario();
                usuarioTransportista.setIdPersona(personaTransportista.getIdPersona());
                usuarioTransportista.setIdRol(transportistaRol.getIdRol());
                usuarioRepository.save(usuarioTransportista);
            }

            // Usuarios Clientes
            Persona[] personasClientes = {
                    personaCliente1, personaPedro, personaAna, personaLuis,
                    personaCarmen, personaRoberto, personaPatricia, personaDiego, personaSofia
            };

            for (Persona persona : personasClientes) {
                if (!usuarioRepository.existsByIdPersona(persona.getIdPersona())) {
                    log.info("Creando usuario: {} {} - Cliente", persona.getNombre(), persona.getApellido());
                    Usuario usuario = new Usuario();
                    usuario.setIdPersona(persona.getIdPersona());
                    usuario.setIdRol(clienteRol.getIdRol());
                    usuarioRepository.save(usuario);
                }
            }

            log.info("Usuarios verificados: {} usuarios en base de datos", usuarioRepository.count());

            // ============================================
            // Precargar Clientes (información adicional)
            // ============================================
            log.info("Paso 4: Verificando clientes...");

            // Definir categorías para cada cliente
            String[][] clientesData = {
                    {personaCliente1.getIdPersona().toString(), "VIP"},      // María González
                    {personaPedro.getIdPersona().toString(), "regular"},     // Pedro Ramírez
                    {personaAna.getIdPersona().toString(), "VIP"},           // Ana Martínez
                    {personaLuis.getIdPersona().toString(), "regular"},      // Luis Fernández
                    {personaCarmen.getIdPersona().toString(), "premium"},    // Carmen López
                    {personaRoberto.getIdPersona().toString(), "regular"},   // Roberto Silva
                    {personaPatricia.getIdPersona().toString(), "VIP"},      // Patricia Rojas
                    {personaDiego.getIdPersona().toString(), "premium"},     // Diego Morales
                    {personaSofia.getIdPersona().toString(), "regular"}      // Sofía Vargas
            };

            for (String[] clienteData : clientesData) {
                Long idPersona = Long.parseLong(clienteData[0]);
                String categoria = clienteData[1];

                clienteRepository.findByIdPersonaWithPersona(idPersona)
                        .orElseGet(() -> {
                            Persona persona = personaRepository.findById(idPersona).get();
                            log.info("Creando cliente: {} {} - {}", persona.getNombre(), persona.getApellido(), categoria);
                            Cliente cliente = new Cliente();
                            cliente.setIdPersona(idPersona);
                            cliente.setCategoria(categoria);
                            return clienteRepository.save(cliente);
                        });
            }

            log.info("Clientes verificados: {} clientes en base de datos", clienteRepository.count());

            // ============================================
            // Resumen de precarga
            // ============================================
            log.info("=== Precarga completada exitosamente ===");
            log.info("Total Roles: {}", rolRepository.count());
            log.info("Total Personas: {}", personaRepository.count());
            log.info("Total Usuarios: {}", usuarioRepository.count());
            log.info("Total Clientes: {}", clienteRepository.count());
            log.info("========================================");
            log.info("Credenciales de prueba:");
            log.info("  Admin: admin@zapateria.cl / admin123!");
            log.info("  Transportista: tra@zapa.cl / tra123!");
            log.info("  Cliente: cli@zapa.cl / cli123!");
            log.info("========================================");
        };
    }
}
