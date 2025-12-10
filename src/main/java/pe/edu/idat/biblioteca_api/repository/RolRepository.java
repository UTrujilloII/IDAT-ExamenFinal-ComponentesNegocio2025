package pe.edu.idat.biblioteca_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.idat.biblioteca_api.model.Rol;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    // Método personalizado para buscar un rol por su nombre (enum)
    Optional<Rol> findByNombre(Rol.TipoRol nombre);
}