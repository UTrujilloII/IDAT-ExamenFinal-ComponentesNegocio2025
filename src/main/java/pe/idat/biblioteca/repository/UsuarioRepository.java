package pe.idat.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.idat.biblioteca.entity.Prestamo;
import pe.idat.biblioteca.entity.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    Optional<Usuario> findByEmail(String email);


}
