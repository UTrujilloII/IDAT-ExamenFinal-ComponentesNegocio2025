package com.biblioteca.api.repository;

import com.biblioteca.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * =========================================================
 * REPOSITORIO USUARIO
 * Incluye un método adicional para buscar por username,
 * necesario para autenticación en la Fase 3 (JWT).
 * =========================================================
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);
}
