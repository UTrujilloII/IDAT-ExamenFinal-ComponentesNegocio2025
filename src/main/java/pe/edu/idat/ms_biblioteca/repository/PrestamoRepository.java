package pe.edu.idat.ms_biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.edu.idat.ms_biblioteca.entity.Prestamo;

import java.util.List;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long>
{
    List<Prestamo> findByUsuarioId(Long idUsuario);
}
