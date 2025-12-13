package pe.edu.idat.msbiblioteca.dto.prestamo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para registrar la devolución de un libro.
 *
 * @see <a href="https://github.com/vansfanelx/">GitHub Profile</a>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevolucionRequestDTO {

    /**
     * Observaciones sobre la devolución (estado del libro, daños, etc.)
     */
    private String observaciones;
}

