package pe.edu.idat.msbiblioteca.dto.libro;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para la creación de un nuevo libro en el sistema.
 * Contiene validaciones para garantizar la integridad de los datos.
 *
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LibroRequestDTO {

    /**
     * Código ISBN del libro - obligatorio y único
     */
    @NotBlank(message = "El ISBN es obligatorio")
    @Size(min = 10, max = 20, message = "El ISBN debe tener entre 10 y 20 caracteres")
    private String isbn;

    /**
     * Título del libro - obligatorio
     */
    @NotBlank(message = "El título es obligatorio")
    @Size(max = 200, message = "El título no puede exceder 200 caracteres")
    private String titulo;

    /**
     * Autor del libro - obligatorio
     */
    @NotBlank(message = "El autor es obligatorio")
    @Size(max = 150, message = "El autor no puede exceder 150 caracteres")
    private String autor;

    /**
     * Editorial del libro - opcional
     */
    @Size(max = 150, message = "La editorial no puede exceder 150 caracteres")
    private String editorial;

    /**
     * Año de publicación - debe ser válido (entre 1000 y año actual + 1)
     */
    @Min(value = 1000, message = "El año de publicación debe ser mayor a 1000")
    @Max(value = 2026, message = "El año de publicación no puede ser futuro")
    private Integer anioPublicacion;

    /**
     * Categoría del libro - opcional
     */
    @Size(max = 100, message = "La categoría no puede exceder 100 caracteres")
    private String categoria;

    /**
     * Cantidad total de copias del libro - obligatorio y positivo
     */
    @NotNull(message = "El número de copias totales es obligatorio")
    @Min(value = 1, message = "Debe haber al menos 1 copia")
    private Integer copiasTotales;

    /**
     * Cantidad de copias disponibles - por defecto igual a copias totales
     */
    @Min(value = 0, message = "Las copias disponibles no pueden ser negativas")
    private Integer copiasDisponibles;

    /**
     * Descripción o sinopsis del libro - opcional
     */
    @Size(max = 5000, message = "La descripción no puede exceder 5000 caracteres")
    private String descripcion;

    /**
     * Ubicación física del libro en la biblioteca - opcional
     */
    @Size(max = 50, message = "La ubicación no puede exceder 50 caracteres")
    private String ubicacion;
}
