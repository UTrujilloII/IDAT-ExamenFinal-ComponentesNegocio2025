package pe.edu.idat.msbiblioteca.dto.libro;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para la respuesta de información de un libro.
 * Se utiliza para retornar datos de libros al cliente.
 *
 * @author Jonathan Jiménez
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroResponseDTO {

    /**
     * ID único del libro
     */
    private Long id;

    /**
     * Código ISBN del libro
     */
    private String isbn;

    /**
     * Título del libro
     */
    private String titulo;

    /**
     * Autor del libro
     */
    private String autor;

    /**
     * Editorial del libro
     */
    private String editorial;

    /**
     * Año de publicación
     */
    private Integer anioPublicacion;

    /**
     * Categoría o género del libro
     */
    private String categoria;

    /**
     * Copias disponibles para préstamo
     */
    private Integer copiasDisponibles;

    /**
     * Copias totales del libro
     */
    private Integer copiasTotales;

    /**
     * Descripción o sinopsis
     */
    private String descripcion;

    /**
     * Ubicación física en la biblioteca
     */
    private String ubicacion;

    /**
     * Estado del libro (DISPONIBLE, AGOTADO, EN_MANTENIMIENTO)
     */
    private String estado;

    /**
     * Fecha de registro en el sistema
     */
    private LocalDate fechaRegistro;
}

