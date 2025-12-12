package com.proyecto.idat.ms_gestion_ventas.dto.libro;

import java.time.LocalDate;

public record LibroResponse(
        Long idLibro,
        String titulo,
        String autor,
        String isbn,
        LocalDate fechaPublicacion,
        Integer ejemplaresTotales,
        Integer ejemplaresDisponibles,
        Long categoriaId,
        String categoriaNombre
) {
}
