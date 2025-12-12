package pe.edu.idat.biblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying; // Importar Modifying
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.idat.biblioteca.entity.Prestamo;
import pe.edu.idat.biblioteca.entity.Usuario;
import pe.edu.idat.biblioteca.entity.EstadoPrestamo;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    // Verifica si existen préstamos de un usuario con un estado específico (ej. ACTIVO/VENCIDO)
    boolean existsByUsuarioIdAndEstado(Long usuarioId, EstadoPrestamo estado);

    // Encuentra préstamos por usuario y los ordena
    List<Prestamo> findByUsuarioOrderByFechaPrestamoDesc(Usuario usuario);

    // Consulta nativa para listar préstamos activos
    @Query("SELECT p FROM Prestamo p WHERE p.estado = 'ACTIVO'")
    List<Prestamo> findAllActivos();

    // Verifica si un libro tiene préstamos activos
    boolean existsByLibroIdAndEstado(Long libroId, EstadoPrestamo estado);

    // Cuenta los préstamos activos de un usuario
    @Query("SELECT COUNT(p) FROM Prestamo p WHERE p.usuario = :usuario AND p.estado = 'ACTIVO'")
    long countByUsuarioAndEstadoActivo(@Param("usuario") Usuario usuario);
    @Modifying // Indica que la consulta modifica la base de datos
    @Query("DELETE FROM Prestamo p WHERE p.usuario.id = :usuarioId")
    void deleteByUsuarioId(@Param("usuarioId") Long usuarioId);
}