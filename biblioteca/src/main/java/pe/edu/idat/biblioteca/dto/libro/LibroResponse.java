package pe.edu.idat.biblioteca.dto.libro;

public record LibroResponse (
        Long idLibro,
        String isbn,
        String titulo,
        String descripcion,
        String autor,
        String genero,
        Integer stock
){}
