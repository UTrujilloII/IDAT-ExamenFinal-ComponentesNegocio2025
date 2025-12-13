package pe.idat.biblioteca.dto.libro;

public record LibroResponse(Long id,
                            String titulo,
                            String autor,
                            String categoria,
                            Integer anioPublicacion,
                            Integer stock,
                            boolean enabled)
{
}
