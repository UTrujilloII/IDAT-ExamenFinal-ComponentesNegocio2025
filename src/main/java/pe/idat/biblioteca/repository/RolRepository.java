package pe.idat.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.idat.biblioteca.entity.Rol;

import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol,Long>
{
    Optional<Rol> findByNombre(String rol);
}
