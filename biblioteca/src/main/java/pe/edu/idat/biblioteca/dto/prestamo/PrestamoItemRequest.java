package pe.edu.idat.biblioteca.dto.prestamo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PrestamoItemRequest(

        // El ID del libro que se quiere prestar.
        @NotNull(message = "El ID del libro no puede ser nulo.")
        Long libroId,

        // La cantidad de copias que se quieren prestar de ese libro específico.
        @NotNull(message = "La cantidad no puede ser nula.")
        @Min(value = 1, message = "La cantidad debe ser al menos 1.")
        Integer cantidad
) {}