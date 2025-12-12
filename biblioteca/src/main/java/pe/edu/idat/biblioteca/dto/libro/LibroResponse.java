package pe.edu.idat.biblioteca.dto.libro;

// Un DTO de respuesta no lleva anotaciones de validación
public record LibroResponse(
        Long id,
        String titulo,
        String autor,
        String editorial,
        String isbn,
        Integer anioPublicacion,
        Integer cantidad,
        String disponible // <--- ¡Cambiado de Boolean a String!
) {}