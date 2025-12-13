package pe.edu.idat.biblioteca.dto.libro;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

public record LibroPatchRequest(

        @Size(min = 3, max = 150, message = "El título debe contener entre 3 y 150 caracteres.")
        String titulo,

        @Size(min = 3, max = 100, message = "El autor debe contener entre 3 y 100 caracteres.")
        String autor,

        @Size(min = 3, max = 100, message = "La editorial debe contener entre 3 y 100 caracteres.")
        String editorial,
        @Size(max = 19, message = "El ISBN no debe exceder los 19 caracteres.")
        String isbn,
        @Min(value = 1500, message = "El año debe ser posterior a 1500.")
        @Max(value = 2025, message = "El año no puede ser futuro.")
        Integer anioPublicacion,
        @Min(value = 0, message = "La cantidad no puede ser negativa.")
        Integer cantidad
) {}