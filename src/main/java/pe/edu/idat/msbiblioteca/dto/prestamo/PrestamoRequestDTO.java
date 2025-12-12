package pe.edu.idat.msbiblioteca.dto.prestamo;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para la creación de un nuevo préstamo.
 * Contiene las validaciones necesarias para registrar un préstamo.
 *
 * @author Jonathan Jiménez
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestamoRequestDTO {

    /**
     * ID del usuario que solicita el préstamo - obligatorio
     */
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    /**
     * ID del libro a prestar - obligatorio
     */
    @NotNull(message = "El ID del libro es obligatorio")
    private Long libroId;

    /**
     * Fecha de préstamo - por defecto la fecha actual
     */
    private LocalDate fechaPrestamo = LocalDate.now();

    /**
     * Días de préstamo - por defecto 14 días
     */
    @Min(value = 1, message = "El período de préstamo debe ser al menos 1 día")
    @Max(value = 90, message = "El período de préstamo no puede exceder 90 días")
    private Integer diasPrestamo = 14;

    /**
     * Observaciones adicionales sobre el préstamo - opcional
     */
    @Size(max = 500, message = "Las observaciones no pueden exceder 500 caracteres")
    private String observaciones;
}

