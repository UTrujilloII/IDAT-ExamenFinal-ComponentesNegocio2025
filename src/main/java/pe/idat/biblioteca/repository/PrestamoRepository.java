package pe.idat.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.idat.biblioteca.dto.prestamo.PrestamoResponse;
import pe.idat.biblioteca.entity.Prestamo;

import java.util.List;
import java.util.Optional;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    List<Prestamo> findByUsuario_Id(Long usuarioId);
    List<Prestamo> findByUsuarioId(Long usuarioId);

}
