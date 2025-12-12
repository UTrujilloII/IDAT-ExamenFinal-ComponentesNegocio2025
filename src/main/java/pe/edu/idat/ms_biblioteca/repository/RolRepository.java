package pe.edu.idat.ms_biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.idat.ms_biblioteca.entity.Rol;
import java.util.Optional;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(String nombre);

}

