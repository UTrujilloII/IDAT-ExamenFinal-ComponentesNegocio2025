package pe.idat.biblioteca.dto.prestamo;

import java.time.LocalDate;

public record PrestamoResponse(Long id,
                               LocalDate fechaPrestamo,
                               LocalDate fechaDevolucion,
                               boolean devuelto,
                               Long idUsuario,
                               String nombreUsuario,
                               Long idLibro,
                               String tituloLibro) {
}
