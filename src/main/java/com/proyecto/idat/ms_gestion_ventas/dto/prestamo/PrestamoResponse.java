package com.proyecto.idat.ms_gestion_ventas.dto.prestamo;

import java.time.LocalDate;

public record PrestamoResponse(
        Long idPrestamo,
        Long idLibro,
        String tituloLibro,
        Long idUsuario,
        String username,
        LocalDate fechaPrestamo,
        LocalDate fechaDevolucion,
        String estado
) {
}
