package pe.edu.idat.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.idat.biblioteca.entity.Prestamo;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    List<Prestamo> findByUsuarioId(Long usuarioId);

    List<Prestamo> findByUsuarioIdAndEstado(Long usuarioId, Prestamo.EstadoPrestamo estado);

    List<Prestamo> findByLibroIdAndEstado(Long libroId, Prestamo.EstadoPrestamo estado);

    List<Prestamo> findByEstado(Prestamo.EstadoPrestamo estado);

    boolean existsByLibroIdAndEstado(Long libroId, Prestamo.EstadoPrestamo estado);

}