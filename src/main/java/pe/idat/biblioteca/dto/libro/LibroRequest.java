package pe.idat.biblioteca.dto.libro;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LibroRequest(@NotBlank(message = "El título del libro es obligatorio")
                           @Size(min = 3, max = 100, message = "El título debe tener entre 1 y 100 caracteres")
                           String titulo,

                           @NotBlank(message = "El nombre del autor es obligatorio")
                           @Size(min = 3, max = 100, message = "El autor debe tener entre 2 y 100 caracteres")
                           String autor,

                           @NotBlank(message = "La categoría es obligatoria")
                           String categoria,

                           @NotNull(message = "El año de publicación es obligatorio")
                           @Min(value = 1000, message = "El año de publicación debe ser un valor razonable (ej: posterior a 1000)")
                           Integer anioPublicacion,

                           @NotNull(message = "El stock es obligatorio")
                           @Min(value = 0, message = "El stock no puede ser negativo")
                           Integer stock) {
}
