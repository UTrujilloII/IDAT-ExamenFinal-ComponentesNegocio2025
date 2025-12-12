package pe.edu.idat.biblioteca.dto.libro;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LibroRequest(
        @NotBlank(message = "El título es requerido")
        @Size(max = 150, message = "El título excede los 150 caracteres")
        String titulo,

        @NotBlank(message = "El autor es requerido")
        @Size(max = 100, message = "El autor excede los 100 caracteres")
        String autor,

        @NotBlank(message = "La editorial es requerida")
        @Size(max = 100, message = "La editorial excede los 100 caracteres")
        String editorial,

        @NotBlank(message = "El ISBN es requerido")
        @Size(max = 19, message = "El ISBN excede los 19 caracteres")
        String isbn,

        @NotNull(message = "El año de publicación es requerido")
        @Min(value = 1800, message = "El año debe ser posterior a 1800")
        Integer anioPublicacion,

        @NotNull(message = "La cantidad es requerida")
        @Min(value = 1, message = "Debe haber al menos 1 libro en stock")
        Integer cantidad
) {}