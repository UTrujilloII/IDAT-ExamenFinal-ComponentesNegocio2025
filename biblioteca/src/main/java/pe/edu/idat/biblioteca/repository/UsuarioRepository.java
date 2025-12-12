package pe.edu.idat.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.idat.biblioteca.entity.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByDni(String dni);
    boolean existsByDni(String dni);
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    boolean existsByTelefono(String telefono);

    boolean existsByTelefonoAndIdIsNot(String telefono, Long id);
}