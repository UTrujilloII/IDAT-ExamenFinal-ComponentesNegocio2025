package pe.edu.idat.biblioteca.dto.prestamo;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PrestamoResponse(
        Long id,
        Long usuarioId,
        String nombreUsuario,
        Long libroId,
        String tituloLibro,
        LocalDateTime fechaPrestamo,
        LocalDate fechaDevolucionEsperada,
        LocalDateTime fechaDevolucionReal,
        String estado
) {}