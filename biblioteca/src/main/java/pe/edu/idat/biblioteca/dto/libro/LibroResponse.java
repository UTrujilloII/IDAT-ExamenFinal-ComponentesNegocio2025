package pe.edu.idat.biblioteca.dto.libro;
public record LibroResponse(
        Long id,
        String titulo,
        String autor,
        String editorial,
        String isbn,
        Integer anioPublicacion,
        Integer cantidad,
        String disponible
) {}