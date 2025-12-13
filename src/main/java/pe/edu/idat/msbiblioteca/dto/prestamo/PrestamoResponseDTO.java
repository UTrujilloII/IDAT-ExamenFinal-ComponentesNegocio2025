package pe.edu.idat.msbiblioteca.dto.prestamo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para la respuesta de información de un préstamo.
 * Incluye información del usuario y libro asociados.
 *
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoResponseDTO {

    /**
     * ID único del préstamo
     */
    private Long id;

    /**
     * ID del usuario que realizó el préstamo
     */
    private Long usuarioId;

    /**
     * Nombre de usuario
     */
    private String usuarioNombre;

    /**
     * ID del libro prestado
     */
    private Long libroId;

    /**
     * Título del libro prestado
     */
    private String libroTitulo;

    /**
     * Autor del libro prestado
     */
    private String libroAutor;

    /**
     * ISBN del libro prestado
     */
    private String libroIsbn;

    /**
     * Fecha en que se realizó el préstamo
     */
    private LocalDate fechaPrestamo;

    /**
     * Fecha límite para devolver el libro
     */
    private LocalDate fechaDevolucionEsperada;

    /**
     * Fecha real de devolución (null si no se ha devuelto)
     */
    private LocalDate fechaDevolucionReal;

    /**
     * Estado del préstamo (ACTIVO, DEVUELTO, VENCIDO)
     */
    private String estado;

    /**
     * Monto de multa por retraso
     */
    private Double multa;

    /**
     * Días de retraso en la devolución
     */
    private Long diasRetraso;

    /**
     * Observaciones adicionales
     */
    private String observaciones;
}
