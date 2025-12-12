package com.biblioteca.biblioteca_api.config;

import com.biblioteca.biblioteca_api.entity.Rol;
import com.biblioteca.biblioteca_api.entity.TipoRol;
import com.biblioteca.biblioteca_api.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;

    @Override
    public void run(String... args) throws Exception {
        // Verifica si ROLE_USUARIO existe
        if (rolRepository.findByNombre(TipoRol.ROLE_USUARIO).isEmpty()) {
            Rol rolUsuario = new Rol();
            rolUsuario.setNombre(TipoRol.ROLE_USUARIO);
            rolRepository.save(rolUsuario);
            System.out.println("Rol ROLE_USUARIO creado.");
        }

        // Verifica si ROLE_ADMIN existe
        if (rolRepository.findByNombre(TipoRol.ROLE_ADMIN).isEmpty()) {
            Rol rolAdmin = new Rol();
            rolAdmin.setNombre(TipoRol.ROLE_ADMIN);
            rolRepository.save(rolAdmin);
            System.out.println("Rol ROLE_ADMIN creado.");
        }
    }
}
