package pe.edu.idat.msbiblioteca.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.idat.msbiblioteca.entity.Prestamo;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para la entidad Prestamo.
 *
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    /**
     * Busca todos los préstamos de un usuario específico
     *
     * @param usuarioId ID del usuario
     * @return Lista de préstamos del usuario
     */
    @Query("SELECT p FROM Prestamo p WHERE p.usuario.id = :usuarioId ORDER BY p.fechaPrestamo DESC")
    List<Prestamo> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    /**
     * Busca todos los préstamos de un libro específico
     *
     * @param libroId ID del libro
     * @return Lista de préstamos del libro
     */
    @Query("SELECT p FROM Prestamo p WHERE p.libro.id = :libroId ORDER BY p.fechaPrestamo DESC")
    List<Prestamo> findByLibroId(@Param("libroId") Long libroId);

    /**
     * Busca préstamos activos (no devueltos) de un usuario
     *
     * @param usuarioId ID del usuario
     * @return Lista de préstamos activos del usuario
     */
    @Query("SELECT p FROM Prestamo p WHERE p.usuario.id = :usuarioId AND p.estado = 'ACTIVO' ORDER BY p.fechaPrestamo DESC")
    List<Prestamo> findPrestamosActivosByUsuario(@Param("usuarioId") Long usuarioId);

    /**
     * Busca todos los préstamos por estado
     *
     * @param estado Estado del préstamo (ACTIVO, DEVUELTO, VENCIDO)
     * @return Lista de préstamos con el estado especificado
     */
    List<Prestamo> findByEstado(String estado);

    /**
     * Busca préstamos vencidos (fecha de devolución esperada superada y no devueltos)
     *
     * @param fechaActual Fecha actual para comparar
     * @return Lista de préstamos vencidos
     */
    @Query("SELECT p FROM Prestamo p WHERE p.fechaDevolucionEsperada < :fechaActual " +
           "AND p.fechaDevolucionReal IS NULL AND p.estado != 'DEVUELTO'")
    List<Prestamo> findPrestamosVencidos(@Param("fechaActual") LocalDate fechaActual);

    /**
     * Busca préstamos por rango de fechas de préstamo
     *
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha fin del rango
     * @return Lista de préstamos en el rango de fechas
     */
    @Query("SELECT p FROM Prestamo p WHERE p.fechaPrestamo BETWEEN :fechaInicio AND :fechaFin ORDER BY p.fechaPrestamo DESC")
    List<Prestamo> findByFechasPrestamo(@Param("fechaInicio") LocalDate fechaInicio,
                                        @Param("fechaFin") LocalDate fechaFin);

    /**
     * Cuenta los préstamos activos de un usuario
     *
     * @param usuarioId ID del usuario
     * @return Número de préstamos activos
     */
    @Query("SELECT COUNT(p) FROM Prestamo p WHERE p.usuario.id = :usuarioId AND p.estado = 'ACTIVO'")
    Long contarPrestamosActivosByUsuario(@Param("usuarioId") Long usuarioId);

    /**
     * Verifica si un usuario tiene préstamos vencidos
     *
     * @param usuarioId ID del usuario
     * @param fechaActual Fecha actual para comparar
     * @return true si el usuario tiene préstamos vencidos, false en caso contrario
     */
    @Query("SELECT COUNT(p) > 0 FROM Prestamo p WHERE p.usuario.id = :usuarioId " +
           "AND p.fechaDevolucionEsperada < :fechaActual " +
           "AND p.fechaDevolucionReal IS NULL AND p.estado != 'DEVUELTO'")
    boolean tienePrestamosVencidos(@Param("usuarioId") Long usuarioId,
                                   @Param("fechaActual") LocalDate fechaActual);

    /**
     * Busca préstamos con multa pendiente
     *
     * @return Lista de préstamos con multa > 0
     */
    @Query("SELECT p FROM Prestamo p WHERE p.multa > 0 ORDER BY p.multa DESC")
    List<Prestamo> findPrestamosConMulta();

    /**
     * Calcula el total de multas pendientes de un usuario
     *
     * @param usuarioId ID del usuario
     * @return Suma total de multas del usuario
     */
    @Query("SELECT COALESCE(SUM(p.multa), 0) FROM Prestamo p WHERE p.usuario.id = :usuarioId")
    Double calcularMultasTotalesByUsuario(@Param("usuarioId") Long usuarioId);

    /**
     * Obtiene estadísticas de préstamos: total, activos, devueltos, vencidos
     *
     * @return Lista con contadores [total, activos, devueltos, vencidos]
     */
    @Query("SELECT COUNT(p), " +
           "SUM(CASE WHEN p.estado = 'ACTIVO' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN p.estado = 'DEVUELTO' THEN 1 ELSE 0 END), " +
           "SUM(CASE WHEN p.estado = 'VENCIDO' THEN 1 ELSE 0 END) " +
           "FROM Prestamo p")
    List<Object[]> obtenerEstadisticasPrestamos();

    /**
     * Busca los últimos N préstamos realizados
     *
     * @param limit Número de préstamos a retornar
     * @return Lista de los últimos préstamos
     */
    @Query(value = "SELECT * FROM prestamos ORDER BY fecha_prestamo DESC LIMIT :limit", nativeQuery = true)
    List<Prestamo> findUltimosPrestamos(@Param("limit") int limit);
}

