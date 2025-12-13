package pe.edu.idat.biblioteca.dto.prestamo;

import pe.edu.idat.biblioteca.entity.EstadoPrestamo;
import java.time.LocalDate;

public record PrestamoResponse(
        Long id,
        Long usuarioId,
        String username,
        Long libroId,
        String tituloLibro,
        LocalDate fechaPrestamo,
        LocalDate fechaDevolucionEstimada,
        LocalDate fechaDevolucionReal,
        EstadoPrestamo estado
) {}