package pe.edu.idat.biblioteca.dto.prestamo;

import java.time.LocalDate;

public record PrestamoResponse (
        Long idPrestamo,
        LocalDate fechaPrestamo,
        LocalDate fechaEntrega,
        LocalDate fechaEntregada,
        String libro,
        String usuario,
        String estado
){}
