package pe.edu.idat.biblioteca_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.idat.biblioteca_api.model.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // Para el login (Spring Security usará esto)
    Optional<Usuario> findByUsername(String username);

    // Para validar antes de registrar (evitar duplicados)
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}