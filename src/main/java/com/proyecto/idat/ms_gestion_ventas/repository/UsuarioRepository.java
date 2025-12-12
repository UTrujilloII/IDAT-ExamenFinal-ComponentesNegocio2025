package com.proyecto.idat.ms_gestion_ventas.repository;

import com.proyecto.idat.ms_gestion_ventas.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // sirven para validar duplicados EXCLUYENDO un id concreto
    boolean existsByUsernameAndIdUsuarioNot(String username, Long idUsuario);

    boolean existsByEmailAndIdUsuarioNot(String email, Long idUsuario);
}
