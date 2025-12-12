package pe.edu.idat.biblioteca.dto.libro;

public record LibroResponse(
        Long id,
        String titulo,
        String autor,
        String editorial,
        String categoria,
        Integer cantidadTotal,
        Integer cantidadDisponible,
        Boolean activo
) {}