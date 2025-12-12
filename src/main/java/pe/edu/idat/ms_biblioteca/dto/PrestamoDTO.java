
package pe.edu.idat.ms_biblioteca.dto;

import java.time.LocalDate;

public record PrestamoDTO(
        Long idUsuario,
        Long idLibro,
        LocalDate fechaPrestamo,
        LocalDate fechaDevolucion
) {}
