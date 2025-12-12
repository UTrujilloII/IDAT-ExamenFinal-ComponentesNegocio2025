package com.proyecto.idat.ms_gestion_ventas;

import com.proyecto.idat.ms_gestion_ventas.entity.Rol;
import com.proyecto.idat.ms_gestion_ventas.entity.Usuario;
import com.proyecto.idat.ms_gestion_ventas.repository.RolRepository;
import com.proyecto.idat.ms_gestion_ventas.repository.UsuarioRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
@Slf4j
public class MsGestionVentasApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsGestionVentasApplication.class, args);
    }


    // Mensaje en consola con la URL de Swagger
    @PostConstruct
    public void logSwaggerUrl() {
        log.info("======================================================================");
        log.info(" Swagger UI disponible en: http://localhost:9090/swagger-ui.html");
        log.info("======================================================================");
    }


    // Datos iniciales: roles y usuario admin
    @Bean
    CommandLineRunner initData(RolRepository rolRepository,
                               UsuarioRepository usuarioRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {

            Rol adminRol = rolRepository.findByNombre("ADMIN")
                    .orElseGet(() -> {
                        Rol r = new Rol();
                        r.setNombre("ADMIN");
                        return rolRepository.save(r);
                    });

            Rol userRol = rolRepository.findByNombre("USUARIO")
                    .orElseGet(() -> {
                        Rol r = new Rol();
                        r.setNombre("USUARIO");
                        return rolRepository.save(r);
                    });

            if (usuarioRepository.findByUsername("admin").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setUsername("admin");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setNombreCompleto("Administrador Biblioteca");
                admin.setEmail("admin@biblioteca.com");
                admin.setRoles(Set.of(adminRol));
                usuarioRepository.save(admin);
            }
        };
    }
}
