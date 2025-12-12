package pe.edu.idat.biblioteca.dto.libro;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LibroRequest(
        @NotBlank(message = "El título es obligatorio")
        @Size(max = 255, message = "El título no puede exceder 255 caracteres")
        String titulo,

        @NotBlank(message = "El autor es obligatorio")
        @Size(max = 255, message = "El autor no puede exceder 255 caracteres")
        String autor,

        @Size(max = 150, message = "La editorial no puede exceder 150 caracteres")
        String editorial,

        @Size(max = 100, message = "La categoría no puede exceder 100 caracteres")
        String categoria,

        @NotNull(message = "La cantidad total es obligatoria")
        @Min(value = 1, message = "La cantidad total debe ser al menos 1")
        Integer cantidadTotal
) {}